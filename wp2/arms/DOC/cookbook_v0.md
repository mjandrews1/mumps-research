# Heuristic Cookbook v0 (arm DOC)

Seeded 2026-11-15 from `wp2/logs/BASE_heuristic_log.csv` (rows cited are
that log's row ids; arm BASE, batches B1 + B2 open). Capture basis per
`wp1/capture_criteria.md` §1: every entry below satisfies **T1
(recurrence ≥2)** — one also via **T2/T3** (red builds / external
reference). Schema per §2. Gates G1–G4 checked at seeding: triggers are
canonical constructs (G2), no oracle outputs embedded (G3), no duplicate
triggers (G4 — deduplicated at seeding; later revisions increment a
version field instead of adding entries).

**Lookup logging (DOC instrumentation, per §4):** before consulting this
cookbook on a unit, append a row to the arm journal with unit id, entry
id consulted, and outcome (hit-reuse / hit-adapt / miss). Batch coverage
checklists are recorded per batch in the journal (manual equivalent of
PRO's `coverage_report/0`).

---

## H-001 · Canonical IO type

- **trigger:** any function signature with a `FILE*` parameter (canonical
  construct `io-stream-param`)
- **condition:** applies to all unit signatures; exceptions only at
  program entry/exit edges
- **action:** map to `java.io.Writer` everywhere; `PrintWriter` only at
  program edges. Writer throws IOException explicitly like C — do not
  substitute silent-error streams.
- **example:** compiler-copyright-notice.c::copyright_notice (log rows 1, 5)
- **provenance:** arm BASE, batch B1, 2026-11-14 (seeded 2026-11-15)
- **verification:** unit-tested (B1 builds green)

## H-002 · Small multi-emission macro

- **trigger:** preprocessor macro expanding to ≥1 statement with a
  parameter (`simple-output-macro`)
- **condition:** no state dependency beyond its arguments
- **action:** local helper function preserving call-site shape
- **example:** Sysout macro (row 2)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-003 · Stateful macro

- **trigger:** macro whose expansion reads/writes file-scope state or
  emits via shared globals (`state-macro`)
- **condition:** state exists in the unit's global set
- **action:** member function of the state singleton (H-006's object),
  one-line call sites preserved
- **example:** INTERPRET macro → CompilerState.interpret (row 9; also
  rows 12, 37, 84, 103)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-004 · File-scope globals

- **trigger:** file-scope mutable globals required by multiple functions
  (`globals-ext`)
- **condition:** Kotlin object replaces C linkage; one owner object per
  C translation unit's global set
- **action:** singleton `object` (e.g. `CompilerState`) hosting the
  globals as properties; register each added property with its source
  header line
- **example:** compiler-globals-ext.h → CompilerState (row 6; also rows
  22, 95, 104; unit 31 incident adopted one-owner-per-C-entity rule)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-005 · Out-parameter → data-class return

- **trigger:** pointer out-parameter whose value(s) are consumed only
  via the return path of the same call (`out-param-bundle`)
- **condition:** Kotlin return slot free; values form a coherent bundle
- **action:** small data class returned from the function
- **example:** short *pd1Last → AtTestResult (row 7; also rows 30, 91, 92)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-006 · Out-parameter → holder array

- **trigger:** out-parameter updated in place where callers pass the
  same variable repeatedly or the C API shape must survive batch
  integration (`out-param-holder`)
- **condition:** when H-005's return-bundle shape would break call-site
  fidelity
- **action:** typed single-element holder array (`ShortArray(1)`,
  `IntArray(1)`); read/write `[0]` at call sites
- **example:** LocalArray Set_flg (row 66); GlobalArray GBL_flg (row 113)
- **provenance:** BASE, B1–B2, 2026-11-15
- **verification:** unit-tested

## H-007 · Buffer role classification

- **trigger:** `unsigned char[]` local buffer (`char-buffer`)
- **condition:** classify by role, not by declaration
- **action:** scan-only → `String`; mutable scratch → `StringBuilder`;
  pointer-into-buffer → offset parameter
- **example:** compiler-if.c xd/pd1 (row 8; also rows 23, 45)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-008 · Shared static scratch buffer

- **trigger:** C `static char tmp[...]` shared scratch reused across
  calls (`static-scratch`)
- **condition:** applies ONLY after verifying no caller aliases the
  buffer across calls; otherwise keep the divergence documented
- **action:** fresh `String` per call; NULL-in returns `""`; fast paths
  return the original object
- **example:** interp-sql.cpp::embed (row 112; also row 109 context)
- **provenance:** BASE, B2, 2026-11-15
- **verification:** unit-tested

## H-009 · sprintf format-string idiom

- **trigger:** `sprintf`/`fprintf` with C-locale format specifiers
  (`format-string`)
- **condition:** byte-equivalence of emitted text required
- **action:** exact Kotlin formatter with explicit `Locale.US`;
  normalize idiom collapses first (e.g. `%2x` of a byte is always a
  2-char hex string); keep trailing-newline behavior of ctime-style
  formats verbatim
- **example:** ctime header (row 3; also rows 26, 33, 79, 93, 109)
- **provenance:** BASE, B1–B2, 2026-11-14/15
- **verification:** unit-tested

## H-010 · Numeric promotion / truncation

- **trigger:** mixed-width integer arithmetic or `atoll`-style
  conversions (`numeric-promotion`)
- **condition:** document every widening; C silent promotions become
  explicit Kotlin types
- **action:** widen explicitly (`Short`→`Int` etc.); NEVER rely on
  Kotlin operator overloads across mixed numeric types (UShort+Short
  has no overload — red build row 89)
- **example:** interp-sym.cpp::_SymFree dead param + row 13, 65, 67
- **provenance:** BASE, B1–B2, 2026-11-15 (T1 + T2: red build)
- **verification:** unit-tested

## H-011 · goto → structured control

- **trigger:** backward or forward `goto` within one function (`goto`)
- **condition:** preserve observable iteration and fallthrough order
- **action:** backward → `while(true)` + boolean flag; forward → flag
  fallthrough
- **example:** compiler-if.c if_again/if_structure (row 10; also 73, 81)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested

## H-012 · Unsigned char domain mapping

- **trigger:** `strcmp`/`strcpy`/`strchr` on `unsigned char` domains
  (`unsigned-byte-domain`)
- **condition:** C compares unsigned char per the standard
- **action:** map bytes to `Char(0..255)`; range comparison semantics
  exact for the byte domain
- **example:** interp-native-btree.cpp::scanblocks (rows 14, 52, 111)
- **provenance:** BASE, B1–B2, 2026-11-15
- **verification:** unit-tested

## H-013 · Dead store / dead parameter

- **trigger:** assigned value recomputed before any observable read;
  unused parameter (`dead-store`)
- **condition:** only when provably unobservable; keep the drop
  documented in-code
- **action:** drop the store/parameter with a comment; never "fix" by
  initializing (changes semantics)
- **example:** scanblocks dead memcpy store (row 111); SymFree dead `i`
  (retro quirk ledger)
- **provenance:** BASE, B1–B2, 2026-11-15
- **verification:** unit-tested

## H-014 · Out-of-scope C function → stub

- **trigger:** call to a C function owned by an unselected unit
  (`out-of-scope-call`)
- **condition:** the owning unit is named in `units.csv` and not yet
  translated
- **action:** TODO stub in the arm's `Stubs.kt` naming the owner unit;
  delegate when the owner lands (Extract2 precedent, B2 opening commit)
- **example:** memcheck stub (row 113); rows 64, 90, 96, 101
- **provenance:** BASE, B1–B2, 2026-11-15
- **verification:** untested (stub — flagged)

## H-015 · Kotlin grammar vs quoted C source

- **trigger:** Kotlin string/comment/template containing C source
  text (`grammar-collision`)
- **condition:** applies to any KDoc or literal quoting C; pre-push scan
  before EVERY push
- **action:** C `*/` inside block comments → reword and reference the
  constant; `$z`/`$` in strings → escape; raw control/high bytes → build
  `Char(nnn)` programmatically; check `for (x in ...)` shadowing
- **example:** patrn1 KDoc `*/` (row 82); zcall `$z` (row 89); LocalArray
  NUL escape (pre-push scan catches)
- **provenance:** BASE, B1, 2026-11-15 (T1 + T2/T3: red builds,
  Kotlin grammar reference)
- **verification:** unit-tested

## H-016 · Statement-level comma operator

- **trigger:** C comma operator sequencing at statement level
  (`comma-operator`)
- **condition:** statement level; comma inside `for`-update clauses maps
  separately
- **action:** split into sequential statements (`;`)
- **example:** interp-sym.cpp::_SymFree red build (row 76)
- **provenance:** BASE, B1, 2026-11-15 (T2: red build, single follow-up fix)
- **verification:** unit-tested

## H-017 · snake_case → camelCase

- **trigger:** any translated function/variable name (`naming`)
- **condition:** always; document per-arm mapping
- **action:** mechanical rename snake→camel; keep a unit-journal note of
  renames that collide with Kotlin keywords (`in`, `is`, `object`)
- **example:** copyright_notice → copyrightNotice (row 4)
- **provenance:** BASE, B1, 2026-11-14
- **verification:** unit-tested
