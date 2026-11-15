#!/usr/bin/env python3
"""characterize_m2.py — WP1/M2: static characterization of the translator's
C++ output, anchored to the frozen oracle cases.

Usage:
  python3 characterize_m2.py <capture-dir> <out-dir> [oracle-expected-dir]

Reads the frozen M2 capture produced by run_m2.sh (per-case generated C++
plus translator logs) and emits, into <out-dir>:

  m2_characterization.json   full machine-readable characterization
  m2_summary.md              human-readable summary report

If the frozen oracle expected/ directory is given, the summary also lists
the oracle case names so every characterization entry is visibly anchored
to a frozen-oracle case (1:1, same 15 programs).

Characterization is descriptive (pre-registered metrics only):
  - per case: generated files, LOC (total/nonblank/comment), includes,
    string literals, numeric literals, function definitions, labels
    (goto targets), goto count, control-flow keyword counts
  - corpus: runtime API surface (identifiers matching the vendor's
    mumps/mdh/symtab naming families), aggregate construct counts
Nothing here is graded against behavior; the interpreter oracle at
wp1/oracle/expected/ remains the sole behavioral authority.
"""
import json
import os
import re
import sys

CF_KEYWORDS = ["if", "else", "for", "while", "do", "switch", "case",
               "break", "continue", "goto", "return"]
API_FAMILIES = re.compile(r"^(mumps|mdh|symtab|global)", re.IGNORECASE)
IDENT = re.compile(r"\b[A-Za-z_][A-Za-z0-9_]*\b")
CALL = re.compile(r"\b([A-Za-z_][A-Za-z0-9_]*)\s*\(")
LABEL = re.compile(r"^\s*([A-Za-z_][A-Za-z0-9_]*)\s*:\s*(//.*)?$")
INCLUDE = re.compile(r'^\s*#\s*include\s*[<"]([^">]+)[">]')
STRING_LIT = re.compile(r'"(?:\\.|[^"\\])*"')
COMMENT_LINE = re.compile(r"^\s*(//|/\*|\*)")
FUNC_DEF = re.compile(r"^[A-Za-z_][A-Za-z0-9_<>:*&\s]*\b([A-Za-z_][A-Za-z0-9_]*)\s*\([^;]*\)\s*\{?\s*$")


def characterize_file(path: str) -> dict:
    with open(path, "r", errors="replace") as fh:
        lines = fh.readlines()
    text = "".join(lines)
    stats = {
        "loc_total": len(lines),
        "loc_nonblank": sum(1 for l in lines if l.strip()),
        "loc_comment": sum(1 for l in lines if COMMENT_LINE.match(l)),
        "includes": sorted(set(INCLUDE.findall(text))),
        "string_literals": len(STRING_LIT.findall(text)),
        "numeric_literals": len(re.findall(r"\b\d+(\.\d+)?\b", text)),
        "function_defs": sorted(set(FUNC_DEF.findall(text, re.M))),
        "labels": sorted({m.group(1) for m in (LABEL.match(l)
                        for l in lines) if m}),
        "control_flow": {k: len(re.findall(rf"\b{k}\b", text))
                         for k in CF_KEYWORDS},
        "api_symbols": sorted({m for m in IDENT.findall(text)
                               if API_FAMILIES.match(m)}),
        "called_symbols": sorted(set(CALL.findall(text))),
    }
    return stats


def characterize_case(case_dir: str) -> dict:
    out = {"files": {}}
    for root, _dirs, files in os.walk(case_dir):
        for fn in sorted(files):
            full = os.path.join(root, fn)
            rel = os.path.relpath(full, case_dir)
            if fn.endswith((".cpp", ".c", ".cc", ".cxx", ".h", ".hpp")):
                out["files"][rel] = characterize_file(full)
            else:
                out["files"][rel] = {"bytes": os.path.getsize(full)}
    return out


def aggregate(per_case: dict) -> dict:
    api_surface = {}
    cf_totals = {}
    totals = {"loc_total": 0, "loc_nonblank": 0, "string_literals": 0,
              "numeric_literals": 0, "labels": 0, "files": 0}
    for case, data in per_case.items():
        for rel, st in data["files"].items():
            if "loc_total" not in st:
                continue
            totals["files"] += 1
            for k in ("loc_total", "loc_nonblank", "string_literals",
                      "numeric_literals"):
                if k in st:
                    totals[k] += st[k]
            totals["labels"] += len(st.get("labels", []))
            for sym in st["api_symbols"]:
                api_surface[sym] = api_surface.get(sym, 0) + 1
            for k, v in st["control_flow"].items():
                cf_totals[k] = cf_totals.get(k, 0) + v
    return {"totals": totals, "api_surface": api_surface,
            "control_flow": cf_totals}


def write_summary(out_dir: str, per_case: dict, agg: dict,
                  oracle_cases=None) -> str:
    lines = ["# WP1/M2 — Translator C++ Output Characterization (summary)",
             "",
             "Static characterization of the mumps2c translator's C++ output",
             "for the frozen-oracle cases. Behavioral authority: the frozen",
             "interpreter oracle (wp1/oracle/expected/).", ""]
    if oracle_cases is not None:
        lines.append(f"Oracle anchoring: {len(per_case)} cases characterized, "
                     f"{len(oracle_cases)} frozen-oracle cases"
                     + ("" if set(per_case) == set(oracle_cases)
                        else "  **MISMATCH — investigate**"))
        lines.append("")
    lines.append("| Case | Files | Total LOC | Non-blank LOC | Labels | "
                 "API symbols |")
    lines.append("|------|-------|-----------|---------------|--------|"
                 "------------|")
    for case in sorted(per_case):
        st = aggregate({case: per_case[case]})["totals"]
        nfiles = len(per_case[case]["files"])
        api_n = len(set().union(*[v.get("api_symbols", [])
                for v in per_case[case]["files"].values()
                if isinstance(v, dict) and "api_symbols" in v])) \
            if per_case[case]["files"] else 0
        lines.append(f"| {case} | {nfiles} | {st['loc_total']} | "
                     f"{st['loc_nonblank']} | {st['labels']} | {api_n} |")
    lines += ["", "## Corpus totals", "",
              f"- Generated source files: {agg['totals']['files']}",
              f"- Total LOC: {agg['totals']['loc_total']} "
              f"(non-blank {agg['totals']['loc_nonblank']})",
              f"- String literals: {agg['totals']['string_literals']}; "
              f"numeric literals: {agg['totals']['numeric_literals']}",
              f"- Goto labels: {agg['totals']['labels']}",
              "", "## Control-flow construct counts (corpus)", ""]
    lines += [f"- {k}: {v}" for k, v in sorted(agg["control_flow"].items())]
    lines += ["", "## Runtime API surface (symbol → files using it)", ""]
    lines += [f"- `{sym}`: {n}" for sym, n in
              sorted(agg["api_surface"].items(), key=lambda x: (-x[1], x[0]))]
    return "\n".join(lines) + "\n"


def main() -> int:
    if len(sys.argv) < 3:
        print(__doc__)
        return 2
    capture, out_dir = sys.argv[1], sys.argv[2]
    oracle_expected = sys.argv[3] if len(sys.argv) > 3 else None

    per_case = {}
    for name in sorted(os.listdir(capture)):
        case_dir = os.path.join(capture, name)
        if os.path.isdir(case_dir):
            per_case[name] = characterize_case(case_dir)

    agg = aggregate(per_case)
    oracle_cases = None
    if oracle_expected and os.path.isdir(oracle_expected):
        oracle_cases = {n.rsplit(".", 1)[0] for n in os.listdir(oracle_expected)
                        if n.endswith(".out")}

    os.makedirs(out_dir, exist_ok=True)
    with open(os.path.join(out_dir, "m2_characterization.json"), "w") as fh:
        json.dump({"per_case": per_case, "aggregate": agg,
                   "oracle_cases": sorted(oracle_cases) if oracle_cases else None},
                  fh, indent=2, sort_keys=True)
        fh.write("\n")
    summary = write_summary(out_dir, per_case, agg, oracle_cases)
    with open(os.path.join(out_dir, "m2_summary.md"), "w") as fh:
        fh.write(summary)
    print(summary)
    return 0


if __name__ == "__main__":
    sys.exit(main())
