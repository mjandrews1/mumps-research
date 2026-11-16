# Per-Unit Checks: Definitions

**Status:** Authoritative definition, effective 2026-11-16 (registry
entry C-002, `wp2/PROTOCOL_CLARIFICATIONS.md`).

**Provenance.** The four per-unit verification columns (code-check,
output-check, runtime-check, target-check) entered use with batch 2 in
`wp2/PILOT_TRACKING.md` on 2026-11-15 without a written definition;
units 50-51 were graded under the operative practice described below.
This document formalizes that practice unchanged — the researcher
confirmed (2026-11-16) that the written definitions match the practice
under which those units were graded. No graded value is reinterpreted.

**Scope.**
- Applies to every study unit, every arm, from batch 2 onward, and to
  the pending B1 backfill pass (all B1 entries stay `todo` until that
  pass runs; nothing is backfilled).
- In `wp2/PILOT_TRACKING.md` the four columns track the **BASE
  reference translation**; each other arm's check results are recorded
  in that arm's journal/log when its batch closes.
- The checks decompose a unit's overall status (todo / translating /
  graded-ok / graded-deviation / deferred); a unit may be `graded-ok`
  overall with individual checks still `todo`, because the checks are
  executed in the batch verification pass.

| Check | Verifies | Instrument |
|---|---|---|
| code-check | Static structural and semantic fidelity | Side-by-side review vs. the C/C++ original; arm build |
| output-check | Behavioral equivalence | Frozen oracle, byte comparison |
| runtime-check | Correct behavior at the edges and under state | Edge/error-path exercise; regression and property tests |
| target-check | Idiomatic Kotlin per the target specification | Kotlin style guide (design §7.2) |

## 1. code-check — static equivalence review

The Kotlin translation is read against the original C/C++ unit, and
every branch, operator, side effect, and vendor quirk is accounted for
(the faithful-quirk discipline of the BASE pilot: comment/code
mismatches, uninitialized reads, silent promotions, and dead parameters
are preserved deliberately and noted — never silently "fixed"). The
unit must compile warning-clean in the arm's build. Missing or invented
behavior relative to the original = `graded-deviation`.

## 2. output-check — behavioral equivalence vs. the frozen oracle

Matched inputs produce byte-equivalent outputs:

- interpreter/library units → frozen interpreter oracle
  (`wp1/oracle/expected/`);
- translator units → frozen translator oracle
  (`wp2/translator-oracle/expected/`).

Byte equivalence is required; any intentional non-equivalence must be
logged as a protocol deviation BEFORE proceeding
(`wp2/arms/ARM_WORKING_NOTES.md` §0.2).

## 3. runtime-check — behavior under runtime conditions

Beyond single-shot I/O equivalence, the unit is exercised at its edges:
boundary and degenerate inputs, error paths, and state interactions
(unit, regression, and property tests per design §9.3). The translation
must not crash, hang, or throw uncaught exceptions on inputs the C/C++
original handles; divergence at an edge = `graded-deviation` with the
edge case recorded in the arm journal.

## 4. target-check — Kotlin target-spec compliance

The translation complies with the Kotlin target specification
(design §7.2): idiomatic Kotlin, null safety, immutable data where
appropriate, no transliterated C idioms (pointer workarounds, C-style
index loops where ranges exist, etc.). Functional equivalence alone does
not pass this check — the unit must be a genuine Kotlin citizen.

## Status vocabulary

Each check uses: `todo` / `graded-ok` / `graded-deviation` /
`deferred`. A check may be `deferred` only with a reason logged in
the arm journal (e.g., an oracle coverage gap); deferrals are reported
data, never silent.

## History

- 2026-11-15: the four check columns enter use with batch 2;
  units 50-51 graded-ok on all four under the operative practice.
- 2026-11-16: B1 table retrofitted with the same columns (all `todo`);
  this definition written and logged as registry entry C-002.
