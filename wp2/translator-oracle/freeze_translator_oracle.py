#!/usr/bin/env python3
"""freeze_translator_oracle.py — record and verify the frozen translator oracle.

Usage:
  python3 freeze_translator_oracle.py expected          # FREEZE
  python3 freeze_translator_oracle.py expected --verify # VERIFY

Unlike wp1's freeze_oracle.py (which manifests .out/.err only), this
manifests EVERY file in the capture directory: the generated .cpp, the
companion .m, the normalized .log and .err. Same freeze discipline:
frozen ONCE on the reference build; any later change is a protocol
deviation to log, never a silent replacement.
"""
import hashlib
import json
import os
import sys
from datetime import datetime, timezone

MANIFEST_NAME = "manifest.json"


def sha256(path: str) -> str:
    h = hashlib.sha256()
    with open(path, "rb") as fh:
        for chunk in iter(lambda: fh.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 2
    expected = sys.argv[1]
    verify = "--verify" in sys.argv[2:]
    manifest_path = os.path.join(expected, MANIFEST_NAME)

    if not verify:
        entries = {
            name: sha256(os.path.join(expected, name))
            for name in sorted(os.listdir(expected))
            if name != MANIFEST_NAME
        }
        manifest = {
            "oracle_version": 1,
            "kind": "translator",
            "frozen_at": datetime.now(timezone.utc).isoformat(timespec="seconds"),
            "file_count": len(entries),
            "files": entries,
        }
        with open(manifest_path, "w") as fh:
            json.dump(manifest, fh, indent=2, sort_keys=True)
            fh.write("\n")
        print(f"TRANSLATOR ORACLE FROZEN: {len(entries)} files -> {manifest_path}")
        print("Commit this directory now. Any later change is a protocol deviation.")
        return 0

    with open(manifest_path) as fh:
        frozen = json.load(fh)
    on_disk = {n for n in os.listdir(expected) if n != MANIFEST_NAME}
    problems = []
    for name, digest in frozen["files"].items():
        p = os.path.join(expected, name)
        if name not in on_disk:
            problems.append(f"missing: {name}")
        elif sha256(p) != digest:
            problems.append(f"hash mismatch: {name}")
    for name in sorted(on_disk - set(frozen["files"])):
        problems.append(f"unexpected extra file: {name}")
    if problems:
        print(f"TRANSLATOR ORACLE MISMATCH: {len(problems)} problems")
        for p_ in problems:
            print(f"  - {p_}")
        return 1
    print(f"TRANSLATOR ORACLE VERIFIED: all {len(frozen['files'])} files match the frozen manifest")
    return 0


if __name__ == "__main__":
    sys.exit(main())
