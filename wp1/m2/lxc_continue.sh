#!/bin/bash
# lxc_continue.sh — run wp1/m2/m1_continue.sh INSIDE the m1-build LXD
# container, from the container HOST (no need to open a shell in the guest).
#
# Usage (on the `container` host, as a user with LXD admin rights):
#   ./wp1/m2/lxc_continue.sh [args...]     # e.g. --check
#   M1_CONTAINER=m1-build ./wp1/m2/lxc_continue.sh --check
#
# Passes exit codes through unchanged (0 all verified, 1 verification
# failure, 2 a step ran — re-run). The in-container driver runs as user
# `builder` with a login environment (HOME=/home/builder).

set -u
HERE="$(cd "$(dirname "$0")" && pwd)"
CONTAINER="${M1_CONTAINER:-m1-build}"

lxc info "$CONTAINER" >/dev/null 2>&1 \
  || { echo "FAIL: LXD container '$CONTAINER' not found or lxc not usable"; exit 2; }

UID_B="$(lxc exec "$CONTAINER" -- id -u builder 2>/dev/null)" \
  || { echo "FAIL: user 'builder' not found in $CONTAINER"; exit 2; }
GID_B="$(lxc exec "$CONTAINER" -- id -g builder 2>/dev/null)" || GID_B="$UID_B"

lxc exec "$CONTAINER" \
  --user "$UID_B" --group "$GID_B" \
  --env HOME=/home/builder --env USER=builder \
  -- bash -lc '
    cd "$HOME/mumps-research" || exit 2
    if [ -d .git ]; then git pull --ff-only >/dev/null 2>&1 || true; fi
    exec ./wp1/m1_continue.sh "$@"
  ' m1-continue "$@"
