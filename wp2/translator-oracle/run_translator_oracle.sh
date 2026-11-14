#!/bin/bash
# run_translator_oracle.sh — freeze the TRANSLATOR oracle: mumps2c's output
# for the 15 characterization cases (wp1/oracle/cases/*.mps).
#
# Per case we capture, in a FRESH temp working directory:
#   case.cpp  — the generated C++ (present when translation succeeds)
#   case.m    — the companion .m file mumps2c writes alongside it
#   case.log  — the translator's stdout log, with [translator rc=N] appended
#   case.err  — the translator's stderr (normalized)
#
# NORMALIZATION (volatile content removed so the oracle is reproducible):
#   - log/err: temp paths, case paths, home, ISO timestamps
#   - cpp/m:   the '//#+ <timestamp>' line mumps2c bakes into generated files
#     (line 3; observed 2026-11-14 — the ONLY volatile content there)
#
# KNOWN TRANSLATOR BEHAVIOR (frozen as ground truth): case_13_user_functions
# aborts with error 92 (missing zmain, rc=9) after writing its .m and
# WITHOUT generating a .cpp. A case is "ok" either when translation
# succeeds (rc=0, .cpp+.m present) or when it is a recorded error case
# (rc!=0, .m present, no .cpp expected).
#
# mumps2c writes its output next to the INPUT file, so each case is COPIED
# into the temp dir and translated there by bare name.
#
# Usage:  ./run_translator_oracle.sh <mumps2c-path> <outdir>
# The FIRST full run on the reference build is the FREEZE; commit outdir/
# with freeze_translator_oracle.py's manifest. Every later run must
# reproduce it byte-for-byte (after normalization above).

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
  printf '\n[translator rc=%d]\n' "$rc" >>"$OUT/$name.log"
  have_m=0; have_cpp=0
  [ -f "$W/$name.m" ] && have_m=1
  [ -f "$W/$name.cpp" ] && have_cpp=1
  if [ "$rc" -eq 0 ]; then
    if [ $have_cpp -eq 1 ] && [ $have_m -eq 1 ]; then
      cp "$W/$name.cpp" "$OUT/$name.cpp"
      cp "$W/$name.m" "$OUT/$name.m"
      echo "ok: $name"
    else
      echo "FAIL(rc=0 but missing artifacts): $name (cpp=$have_cpp m=$have_m)"
      fails=$((fails+1))
    fi
  else
    if [ $have_m -eq 1 ]; then
      cp "$W/$name.m" "$OUT/$name.m"
      echo "ok (error case, rc=$rc): $name"
    else
      echo "FAIL(rc=$rc and no artifacts): $name"
      fails=$((fails+1))
    fi
  fi
  # normalize volatile fields
  sed -i -E "s#$W#<WORK>#g; s#$(dirname "$f")#<CASES>#g; s#$HOME#<HOME>#g; s#[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9:.]+##g" \
    "$OUT/$name.log" "$OUT/$name.err"
  sed -i -E "s@^//#+.*@//#+ <TS>@" \
    "$OUT/$name.cpp" "$OUT/$name.m" 2>/dev/null
  rm -rf "$W"
done
echo
echo "cases: $(ls "$CASES"/*.mps | wc -l), failures: $fails"
[ $fails -eq 0 ] || exit 1
