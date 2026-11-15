# WP1/M2 — Findings: Candidate Construct Inventory for the WP2 Pilot

**Status: characterization output — NOT knowledge artifacts.** Per the
pre-registered capture criteria (`wp1/capture_criteria.md` §1), cookbook
entries (DOC) and Prolog rules (PRO) may only be captured from actual
translation work under triggers T1/T2/T3, with provenance (arm + batch +
date) and a real corpus example. This inventory therefore does NOT contain
`H-###` entries or `rule(hNNN,...)` facts; it is the empirical basis that
lets the pilot batch reach capture quickly and consistently across arms.
Grounded in `m2_characterization.json` (commit `1d13373`, analyzer fixed
in `2ee3d83` to populate the include inventory — regenerate after that fix).

## 1. The runtime scaffold (present in all 14 generated files)

Every translated program embeds the same vendor runtime preamble/epilogue:

- Symbol table: `sym_`, `fcn` dispatch, `zfcn` (z-functions)
- Error machinery: `ErrorMessage`, `VariableNotFound`/`VariableNotFound1`,
  `setjmp`/`longjmp`, `setitimer`/`set_alarm` (hang), `SyncClock`
- Lock services: `makeLock`, `cleanLocks`, `releaseLock`, `eraseLocks`
- String runtime: `strmove`, `strcatz`, `strxcat`, `floatstring`,
  `Mltoa`; macros `CSTR`/`UNS`/`CCHR`
- Numeric runtime: `add`, `sub`, `mult`, `divi`, `divx`, `expx`,
  `numcomp`/`numcomp1`/`numcomp2`, `atof`/`atoi`/`atol`
- Globals: `Mglobal`, `Global`/`globalOrder` include family

**Pilot implication:** the highest-value first capture will likely be a
T2-magnitude "map the scaffold once to a Kotlin runtime facade" decision
(the BASE arm already has `mumps/interp/` scaffolding stubs). Expect this
to become the first cookbook entry / Prolog rule pair, verified against
any oracle case (all exercise the scaffold).

## 2. Construct families and their oracle anchors

Verification for later entries can cite `oracle-verified` where a frozen
oracle case exercises the construct:

| Canonical construct family | Corpus evidence | Oracle anchor |
|---|---|---|
| `$Extract`/`$Find` | `_extract`, `_find` calls | case_05 |
| `$Piece` | `_piece` | case_04 |
| `$Justify`, `$FNumber` | `_justify`, `_fnumber` (all files) | case_06, case_14 |
| `$Length`, `$Char` | `_length`, `strcatz` | case_03 |
| `$Translate` | `Translate` | case_10 |
| Pattern match (`?`) | `test`, pattern helpers | case_11 |
| Global array ops | `GlobalGet/Set/Order/Data`, `Merge`, `Query`, `LocalGet/Set/Order` | case_09 (heaviest: 8 API symbols), case_07 |
| Indirection (`@`) | `Interpret` | case_12 |
| `For` loops | labels `For1`/`For2` + `goto`/`do-while` (the only gotos in the corpus) | case_08 |
| Numeric comparisons | `numcomp1`/`numcomp2` chains | case_07, case_14 |
| `Hang`/device IO | `setitimer`/`set_alarm` | case_15 |

## 3. Structural findings relevant to translation strategy

- **Control flow is if-dominant and structured**: 323 `if`, 65 `while`,
  60 `else`, 37 `for`; **zero `switch`**. Only case_08 uses `goto`
  (6 gotos, 2 labels, `For1`/`For2` loop lowering). A Kotlin mapping
  strategy will lean on expression translation, not control-flow novelty.
- **Expansion ratio**: 15 small Mumps programs → 4,607 LOC C++ (4,033
  non-blank), ~330 LOC average, because the scaffold dominates. Kotlin
  equivalents that assume a shared runtime facade should collapse this
  dramatically — a measurable expectation to record for the pilot.
- **case_13 is a translator hard stop** (error 92, missing `zmain`,
  rc=9, no `.cpp`): user-function/`Do`-based subroutines cannot be
  characterized via the compiled path in this dialect; they are only
  anchored in the interpreter oracle. Any Study Subset unit relying on
  `$$`-style externs will hit this (see PRE_FREEZE_NOTES: `$$` is an
  external routine-file call here, not ISO-M).
- **Include inventory** is empty in the committed JSON due to the
  `2ee3d83` regex fix — regenerate the analysis files before relying on
  include data (expected: the `<mumpsc/*.h>` family seen in the captures).

## 4. What this inventory does NOT do

- It does not pre-answer translation decisions (G1/G2: entries must come
  from resolved units, in canonical form, with real examples).
- It does not embed oracle outputs (G3).
- It is not deduplicated against any arm artifact (G4) — no entries exist
  yet in either arm, so the pilot starts from an empty artifact set, as
  pre-registered.
