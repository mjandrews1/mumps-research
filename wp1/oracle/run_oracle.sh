#!/bin/bash
# run_oracle.sh — run all characterization cases against a Mumps II interpreter
#
# Usage:
#   ./run_oracle.sh <mumps2c-path> <outdir>          # capture outputs
#   ./run_oracle.sh <mumps2c-path> expected           # first freeze run
#
# The FIRST full run on the reference build produces outdir/ and is the
# ORACLE FREEZE (per RESEARCH_DESIGN §2.4 step 5). Commit the result with
# freeze_oracle.py's manifest. Every later run must reproduce it byte-for-byte
# (after normalization of the volatile fields this script already strips).

set -u
BIN="${1:?usage: run_oracle.sh <mumps2c-path> <outdir>}"
OUT="${2:?usage: run_oracle.sh <mumps2c-path> <outdir>}"
HERE="$(cd "$(dirname "$0")" && pwd)"

mkdir -p "$OUT"
fails=0
for f in "$HERE"/cases/*.mps; do
  name="$(basename "$f" .mps)"
  # strip timing/job-control lines the interpreter may emit
  "$BIN" "$f" >"$OUT/$name.out" 2>"$OUT/$name.err"
  rc=$?
  # normalize volatile prefixes (timestamps, paths) if present
  sed -i -E 's/[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9:.]+//g; s#/tmp/[a-zA-Z0-9_.-]+#<TMP>#g; s#'$HOME'#<HOME>#g' "$OUT/$name.out" "$OUT/$name.err"
  if [ $rc -ne 0 ]; then echo "FAIL(rc=$rc): $name"; fails=$((fails+1));
  else echo "ok: $name"; fi
done
echo
echo "cases: $(ls "$HERE"/cases/*.mps | wc -l), failures: $fails"
[ $fails -eq 0 ] || exit 1
