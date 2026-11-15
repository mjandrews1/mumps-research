#!/bin/bash
# m1_continue.sh — WP1 state-aware driver for m1-build (the `container` host's
# LXD container, user builder). Checks work completed to date and proceeds
# from that point forward. Idempotent: safe to re-run at any time; frozen
# artifacts are only VERIFIED, never regenerated or overwritten.
#
# Usage (inside m1-build, from ~/mumps-research):
#   ./wp1/m2/m1_continue.sh            # check + proceed to next pending step
#   ./wp1/m2/m1_continue.sh --check    # report status only, do nothing
#
# Translator capture: prefers the FROZEN translator oracle at
# wp2/translator-oracle/expected/ (frozen 2026-11-14, 59 files, case_13 is
# a recorded error case with no .cpp). Only if that is absent does it fall
# back to creating a wp1/m2/capture/ via run_m2.sh + freeze_m2.py.
#
# Exit codes: 0 = all WP1 steps complete and verified; 1 = a verification
# FAILED (do not proceed — investigate, log deviations); 2 = a step was
# executed (or usage error) — re-run to confirm it now passes.
#
# Protocol notes:
#   - The oracle freezes are one-time events. If either --verify fails,
#     that is a protocol deviation: STOP, do not re-freeze.
#   - Commits of new artifacts are left to the researcher (this script only
#     stages hints at the end of its run).

set -u
CHECK_ONLY=0
[ "${1:-}" = "--check" ] && CHECK_ONLY=1
cd "$(dirname "$0")/../.." || exit 2   # repo root
REPO_TOP="$(pwd)"

step() { printf '\n=== %s ===\n' "$*"; }
ok()   { echo "  ok: $*"; }
info() { echo "  .. $*"; }
fail() { echo "  FAIL: $*"; exit 1; }
run()  { # execute unless --check; returns rc
  if [ $CHECK_ONLY -eq 1 ]; then info "skipped (--check): $*"; return 0; else "$@"; fi
}

FAILED=0
verify_or_fail() { "$@" || { FAILED=1; } ; }

step "0. Repository state"
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  fail "not a git checkout: $REPO_TOP"
fi
BRANCH="$(git rev-parse --abbrev-ref HEAD)"
info "branch: $BRANCH, HEAD: $(git rev-parse --short HEAD)"
if [ -n "$(git status --porcelain)" ]; then
  info "working tree dirty — skipping pull (commit or stash first)"
else
  git pull --ff-only >/dev/null 2>&1 || info "git pull failed (offline?) — continuing with local state"
fi

step "1. Mumps II build present"
MUMPS_BIN="$(command -v mumps || true)"
MUMPS2C_BIN="$(command -v mumps2c || true)"
[ -n "$MUMPS_BIN" ]  || fail "interpreter 'mumps' not installed (run wp1/build-mumps-fleet.sh deps build)"
[ -n "$MUMPS2C_BIN" ] || fail "translator 'mumps2c' not installed (run wp1/build-mumps-fleet.sh deps build)"
ok "interpreter: $MUMPS_BIN"
ok "translator:  $MUMPS2C_BIN"

step "2. Behavioral oracle (frozen at 090a82f)"
if [ -f wp1/oracle/expected/manifest.json ]; then
  verify_or_fail bash wp1/oracle/run_oracle.sh "$MUMPS_BIN" /tmp/oracle-verify-run
  [ $FAILED -eq 0 ] || fail "oracle re-run incomplete — see output above"
  verify_or_fail python3 wp1/oracle/freeze_oracle.py /tmp/oracle-verify-run --verify
  [ $FAILED -eq 0 ] || fail "ORACLE MISMATCH vs frozen manifest — protocol deviation: stop, investigate, do NOT re-freeze"
  rm -rf /tmp/oracle-verify-run
  ok "frozen oracle verified against a fresh interpreter run"
else
  fail "wp1/oracle/expected/manifest.json missing — oracle not frozen; freeze on the reference build first (see wp1/oracle/README.md)"
fi

CAPTURE_DIR=""
if [ -f wp2/translator-oracle/expected/manifest.json ]; then
  step "3. Translator oracle (wp2/translator-oracle/expected/)"
  verify_or_fail bash wp2/translator-oracle/run_translator_oracle.sh "$MUMPS2C_BIN" /tmp/tr-verify-run
  [ $FAILED -eq 0 ] || fail "translator-oracle re-run incomplete — see output above"
  verify_or_fail python3 wp2/translator-oracle/freeze_translator_oracle.py /tmp/tr-verify-run --verify
  [ $FAILED -eq 0 ] || fail "TRANSLATOR ORACLE MISMATCH vs frozen manifest — protocol deviation: stop, investigate, do NOT re-freeze"
  rm -rf /tmp/tr-verify-run
  ok "frozen translator oracle verified against a fresh mumps2c run"
  CAPTURE_DIR="wp2/translator-oracle/expected"
else
  step "3. M2 translator-output capture (wp1/m2/capture/)"
  if [ ! -f wp1/m2/capture/manifest.json ]; then
    info "no frozen translator oracle and no M2 capture — creating one now (one-time FREEZE)"
    run bash wp1/m2/run_m2.sh "$MUMPS2C_BIN" wp1/m2/capture \
      || fail "run_m2.sh failed — translator failures are characterization data; record them before retrying"
    run python3 wp1/m2/freeze_m2.py wp1/m2/capture \
      || fail "freeze_m2.py could not write the capture manifest"
    info "M2 capture frozen — commit wp1/m2/capture/ now, then re-run this script"
    exit 2
  fi
  verify_or_fail bash wp1/m2/run_m2.sh "$MUMPS2C_BIN" /tmp/m2-verify-run
  [ $FAILED -eq 0 ] || fail "m2 re-capture incomplete — see output above"
  verify_or_fail python3 wp1/m2/freeze_m2.py /tmp/m2-verify-run --verify
  [ $FAILED -eq 0 ] || fail "M2 CAPTURE MISMATCH vs frozen manifest — protocol deviation: stop, investigate, do NOT re-freeze"
  rm -rf /tmp/m2-verify-run
  ok "frozen M2 capture verified against a fresh translator run"
  CAPTURE_DIR="wp1/m2/capture"
fi

step "4. M2 characterization (wp1/m2/analysis/)"
if [ ! -f wp1/m2/analysis/m2_characterization.json ]; then
  info "no characterization yet — generating it now (from $CAPTURE_DIR)"
  run python3 wp1/m2/characterize_m2.py "$CAPTURE_DIR" wp1/m2/analysis wp1/oracle/expected \
    || fail "characterize_m2.py failed"
  info "characterization written — commit wp1/m2/analysis/ now, then re-run this script"
  exit 2
fi
ok "m2_characterization.json + m2_summary.md present"
if [ "$CAPTURE_DIR/manifest.json" -nt wp1/m2/analysis/m2_characterization.json ]; then
  info "capture is newer than the analysis — consider re-running characterize_m2.py"
fi

step "Summary"
head -60 wp1/m2/analysis/m2_summary.md 2>/dev/null
echo
if git status --porcelain | grep -q 'wp1/m2/\(analysis\)'; then
  info "uncommitted M2 artifacts:"
  git status --porcelain | grep 'wp1/m2/' | sed 's/^/    /'
  info "commit with: git add wp1/m2/analysis && git commit -m 'wp1/m2: translator C++ characterization' && git push"
else
  ok "all WP1 steps complete and verified — next: WP2 pilot seeding from the M2 API surface"
fi
echo "done: all checks passed"
