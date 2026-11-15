#!/usr/bin/env python3
"""freeze_m2.py — record and verify the frozen M2 translator-output capture.

Usage:
  python3 freeze_m2.py capture          # FREEZE: write manifest.json
  python3 freeze_m2.py capture --verify # VERIFY: compare a fresh capture

Mirrors wp1/oracle/freeze_oracle.py. Hashes every captured file (translator
logs, stderr, and all generated C++ files, walked recursively) under the
capture directory, excluding manifest.json itself.

Freeze rules:
  - The M2 capture is frozen ONCE, on the reference build (m1-build,
    same provenance as the oracle freeze), before any Kotlin translation
    or cookbook/rule-base seeding begins.
  - A differing re-capture is a protocol deviation: log it, never silently
    replace the manifest.
"""
import hashlib
import json
import os
import platform
import sys
from datetime import datetime, timezone

MANIFEST_NAME = "manifest.json"


def sha256(path: str) -> str:
    h = hashlib.sha256()
    with open(path, "rb") as fh:
        for chunk in iter(lambda: fh.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def walk_files(root: str):
    for dirpath, _dirnames, filenames in os.walk(root):
        for fn in sorted(filenames):
            full = os.path.join(dirpath, fn)
            rel = os.path.relpath(full, root)
            if rel == MANIFEST_NAME:
                continue
            yield rel, full


def build_manifest(capture_dir: str) -> dict:
    entries = {rel: sha256(full) for rel, full in walk_files(capture_dir)}
    return {
        "m2_version": 1,
        "frozen_at": datetime.now(timezone.utc).isoformat(timespec="seconds"),
        "platform": platform.platform(),
        "case_count": len({rel.split(os.sep)[0].rsplit(".", 1)[0]
                          for rel, _ in walk_files(capture_dir)
                          if os.sep in rel}),
        "files": entries,
    }


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 2
    capture = sys.argv[1]
    verify = "--verify" in sys.argv[2:]
    manifest_path = os.path.join(capture, MANIFEST_NAME)

    if not verify:
        manifest = build_manifest(capture)
        with open(manifest_path, "w") as fh:
            json.dump(manifest, fh, indent=2, sort_keys=True)
            fh.write("\n")
        print(f"FROZEN: {manifest['case_count']} cases, "
              f"{len(manifest['files'])} captured files -> {manifest_path}")
        print("Commit this directory now. Any later change is a protocol deviation.")
        return 0

    with open(manifest_path) as fh:
        frozen = json.load(fh)
    present = {rel: sha256(full) for rel, full in walk_files(capture)}
    mismatches = [n for n, h in frozen["files"].items()
                  if present.get(n) != h]
    extra = [n for n in present if n not in frozen["files"]]
    if mismatches or extra:
        print(f"M2 CAPTURE MISMATCH: {len(mismatches)} differing/missing, "
              f"{len(extra)} unexpected new files")
        for n in mismatches[:20]:
            print(f"  - {n}")
        for n in extra[:20]:
            print(f"  + {n}")
        return 1
    print(f"M2 CAPTURE VERIFIED: all {len(frozen['files'])} files match the "
          f"frozen manifest")
    return 0


if __name__ == "__main__":
    sys.exit(main())
