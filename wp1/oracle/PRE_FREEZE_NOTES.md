# Pre-freeze corrections and dialect findings (2026-11-14)

Recorded BEFORE the oracle freeze on the reference build (m1-build LXD
container, Ubuntu 24.04, mumps-sqlite3 2026.05.14-1). Nothing here is a
post-freeze protocol deviation: the freeze had not yet occurred.

## Reference build provenance

- Source tarball: mumps-14-may-2026.tgz
- SHA-256: 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe
- Environment: Ubuntu 24.04 x86_64 (LXD), gcc/g++ 13.3.0, sqlite3 3.45.1

## Harness corrections (pre-freeze)

1. `run_oracle.sh` now targets the INTERPRETER (`mumps`), not the `mumps2c`
   translator. mumps2c only translates .mps to C++; its captured output
   was a translator log, not program behavior. The interpreter executes
   source files directly (`mumps prog.mps`) and needs a freshly created
   `mumps.sqlite` in the working directory (via `mumps-sql-db-create`).
   Each run now uses a clean temp directory so db state cannot leak.
2. `case_05_extract_find.mps` revised: indeterminate `*` subscripts are
   unsupported in this dialect (E1 multiple adjacent operators on
   `$Extract(s,*-3,*)`). Replaced with numeric subscripts.
3. `case_13_user_functions.mps` revised twice: (a) interpreted programs
   run top-of-file, so main code must precede labels; (b) `$$name(args)`
   is an external routine-file call in this dialect (E26 File error),
   not a local extrinsic function, so the case now covers Do-based
   subroutines with argument passing only.
4. `case_10_translate_case.mps` revised: `$ZConvert` is not present in
   this build ("Z function ZConvert not found"). Case conversion is
   expressed via `$Translate`.
5. `build-mumps-fleet.sh` fetch stage fixed: extraction target was one
   directory too deep (`-C "$(dirname $MUMPS_SRC)/.."` should be
   `-C "$(dirname $MUMPS_SRC)/../.."`).

## MDH dialect findings (characterization targets, not bugs)

These differences from ISO 11756 M were observed on the reference build
and are intentionally captured by the oracle:

- **$Extract single position**: `$Extract(s,4)` returns the single
  character `e`, not the ISO-M from-4-to-end substring `erpreter`.
- **Newline suppression after extrinsic**: `Write $$square(6),!` emits
  `36` but the trailing `!` does not produce a newline after an extrinsic
  function value (observed: `36report: test`).
- **$$ is an external call**: `$$name(args)` resolves to a call into a
  routine file (`do name^file(args)`), yielding E26 File error when no
  such file exists — it is not the ISO-M local extrinsic function.
- **No $ZConvert**: the case-conversion Z-function is absent from this
  build; conversion must go through `$Translate`.
- **No Zmain in interpreted mode**: `zmain` is only required by the
  compiled path (`mumpsc`); interpreted programs start at the top of the
  file. The vendor man page documents this.
- **mumpsc vendor bugs (not blocking, logged)**: `final.script` invoked
  without execute permission; `mumpsc --help` triggers a shell test
  error (`[: too many arguments`, line 66).
