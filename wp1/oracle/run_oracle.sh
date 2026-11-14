#!/bin/bash
# run_oracle.sh — run all characterization cases against the Mumps II interpreter
#
# Usage:
#   ./run_oracle.sh <mumps-path> <outdir>        # capture outputs
#
# REVISED (pre-freeze, 2026-11-14): the behavioral oracle target is the
# INTERPRETER (/usr/bin/mumps, sqlite3 build), not the mumps2c translator.
# mumps2c only translates .mps to C++ and its output is a translator log,
# not program behavior. Each run uses a fresh temp working directory with
# a newly created mumps.sqlite so database state cannot leak between
# cases or runs.
#
# The FIRST full run on the reference build produces outdir/ and is the
# ORACLE FREEZE (per RESEARCH_DESIGN §2.4 step 5). Commit the result with
# freeze_oracle.py's manifest. Every later run must reproduce it byte-
# for-byte (after normalization of volatile fields below).

set -u
BIN="${1:?usage: run_oracle.sh <mumps-path> <outdir>}"
OUT="${2:?usage: run_oracle.sh <mumps-path> <outdir>}"
HERE="$(cd "$(dirname "$0")" && pwd)"

rm -rf "$OUT"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

# fresh sqlite database for the interpreter (it refuses to run without one)
printf 'y\n' | (cd "$WORK" && mumps-sql-db-create) >/dev/null 2>&1 \
  || { echo "FATAL: mumps-sql-db-create failed"; exit 2; }

mkdir -p "$OUT"
fails=0
for f in "$HERE"/cases/*.mps; do
  name="$(basename "$f" .mps)"
  (cd "$WORK" && "$BIN" "$f") >"$OUT/$name.out" 2>"$OUT/$name.err"
  rc=$?
  # normalize volatile fields (temp paths, home, case paths, timestamps)
  sed -i -E "s#$WORK#<WORK>#g; s#$(dirname "$f")#<CASES>#g; s#$HOME#<HOME>#g; s#[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9:.]+##g" \
    "$OUT/$name.out" "$OUT/$name.err"
  if [ $rc -ne 0 ]; then echo "FAIL(rc=$rc): $name"; fails=$((fails+1));
  else echo "ok: $name"; fi
done
echo
echo "cases: $(ls "$HERE"/cases/*.mps | wc -l), failures: $fails"
[ $fails -eq 0 ] || exit 1
