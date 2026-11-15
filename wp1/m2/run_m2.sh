#!/bin/bash
# run_m2.sh — WP1/M2: capture the mumps2c translator's C++ output for every
# frozen-oracle case.
#
# Usage:
#   ./run_m2.sh <mumps2c-path> <outdir>
#
# Runs the TRANSLATOR (mumps2c) — not the interpreter — on each of the 15
# oracle cases (wp1/oracle/cases/*.mps, the exact programs behind the frozen
# behavioral oracle at wp1/oracle/expected/). For each case it captures:
#
#   <outdir>/<name>.log        translator stdout
#   <outdir>/<name>.err        translator stderr
#   <outdir>/<name>/<file>     every file the translator creates in the
#                              working directory (the generated C++ etc.,
#                              whatever its name — collected by snapshot
#                              diff, so naming changes are tolerated)
#
# Each case runs in a fresh temp working directory so translator outputs
# cannot leak between cases. The FIRST full run on the reference build is
# the M2 CAPTURE; freeze it immediately with freeze_m2.py. Later runs must
# reproduce it byte-for-byte after normalization.
#
# NOTE (protocol): the translator output is SOURCE CHARACTERIZATION DATA
# only. The behavioral authority remains the frozen interpreter oracle at
# wp1/oracle/expected/ (manifest 090a82f freeze). Nothing here replaces or
# amends the oracle.

set -u
BIN="${1:?usage: run_m2.sh <mumps2c-path> <outdir>}"
OUT="${2:?usage: run_m2.sh <mumps2c-path> <outdir>}"
HERE="$(cd "$(dirname "$0")" && pwd)"

rm -rf "$OUT"
mkdir -p "$OUT"
fails=0
for f in "$HERE"/../oracle/cases/*.mps; do
  name="$(basename "$f" .mps)"
  WORK="$(mktemp -d)"
  # snapshot of pre-existing files
  (cd "$WORK" && find . -type f | sort) > "$WORK/.snap_before"
  (cd "$WORK" && "$BIN" "$f") > "$OUT/$name.log" 2> "$OUT/$name.err"
  rc=$?
  # normalize volatile fields in logs (temp paths, case paths, home, dates)
  sed -i -E "s#$WORK#<WORK>#g; s#$(dirname "$f")#<CASES>#g; s#$HOME#<HOME>#g; s#[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9:.]+##g" \
    "$OUT/$name.log" "$OUT/$name.err"
  # collect every file the translator created
  (cd "$WORK" && find . -type f ! -name .snap_before | sort) > "$WORK/.snap_after"
  n_new=$(comm -13 "$WORK/.snap_before" "$WORK/.snap_after" | wc -l)
  mkdir -p "$OUT/$name"
  while IFS= read -r rel; do
    [ -n "$rel" ] && cp "$WORK/$rel" "$OUT/$name/${rel#./}"
  done < <(comm -13 "$WORK/.snap_before" "$WORK/.snap_after")
  rm -rf "$WORK"
  if [ $rc -ne 0 ]; then
    echo "FAIL(rc=$rc): $name ($n_new files generated)"
    fails=$((fails+1))
  else
    echo "ok: $name ($n_new files generated)"
  fi
done
echo
echo "cases: $(ls "$HERE"/../oracle/cases/*.mps | wc -l), failures: $fails"
[ $fails -eq 0 ] || exit 1
