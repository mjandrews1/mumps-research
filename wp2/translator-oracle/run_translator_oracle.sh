#!/bin/bash
# run_translator_oracle.sh — freeze the TRANSLATOR oracle: mumps2c's output
# for the 15 characterization cases (wp1/oracle/cases/*.mps).
#
# Per case we capture, in a FRESH temp working directory:
#   case.cpp  — the generated C++ (ground truth for translator-side units)
#   case.m    — the companion .m file mumps2c writes alongside it
#   case.log  — the translator's stdout log (normalized)
#   case.err  — the translator's stderr (normalized)
#
# mumps2c writes its output next to the INPUT file, so each case is COPIED
# into the temp dir and translated there by bare name (observed 2026-11-14).
#
# Usage:  ./run_translator_oracle.sh <mumps2c-path> <outdir>
# The FIRST full run on the reference build is the FREEZE; commit outdir/
# with freeze_oracle.py's manifest. Every later run must reproduce it
# byte-for-byte (after normalization of volatile fields below).

set -u
BIN="${1:?usage: run_translator_oracle.sh <mumps2c-path> <outdir>}"
OUT="${2:?usage: run_translator_oracle.sh <mumps2c-path> <outdir>}"
HERE="$(cd "$(dirname "$0")" && pwd)"
CASES="${CASES:-$(cd "$HERE/../../wp1/oracle/cases" && pwd)}"

rm -rf "$OUT"
mkdir -p "$OUT"
fails=0
for f in "$CASES"/*.mps; do
  name="$(basename "$f" .mps)"
  W="$(mktemp -d)"
  cp "$f" "$W/"
  (cd "$W" && "$BIN" "$name.mps") >"$OUT/$name.log" 2>"$OUT/$name.err"
  rc=$?
  for ext in cpp m; do
    if [ -f "$W/$name.$ext" ]; then
      cp "$W/$name.$ext" "$OUT/$name.$ext"
    else
      echo "MISSING: $name.$ext"
      fails=$((fails+1))
    fi
  done
  # normalize volatile fields (temp paths, case paths, home, timestamps)
  sed -i -E "s#$W#<WORK>#g; s#$(dirname "$f")#<CASES>#g; s#$HOME#<HOME>#g; s#[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9:.]+##g" \
    "$OUT/$name.log" "$OUT/$name.err"
  rm -rf "$W"
  if [ $rc -ne 0 ]; then echo "FAIL(rc=$rc): $name"; fails=$((fails+1));
  else echo "ok: $name"; fi
done
echo
echo "cases: $(ls "$CASES"/*.mps | wc -l), failures: $fails"
[ $fails -eq 0 ] || exit 1
