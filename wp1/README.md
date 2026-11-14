# WP1: Preparation and Learning — Infrastructure Deliverables

Work package 1 per `RESEARCH_DESIGN_C_To_Kotlin.md` §8 (WP1). This directory
contains everything needed to run M1 on the fleet (Ubuntu LXD container
recommended; the build targets Linux x86_64).

## Contents

| File | Purpose |
|------|---------|
| `build-mumps-fleet.sh` | One-shot environment setup + build of the Mumps II Interpreter-Compiler Library (compiler in C, interpreter in C++) |
| `oracle/cases/*.mps` | Characterization test programs (deterministic; no time/random/IO-prompt constructs) |
| `oracle/run_oracle.sh` | Runs all cases against the built interpreter, captures raw outputs |
| `oracle/freeze_oracle.py` | Freeze protocol: records expected outputs + SHA-256 manifest; later runs verify against the frozen oracle |
| `oracle/README.md` | Oracle freeze procedure and grading rules |
| `order_assignments.json` + `order_assignments.md` | **Pre-registered** Latin-square arm→batch order assignments (committed before pilot; do not revise) |
| `capture_criteria.md` | **Pre-registered** heuristic capture criteria, shared verbatim by arms DOC (cookbook) and PRO (rule base) |

## Build

```bash
# 1. deps + build + install (adjust MUMPS_SRC to the extracted component dir)
sudo ./build-mumps-fleet.sh deps        # apt packages (Linux x86_64)
./build-mumps-fleet.sh build            # configure + make + deb install
./build-mumps-fleet.sh check           # sanity: interpreter runs hello.mps
# 2. freeze the oracle (FIRST run on the reference build = the freeze)
cd oracle && ./run_oracle.sh ../mumps-bin/mumps2c expected
python3 freeze_oracle.py expected      # writes manifest.json (commit this)
# 3. commit order assignments + capture criteria (already pre-registered
#    in this directory — see order_assignments.json)
```

## Environment targets (fleet)

- Ubuntu 22.04/24.04 x86_64 LXD container (matches the vendor's build host)
- Toolchain: gcc, g++, make, pkg-config, autoconf artifacts already in-tree
- Libraries: gtk+-3.0, gdkmm-3.0, sqlite3 (libsqlite3-dev), libgmp-dev,
  plus the list in `get-software.script` (Linux-only; macOS builds are NOT
  supported by the vendor build — do not attempt on the Mac hosts directly)
- Study toolchain (for WPs 2+): OpenJDK 17+, Gradle, Kotlin 2.x, Dafny 4.x
  (with Z3), SWI-Prolog 9.x

## Status / verification checklist (M1 exit criteria)

- [ ] `build-mumps-fleet.sh check` passes (hello world from source build)
- [ ] Oracle frozen: `oracle/expected/` + `manifest.json` committed; a
      re-run of `run_oracle.sh` verifies all cases against the manifest
- [ ] Order assignments committed (present in this directory — done 2026-11-14)
- [ ] Capture criteria committed (present in this directory — done 2026-11-14)
- [ ] Batch count fixed at 10 and tied to `tools/examples/EXAMPLE_protocol_seed-20261113.json`
- [ ] Study Subset batch manifests regenerated with the same seed and archived
