#!/bin/bash
# oracle_verify.sh: verify BOTH frozen oracles against their committed
# manifests (the integrity prerequisite of the per-unit output-check;
# wp2/PER_UNIT_CHECKS.md section 2). Run from the repository root.
# Exits nonzero on the first failure. This verifies the ORACLE only:
# it is not by itself per-unit output-check evidence (see
# wp2/analysis/README.md, "Backfill tooling").
set -euo pipefail
cd "$(git rev-parse --show-toplevel 2>/dev/null)" || { echo "FATAL: not inside the repo"; exit 1; }

echo "== interpreter oracle (wp1/oracle) =="
(cd wp1/oracle && python3 freeze_oracle.py expected --verify)

echo "== translator oracle (wp2/translator-oracle) =="
(cd wp2/translator-oracle && python3 freeze_translator_oracle.py expected --verify)

echo "ORACLES VERIFIED: frozen manifests intact (expected outputs unchanged)"
