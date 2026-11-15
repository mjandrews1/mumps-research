# WP1/M2 — Source Characterization of the Translator's C++ Output

WP1/M2 per `RESEARCH_DESIGN_C_To_Kotlin.md` (§2, §8) and the WP1 plan:
characterize what `mumps2c` actually emits when translating, anchored to
the frozen behavioral oracle. The 15 characterization programs behind the
oracle freeze (commit `090a82f`, `wp1/oracle/cases/`) are run through the
TRANSLATOR, and everything it produces is captured, frozen, and statically
characterized.

**Protocol note:** the translator output is *source characterization data*
for seeding the WP2 cookbook and Prolog rule base (identical capture
criteria, per `wp1/capture_criteria.md`). The behavioral authority remains
the frozen interpreter oracle at `wp1/oracle/expected/` — nothing in this
directory replaces or amends it.

## Contents

| File | Purpose |
|------|---------|
| `run_m2.sh` | Runs `mumps2c` on every oracle case in a fresh temp dir per case; captures translator stdout/stderr and every generated file (snapshot-diff, so any output naming is tolerated); normalizes volatile fields |
| `freeze_m2.py` | Freeze protocol mirroring `wp1/oracle/freeze_oracle.py`: SHA-256 manifest over all captured files; `--verify` re-checks a fresh capture |
| `characterize_m2.py` | Static characterization of the generated C++: per-case LOC/comments/includes/literals/labels/control-flow/runtime-API symbols; emits `m2_characterization.json` + `m2_summary.md` |
| `capture/` | (After running on m1-build) the frozen capture + `manifest.json` — commit this |
| `analysis/` | (After running) `m2_characterization.json`, `m2_summary.md` |

## Run on the reference build (m1-build, `container` host)

```bash
cd ~/mumps-research && git pull
cd wp1/m2
./run_m2.sh /usr/bin/mumps2c capture          # first full run = the M2 capture
python3 freeze_m2.py capture                  # writes capture/manifest.json
python3 freeze_m2.py capture --verify         # must verify
python3 characterize_m2.py capture analysis ../oracle/expected
git add capture analysis && git commit -m "wp1/m2: freeze translator C++ capture + characterization"
```

## Freeze rules (mirroring the oracle freeze)

- Freeze ONCE, on the reference build (mumps-sqlite3 2026.05.14-1, tarball
  `mumps-14-may-2026.tgz`, sha256
  `621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe`).
- A differing re-capture is a protocol deviation: log it in
  `capture/PRE_FREEZE_NOTES` style before freezing, never silently replace
  a frozen manifest.
- If the translator fails on any case (nonzero rc), the run exits 1; the
  failure itself is characterization data — record it before retrying.

## What the characterization feeds

- **WP2 DOC/PRO seeding**: the runtime API surface and construct inventory
  here are the empirical basis for the first cookbook entries and Prolog
  rules (shared criteria per `wp1/capture_criteria.md`).
- **Translation difficulty covariates**: per-case metrics (labels, goto
  density, API breadth) become module-level covariates for the Study
  Subset batches containing these constructs.
- **Dialect confirmation**: the MDH findings in
  `wp1/oracle/PRE_FREEZE_NOTES.md` should reappear structurally in the
  generated C++ (e.g. `$Extract` single-position semantics, `$$`
  external-call lowering, `$Translate`-based case conversion).
## State-aware driver (recommended)

`m1_continue.sh` checks work completed to date on m1-build and proceeds
from that point forward — idempotent, safe to re-run any time:

```bash
cd ~/mumps-research && git pull
./wp1/m1_continue.sh            # executes only the next pending step
./wp1/m1_continue.sh --check    # report status only, do nothing
```

It verifies the oracle freeze and the M2 capture against fresh runs
(never re-freezes; a mismatch is a hard stop and a logged protocol
deviation), then creates the M2 capture and characterization if missing.
Exit codes: 0 = all complete and verified; 1 = verification failed
(investigate); 2 = a step ran — re-run to confirm it passes.

### From the `container` host (via lxc exec)

`lxc_continue.sh` runs the same driver inside m1-build without opening a
shell in the guest (login environment as user `builder`, args and exit
codes pass through):

```bash
./wp1/m2/lxc_continue.sh            # next pending step, inside m1-build
./wp1/m2/lxc_continue.sh --check    # status only
M1_CONTAINER=m1-build ./wp1/m2/lxc_continue.sh --check
```
