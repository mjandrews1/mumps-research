#!/usr/bin/env python3
"""freeze_oracle.py — record and verify the frozen characterization oracle.

Usage:
  python3 freeze_oracle.py expected          # FREEZE: write manifest.json
  python3 freeze_oracle.py expected --verify # VERIFY: compare a fresh run

Freeze rules (RESEARCH_DESIGN §2.4 step 5):
  - The oracle is frozen ONCE, on the reference build, before any Kotlin
    translation begins.
  - manifest.json records the sha256 of every case output, the interpreter
    version string, and the freeze date.
  - Regenerating with a different build is a protocol deviation: log it,
    never silently replace the manifest.
"""
import hashlib
import json
import os
import platform
import subprocess
import sys
from datetime import datetime, timezone

MANIFEST_NAME = "manifest.json"


def sha256(path: str) -> str:
    h = hashlib.sha256()
    with open(path, "rb") as fh:
        for chunk in iter(lambda: fh.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def build_manifest(expected_dir: str) -> dict:
    entries = {}
    for name in sorted(os.listdir(expected_dir)):
        if name.endswith((".out", ".err")):
            entries[name] = sha256(os.path.join(expected_dir, name))
    return {
        "oracle_version": 1,
        "frozen_at": datetime.now(timezone.utc).isoformat(timespec="seconds"),
        "platform": platform.platform(),
        "case_count": len({n.rsplit(".", 1)[0] for n in entries}),
        "files": entries,
    }


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 2
    expected = sys.argv[1]
    verify = "--verify" in sys.argv[2:]
    manifest_path = os.path.join(expected, MANIFEST_NAME)

    if not verify:
        manifest = build_manifest(expected)
        with open(manifest_path, "w") as fh:
            json.dump(manifest, fh, indent=2, sort_keys=True)
            fh.write("\n")
        print(f"FROZEN: {manifest['case_count']} cases, "
              f"{len(manifest['files'])} output files -> {manifest_path}")
        print("Commit this directory now. Any later change is a protocol deviation.")
        return 0

    with open(manifest_path) as fh:
        frozen = json.load(fh)
    mismatches = [
        n for n, h in frozen["files"].items()
        if n not in os.listdir(expected) or sha256(os.path.join(expected, n)) != h
    ]
    missing = [n for n in frozen["files"] if n not in os.listdir(expected)]
    if mismatches or missing:
        print(f"ORACLE MISMATCH: {len(mismatches)} differing, {len(missing)} missing")
        for n in mismatches:
            print(f"  - {n}")
        return 1
    print(f"ORACLE VERIFIED: all {len(frozen['files'])} outputs match the frozen manifest")
    return 0


if __name__ == "__main__":
    sys.exit(main())
