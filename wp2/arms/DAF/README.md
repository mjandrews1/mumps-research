# Arm DAF - Dafny-verified translation

Operational guide: `wp2/arms/ARM_WORKING_NOTES.md` section 3 (binding).

## Toolchain (installed 2026-11-19, m1-build)

- Dafny 4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2 (self-contained
  linux-x64, Z3 bundled; installed at ~/dafny-4.11.0/dafny/dafny,
  symlinked ~/bin/dafny).
- Gate: `dafny --version` must print before each session (arm README
  requirement).

## Layout

- `src/main/kotlin/daf/` - the Kotlin translations (unit order and grading
  identical to BASE/DOC/PRO; same oracles).
- `spec/<unit>.dfy` - one Dafny method per verified unit: a SPEC of the
  Kotlin function (pre/post-conditions, loop invariants), written after the
  Kotlin translation. `dafny verify spec/<unit>.dfy` must report 0 errors /
  0 timeouts. Verification edits never change graded behavior (a logged
  deviation would be required first).
- Exemptions (pointer-heavy, IO-heavy, state-machine units) are listed in
  the unit journal; the exemption rate is itself a measured outcome.

## Session commands

```bash
cd ~/mumps-research/wp2/arms/DAF
dafny --version
gradle --no-daemon build
```
