#!/bin/bash
# one_off_cleanup.sh — ONE-TIME housekeeping on m1-build (run as ROOT, once).
#
# Removes artifacts left by the accidental root-account sessions of
# 2026-11-15 (bare `lxc exec m1-build -- bash -lc ...` ran as root against
# a stray /root/mumps-research clone). The canonical clone is user
# `builder`'s /home/builder/mumps-research — this script never touches it.
#
# What it removes:
#   1. /root/mumps-research        stray non-canonical root clone
#   2. /tmp/oracle-verify-run     leftover from the pre-fix verification
#      /tmp/tr-verify-run         run (the driver now cleans these itself)
#      /tmp/m2-verify-run
#
# What it checks (read-only, no changes):
#   - builder's clone exists, is a git checkout, current branch/HEAD
#
# Usage (from the `container` host, after builder has pulled this commit):
#   lxc exec m1-build -- bash /home/builder/mumps-research/wp1/m2/one_off_cleanup.sh
#
# Idempotent: safe to re-run; prints what it finds/does. Exits non-zero only
# if builder's canonical clone is missing (which it must NOT touch).

set -u
removed=0

rm_quiet() {
  if [ -e "$1" ]; then
    rm -rf "$1"
    echo "removed: $1"
    removed=$((removed+1))
  else
    echo "absent (already clean): $1"
  fi
}

echo "=== one-off cleanup (run as $(id -un)) ==="

echo
echo "1. stray root clone:"
rm_quiet /root/mumps-research

echo
echo "2. leftover verification dirs (/tmp):"
rm_quiet /tmp/oracle-verify-run
rm_quiet /tmp/tr-verify-run
rm_quiet /tmp/m2-verify-run

echo
echo "3. canonical clone check (builder's ~/mumps-research) — READ ONLY:"
CANON=/home/builder/mumps-research
if [ -d "$CANON/.git" ]; then
  echo "  ok: $CANON exists"
  echo "  branch/HEAD: $(git -C "$CANON" rev-parse --abbrev-ref HEAD) / $(git -C "$CANON" rev-parse --short HEAD)"
  echo "  owned by: $(stat -c '%U:%G' "$CANON")"
  if [ "$(stat -c '%U' "$CANON")" != "builder" ]; then
    echo "  NOTE: not owned by builder — fix with:"
    echo "    chown -R builder:builder $CANON"
  fi
else
  echo "  FAIL: $CANON missing or not a git checkout — investigate before proceeding"
  exit 1
fi

echo
echo "cleanup complete ($removed item(s) removed)."
echo "Going forward use: lxc exec m1-build -- su - builder -c 'cd ~/mumps-research && ...'"
