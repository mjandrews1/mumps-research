#!/bin/bash
# build-mumps-fleet.sh — Mumps II Interpreter-Compiler Library: fleet build
# Research: machine-assisted C/C++-to-Kotlin translation of Mumps II
# (mjandrews1/mumps-research, WP1). Targets Ubuntu x86_64 (vendor-supported).
#
# Usage:
#   sudo ./build-mumps-fleet.sh deps     # install apt dependencies
#   ./build-mumps-fleet.sh build          # vendor configure + make + install
#   ./build-mumps-fleet.sh check          # smoke-test the interpreter
#
# MUMPS_SRC: path to the extracted component
#   mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library
# Default below assumes the tarball is extracted at ~/mumps-src.

set -u
MUMPS_SRC="${MUMPS_SRC:-$HOME/mumps-src/mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library}"
TARBALL_URL="https://www.cs.uni.edu/~okane/source/MUMPS-MDH/mumps-14-may-2026.tgz"
TARBALL_SHA256_PLACEHOLDER="(verified on reference build: 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe)"

die() { echo "ERROR: $*" >&2; exit 1; }

case "${1:-}" in
deps)
  [ "$(id -u)" = 0 ] || die "run 'deps' with sudo"
  # vendor dependency list (get-software.script) + build essentials
  apt-get update
  apt-get install -y build-essential pkg-config \
    libgtk-3-dev libgtkmm-3.0-dev \
    libsqlite3-dev libgmp-dev libpcre2-dev flex bison
  bash "$MUMPS_SRC/get-software.script" || echo "WARN: vendor dep script partial-failed; continue if build succeeds"
  ;;

fetch)
  # 2026-11-14 fix: extraction target was one directory too deep.
  # The tar member is mumps/Mumps-Language-Processors/..., which must
  # land directly under the parent of MUMPS_SRC's mumps/ component.
  mkdir -p "$(dirname "$MUMPS_SRC")/../.."
  curl -L "$TARBALL_URL" -o /tmp/mumps.tgz
  # Recorded on the reference build (2026-11-14):
  #   621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe  mumps-14-may-2026.tgz
  sha256sum /tmp/mumps.tgz
  tar xzf /tmp/mumps.tgz -C "$(dirname "$MUMPS_SRC")/../.." \
    mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library
  ;;

build)
  [ -f "$MUMPS_SRC/build-mumps.script" ] || { echo "source missing; run: $0 fetch"; exit 1; }
  cd "$MUMPS_SRC" || die "cannot cd $MUMPS_SRC"
  # Vendor script: configure (sqlite3 backend) + make -B -j8 + install .deb
  # Do NOT run as root (vendor script refuses).
  bash build-mumps.script || die "vendor build failed"
  echo
  echo "If final.script says to install the .deb:"
  echo "  sudo dpkg --remove mumps-sqlite3 2>/dev/null; sudo dpkg --install $MUMPS_SRC/mumps-sqlite3-amd64.deb && sudo apt install -f"
  ;;

check)
  # Smoke test: interpreter + translator presence
  command -v mumps >/dev/null || die "mumps not on PATH — install the .deb (see 'build' output)"
  command -v mumps2c >/dev/null || die "mumps2c not on PATH — install the .deb (see 'build' output)"
  cat >/tmp/hello_research.mps <<'MPS'
	; smoke test — mjandrews1/mumps-research WP1
	Set t="Mumps II baseline OK"
	Write t,!
	Write 6*7,!
	Quit
MPS
  # interpreter needs a sqlite db in the working directory
  printf 'y\n' | (cd /tmp && mumps-sql-db-create) >/dev/null 2>&1 \
    || echo "WARN: mumps-sql-db-create failed; smoke test may fail"
  echo "--- interpreter (mumps) ---"
  (cd /tmp && mumps /tmp/hello_research.mps) || die "interpreter smoke test failed"
  echo "--- translator (mumps2c) ---"
  mumps2c /tmp/hello_research.mps >/dev/null || die "translator smoke test failed"
  echo "SMOKE TEST PASSED"
  ;;

*)
  echo "usage: $0 deps | fetch | build | check"
  echo "env:   MUMPS_SRC=$MUMPS_SRC"
  exit 2
  ;;
esac
