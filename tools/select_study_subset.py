#!/usr/bin/env python3
"""
select_study_subset.py — Random, reproducible selection of modules/chunks for the
Mumps II (Interpreter-Compiler Library) C/C++-to-Kotlin translation study
(arms BASE, DOC, PRO, DAF, BOTH).

Implements the sampling requirements of RESEARCH_DESIGN_C_To_Kotlin.md v1.2:
  1. Stratify translation units (files or functions) by a complexity score.
  2. Randomly and reproducibly assign units to N balanced Study-Subset batches
     (seeded shuffle within strata, then greedy balanced assignment).
  3. Randomly draw the micro-replication units (3 per arm by default).
  4. Emit a protocol log (seed, source snapshot, hashes) suitable for
     pre-registration: commit it BEFORE the pilot batch, and never regenerate.

Subject handling: scans C (.c/.h) and C++ (.cpp/.cc/.hpp/.hh) sources. By
default it EXCLUDES autoconf templates (*.in), packaging payloads (*/usr/*,
*/DEBIAN/*), and autom4te caches — the O'Kane distribution ships near-duplicate
copies in all of those. Each unit is tagged with its language (c / cpp).

Determinism: all randomness flows from --seed via hashlib-based PRNGs, so the
same source tree + seed always yields the identical assignment.

Usage:
  ./select_study_subset.py --source DIR [--unit file|function] [--batches 10]
                           [--min-loc 15] [--micro 3] [--micro-shared]
                           [--exclude GLOB ...] --seed 20261113 --out DIR
"""

import argparse
import csv
import datetime
import fnmatch
import hashlib
import json
import os
import re
import sys

SRC_EXT = (".c", ".h", ".cpp", ".cc", ".hpp", ".hh")

# Templates, packaging payloads, and build caches: near-duplicates of the
# real sources in the O'Kane distribution. Override/add with --exclude.
DEFAULT_EXCLUDES = ["*.in", "*/usr/*", "*/DEBIAN/*", "*/autom4te.cache/*",
                    "*/Checkout/*"]

# ---------------------------------------------------------------- PRNG (seeded)

class SeededPRNG:
    """Small deterministic PRNG: sha256 counter mode. Independent streams per
    purpose (stratify / micro / arm draws) so adding a stream never perturbs
    existing draws."""

    def __init__(self, seed: str, stream: str = "default"):
        self.base = f"{seed}|{stream}|".encode()
        self.counter = 0

    def _next(self):
        h = hashlib.sha256(self.base + str(self.counter).encode()).digest()
        self.counter += 1
        return int.from_bytes(h[:8], "big")

    def random(self) -> float:
        return self._next() / 2**64

    def shuffle(self, xs):
        # Fisher-Yates with deterministic draws
        xs = list(xs)
        for i in range(len(xs) - 1, 0, -1):
            j = self._next() % (i + 1)
            xs[i], xs[j] = xs[j], xs[i]
        return xs

    def sample(self, xs, k):
        if k > len(xs):
            raise ValueError(f"cannot draw {k} from {len(xs)} units")
        xs = list(xs)
        out = []
        for _ in range(k):
            j = self._next() % len(xs)
            out.append(xs.pop(j))
        return out

# ------------------------------------------------------------ unit extraction

C_KEYWORDS = {"if", "while", "for", "switch", "return", "else", "sizeof",
              "case", "do", "goto", "typedef", "struct", "union", "enum",
              # C++ additions
              "new", "delete", "try", "catch", "throw", "namespace",
              "using", "class", "template", "operator", "static_cast",
              "reinterpret_cast", "const_cast", "dynamic_cast"}

# C/C++ function-definition head: allows return types, `Class::method`,
# `~Destructor`, references `&`, pointers `*`, templates `<...>` and
# default args in the parameter list.
FUNC_HEAD = re.compile(
    r"^[A-Za-z_~][A-Za-z0-9_:\s\*&]*<[^;]*>\([^;]*\)[\s]*\{?\s*"  # templated
    r"|^[A-Za-z_~][A-Za-z0-9_:\s\*&<>]*\([^;]*\)[\s]*(const)?[\s]*\{?\s*$")
BRACE_TOKENS = {"{": 1, "}": -1}
STRIP_COMMENT_LINE = re.compile(r"^\s*(/\*|\*|//)")

def _physical_loc(lines):
    """Non-blank, non-comment-only lines."""
    n = 0
    for ln in lines:
        s = ln.strip()
        if s and not STRIP_COMMENT_LINE.match(s):
            n += 1
    return n

def _brace_delta(line, in_comment):
    """Net brace delta of a line, ignoring //, /*...*/ (multi-line aware),
    string and char literals. Returns (delta, in_comment_after)."""
    delta, i, n = 0, 0, len(line)
    while i < n:
        if in_comment:
            j = line.find("*/", i)
            if j == -1:
                return delta, True
            i, in_comment = j + 2, False
            continue
        c = line[i]
        if c == "/" and i + 1 < n and line[i + 1] == "/":
            break
        if c == "/" and i + 1 < n and line[i + 1] == "*":
            in_comment = True
            i += 2
            continue
        if c in ("\"", "'"):
            quote = c
            i += 1
            while i < n and line[i] != quote:
                i += 2 if line[i] == "\\" else 1
            i += 1
            continue
        if c in BRACE_TOKENS:
            delta += BRACE_TOKENS[c]
        i += 1
    return delta, in_comment


HEAD_START = re.compile(r"^[A-Za-z_~][A-Za-z0-9_:\s\*&<>,]*\(")
HEAD_FULL = re.compile(r"^[A-Za-z_~].*\([^;]*\)[\s]*(const)?[\s]*\{?\s*$")


def _match_head(lines, i):
    """If a function-definition head starts at lines[i], return the index of
    its last line (handles multi-line parameter lists), else None."""
    line = lines[i]
    if line.strip().endswith(";"):
        return None
    if FUNC_HEAD.match(line):
        return i
    if not HEAD_START.match(line):
        return None
    joined, j = line, i
    while j < min(i + 3, len(lines) - 1):
        j += 1
        joined += " " + lines[j]
        if joined.count("(") <= joined.count(")"):
            if HEAD_FULL.match(joined):
                return j
            break
    return None


def extract_functions(lines):
    """Heuristic top-level C/C++ function chunker. Returns [(name, body)]
    where body is a slice of the SAME line objects, so callers can compute
    the residual <misc> lines by identity."""
    units, i, n = [], 0, len(lines)
    while i < n:
        head_end = _match_head(lines, i)
        if head_end is None:
            i += 1
            continue
        head_text = " ".join(lines[i:head_end + 1])
        # collect until braces balance (opening brace may be on a later line)
        depth, j, started, in_comment = 0, head_end, False, False
        while j < n:
            d, in_comment = _brace_delta(lines[j], in_comment)
            if d or started:
                depth += d
                started = True
            if started and depth <= 0:
                break
            j += 1
        if started and j < n:
                body = lines[i:j + 1]
                # name = last identifier (possibly :: qualified) before '('
                pm = re.search(r"([A-Za-z_~][A-Za-z0-9_:]*)\s*\(", head_text)
                name = pm.group(1) if pm else f"line{i}"
                if name.split("::")[-1] in C_KEYWORDS:
                    i = j + 1               # control statement, not a function
                    continue
                units.append((name, body))
                i = j + 1
                continue
        i += 1
    return units

def complexity_metrics(lines):
    text = "\n".join(lines)
    loc = _physical_loc(lines)
    comments = max(1, sum(1 for ln in lines if STRIP_COMMENT_LINE.match(ln)))
    ptr = len(re.findall(r"\*[_A-Za-z]|->|malloc|free\s*\(|memcpy|memset", text))
    ctrl = len(re.findall(r"\b(if|for|while|switch|goto|case)\b", text))
    calls = len(re.findall(r"\b[A-Za-z_][A-Za-z0-9_]*\s*\(", text))
    score = (loc / 10.0) + 2.0 * ptr + 1.0 * ctrl + 0.2 * calls
    return {
        "loc": loc,
        "comment_ratio": round(comments / max(1, comments + loc), 3),
        "pointer_marks": ptr,
        "control_marks": ctrl,
        "score": round(score, 1),
    }

def _lang(rel):
    ext = os.path.splitext(rel)[1]
    return "cpp" if ext in (".cpp", ".cc", ".hpp", ".hh") else "c"

def _excluded(rel, patterns):
    rel = rel.replace(os.sep, "/")
    parts = rel.split("/")
    dirs = parts[:-1]
    for p in patterns:
        if fnmatch.fnmatch(rel, p) or fnmatch.fnmatch("/" + rel, "/" + p):
            return True
        # "*/Checkout/*" also excludes a "Checkout/" at the tree root
        if p.startswith("*/") and p.endswith("/*") and p[2:-2] in dirs:
            return True
    return False

def load_units(source, unit, min_loc, chunk_loc, excludes):
    units = []
    for root, _dirs, files in os.walk(source):
        for f in sorted(files):
            if not f.endswith(SRC_EXT):
                continue
            p = os.path.join(root, f)
            rel = os.path.relpath(p, source)
            if _excluded(rel, excludes):
                continue
            try:
                text = open(p, encoding="utf-8", errors="replace").read()
            except OSError as e:
                print(f"warning: cannot read {p}: {e}", file=sys.stderr)
                continue
            lines = text.splitlines()
            lang = _lang(rel)
            if unit == "file":
                if _physical_loc(lines) >= min_loc:
                    units.append({"id": rel, "lang": lang, "lines": lines})
                continue
            # function mode
            fns = extract_functions(lines)
            misc = []
            claimed = {id(l) for _n, b in fns for l in b}
            for ln in lines:
                if id(ln) not in claimed:
                    misc.append(ln)
            for name, body in fns:
                if _physical_loc(body) >= min_loc:
                    units.append({"id": f"{rel}::{name}", "lang": lang,
                                  "lines": body})
                else:
                    misc.extend(body)
            misc_loc = _physical_loc(misc)
            if misc_loc < min_loc:
                continue
            if misc_loc <= chunk_loc:
                units.append({"id": f"{rel}::<misc>", "lang": lang,
                              "lines": misc})
            else:
                # split oversized residual chunks into comparable-size units
                part, count = [], 1
                for ln in misc:
                    part.append(ln)
                    if _physical_loc(part) >= chunk_loc:
                        units.append({"id": f"{rel}::<misc-{count}>",
                                      "lang": lang, "lines": part})
                        part, count = [], count + 1
                if _physical_loc(part) >= min(min_loc, 1):
                    units.append({"id": f"{rel}::<misc-{count}>",
                                  "lang": lang, "lines": part})
    for u in units:
        u.update(complexity_metrics(u["lines"]))
        del u["lines"]
    return units

# ------------------------------------------------------------------ assignment

STRATA = ["simple", "core", "complex"]

def stratify(units):
    """Rank by score, split into 3 equal strata (low/mid/high complexity)."""
    ranked = sorted(units, key=lambda u: u["score"])
    k = len(ranked)
    third = max(1, k // 3)
    for i, u in enumerate(ranked):
        u["stratum"] = STRATA[min(2, i // third)] if k >= 3 else STRATA[1]
        u["complexity_rank"] = i + 1
    return ranked

def assign_batches(ranked, n_batches, seed):
    """Seeded shuffle within each stratum, then greedy balanced assignment:
    each unit (in random order) goes to the batch with the fewest units so
    far; ties broken by lowest accumulated score. Batches end up equal in
    unit count and close in complexity/LOC, while WHICH units land in which
    batch is genuinely random (unlike a post-shuffle sort, which would erase
    the seed)."""
    rng = SeededPRNG(seed, "batches")
    per_batch = {b: 0 for b in range(1, n_batches + 1)}
    score_sum = {b: 0.0 for b in range(1, n_batches + 1)}
    target = -(-len(ranked) // n_batches)  # ceil; cap per batch per stratum
    for stratum in STRATA:
        members = rng.shuffle([u for u in ranked if u["stratum"] == stratum])
        local_count = {b: 0 for b in range(1, n_batches + 1)}
        for u in members:
            # prefer batches under the stratum's unit cap, then lowest load
            cand = [b for b in range(1, n_batches + 1)
                    if local_count[b] < target] or list(range(1, n_batches + 1))
            b = min(cand, key=lambda x: (local_count[x], score_sum[x], x))
            u["batch"] = b
            per_batch[b] += 1
            local_count[b] += 1
            score_sum[b] += u["score"]
    return per_batch

def draw_micro(ranked, arms, n_micro, seed, shared):
    draws = {}
    if shared:
        pool = [u["id"] for u in ranked]
        pick = SeededPRNG(seed, "micro-shared").sample(pool, n_micro)
        for a in arms:
            draws[a] = pick
    else:
        pool = [u["id"] for u in ranked]
        for a in arms:
            draws[a] = SeededPRNG(seed, f"micro-{a}").sample(pool, n_micro)
    return draws

# ------------------------------------------------------------------ outputs

def snapshot_hashes(source, excludes):
    """Hash every scanned source file so the protocol log pins the state."""
    out = {}
    for root, _d, files in os.walk(source):
        for f in sorted(files):
            if f.endswith(SRC_EXT):
                p = os.path.join(root, f)
                rel = os.path.relpath(p, source)
                if _excluded(rel, excludes):
                    continue
                h = hashlib.sha256()
                with open(p, "rb") as fh:
                    for blk in iter(lambda: fh.read(65536), b""):
                        h.update(blk)
                out[rel] = h.hexdigest()[:16]
    return out

def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--source", required=True,
                    help="Mumps II (Interpreter-Compiler Library) source directory")
    ap.add_argument("--unit", choices=["file", "function"], default="function",
                    help="translation unit granularity (default: function)")
    ap.add_argument("--batches", type=int, default=10, help="Study Subset batches")
    ap.add_argument("--min-loc", type=int, default=15,
                    help="minimum physical LOC for a standalone unit")
    ap.add_argument("--chunk-loc", type=int, default=600,
                    help="max physical LOC per residual <misc> chunk "
                         "(oversized chunks are split)")
    ap.add_argument("--micro", type=int, default=3,
                    help="micro-replication units per arm")
    ap.add_argument("--micro-shared", action="store_true",
                    help="same micro units for all arms (default: per-arm draws)")
    ap.add_argument("--arms", nargs="+",
                    default=["BASE", "DOC", "PRO", "DAF", "BOTH"])
    ap.add_argument("--exclude", nargs="*", default=[],
                    help="extra glob excludes (in addition to the defaults)")
    ap.add_argument("--seed", required=True,
                    help="pre-registered seed (commit it before the pilot)")
    ap.add_argument("--out", required=True, help="output directory")
    args = ap.parse_args()

    excludes = DEFAULT_EXCLUDES + list(args.exclude)
    os.makedirs(args.out, exist_ok=True)
    units = load_units(args.source, args.unit, args.min_loc, args.chunk_loc,
                       excludes)
    if not units:
        sys.exit("error: no translation units found (check --source / --min-loc)")
    ranked = stratify(units)
    per_batch = assign_batches(ranked, args.batches, args.seed)
    micro = draw_micro(ranked, args.arms, args.micro, args.seed,
                       args.micro_shared)

    # ---- units.csv : full manifest with metrics, stratum, batch
    units_csv = os.path.join(args.out, "units.csv")
    cols = ["id", "lang", "loc", "comment_ratio", "pointer_marks",
            "control_marks", "score", "complexity_rank", "stratum", "batch"]
    with open(units_csv, "w", newline="") as fh:
        w = csv.DictWriter(fh, fieldnames=cols, extrasaction="ignore")
        w.writeheader()
        for u in sorted(ranked, key=lambda x: (x["batch"], -x["score"])):
            w.writerow(u)

    # ---- micro_replication.csv
    micro_csv = os.path.join(args.out, "micro_replication.csv")
    with open(micro_csv, "w", newline="") as fh:
        w = csv.writer(fh)
        w.writerow(["arm", "unit_id"])
        for a in args.arms:
            for uid in micro[a]:
                w.writerow([a, uid])

    # ---- batch_plan.md : human-readable work order
    plan = [f"# Study Subset Batch Plan (seed {args.seed})",
            f"Generated: {datetime.datetime.now().isoformat(timespec='seconds')}",
            f"Subject: Mumps II Interpreter-Compiler Library",
            f"Unit granularity: {args.unit}; units: {len(ranked)}; "
            f"batches: {args.batches}",
            f"Excludes: {', '.join(excludes)}", "",
            "| Batch | Units | Strata (s/c/x) | Total LOC | C / C++ |",
            "|---|---|---|---|---|"]
    for b in range(1, args.batches + 1):
        us = [u for u in ranked if u["batch"] == b]
        scx = [u["stratum"][0] for u in us]
        plan.append(f"| B{b} | {len(us)} | "
                    f"{scx.count('s')}/{scx.count('c')}/{scx.count('x')} | "
                    f"{sum(u['loc'] for u in us)} | "
                    f"{sum(1 for u in us if u['lang']=='c')} / "
                    f"{sum(1 for u in us if u['lang']=='cpp')} |")
    plan += ["", "## Batch contents", ""]
    for b in range(1, args.batches + 1):
        plan.append(f"### B{b}")
        for u in sorted((u for u in ranked if u["batch"] == b),
                        key=lambda x: x["score"]):
            plan.append(f"- `{u['id']}` [{u['lang']}] — {u['stratum']}, "
                        f"LOC {u['loc']}, score {u['score']}")
        plan.append("")
    open(os.path.join(args.out, "batch_plan.md"), "w").write("\n".join(plan))

    # ---- protocol.json : the pre-registration record
    protocol = {
        "tool": "select_study_subset.py",
        "subject": "Mumps II Interpreter-Compiler Library",
        "seed": args.seed,
        "generated": datetime.datetime.now().isoformat(timespec="seconds"),
        "source_dir": os.path.abspath(args.source),
        "unit": args.unit,
        "min_loc": args.min_loc,
        "chunk_loc": args.chunk_loc,
        "batches": args.batches,
        "arms": args.arms,
        "micro_per_arm": args.micro,
        "micro_shared": args.micro_shared,
        "excludes": excludes,
        "unit_count": len(ranked),
        "lang_split": {l: sum(1 for u in ranked if u["lang"] == l)
                       for l in ("c", "cpp")},
        "stratum_sizes": {s: sum(1 for u in ranked if u["stratum"] == s)
                          for s in STRATA},
        "units_per_batch": per_batch,
        "source_hashes": snapshot_hashes(args.source, excludes),
    }
    with open(os.path.join(args.out, "protocol.json"), "w") as fh:
        json.dump(protocol, fh, indent=2)

    # ---- console summary
    print(f"Subject: Mumps II Interpreter-Compiler Library")
    print(f"Units: {len(ranked)}  "
          f"(c={protocol['lang_split']['c']}, "
          f"cpp={protocol['lang_split']['cpp']}; strata: "
          + ", ".join(f"{s}={protocol['stratum_sizes'][s]}"
                      for s in STRATA) + ")")
    for b in range(1, args.batches + 1):
        us = [u for u in ranked if u["batch"] == b]
        print(f"  B{b:2d}: {len(us):3d} units, "
              f"{sum(u['loc'] for u in us):5d} LOC, "
              f"c/cpp {sum(1 for u in us if u['lang']=='c')}"
              f"/{sum(1 for u in us if u['lang']=='cpp')}")
    print(f"Micro-replication draws per arm: {args.micro} "
          f"({'shared' if args.micro_shared else 'per-arm'})")
    print(f"Outputs written to {args.out}: units.csv, micro_replication.csv, "
          f"batch_plan.md, protocol.json")
    print("REMINDER: commit these outputs (esp. protocol.json) before the "
          "pilot batch; regeneration with the same seed is verification, "
          "with a different seed is a protocol deviation.")

if __name__ == "__main__":
    main()
