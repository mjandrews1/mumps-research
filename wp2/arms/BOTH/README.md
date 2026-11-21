# Arm BOTH - Prolog rule base + Dafny-verified translation

Operational guide: `wp2/arms/ARM_WORKING_NOTES.md` section 4 (binding).

Factor levels combined: the PRO workflow (machine-queried rule base,
events under `wp2/logs/BOTH_knowledge_events.csv` via the BOTH-local
`knowledge_tooling.pl`) AND the DAF workflow (per-unit Dafny specs under
`spec/`, verified 0 errors / 0 timeouts; verification outcomes feed
back into the rule base, each amendment recorded).

## Rule base

`rules_v0.pl` is a SEPARATE per-arm file, seeded 2026-11-22 as a
byte-identical copy of PRO's 18-rule `rules_v0.pl` (identical starting
condition required by the design, section 4 of the journal; the seed
carries each rule's most recent verification upgrade). It evolves only
through BOTH work; PRO's artifact is never edited by this arm.

## Layout

- `src/main/kotlin/both/` - the Kotlin translations (unit order and
  grading identical to BASE/DOC/PRO/DAF; same oracles). Arm
  independence: renders from pristine vendor sources only; no
  DOC/PRO/DAF Kotlin is consulted.
- `spec/<unit>.dfy` - a SPEC of the Kotlin function (pre/post,
  loop invariants), written after the Kotlin translation; pure
  decidable units only, exemptions logged like DAF.

## Session commands

```bash
cd ~/mumps-research/wp2/arms/BOTH
dafny --version
dafny verify spec/*.dfy
gradle --no-daemon build
```
