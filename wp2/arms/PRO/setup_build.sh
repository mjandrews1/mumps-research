#!/usr/bin/env bash
# One-time build tooling setup for arm PRO on m1-build (run as builder).
#
# Step 1 (root, from the container host):
#   lxc exec m1-build -- bash -c 'apt-get update && apt-get install -y openjdk-17-jdk-headless unzip'
#
# Step 2 (builder, from the repo root):
#   bash wp2/arms/PRO/setup_build.sh
#
# Then build the arm:
#   cd wp2/arms/PRO && gradle --no-daemon build
set -euo pipefail

GRADLE_VER=9.9.1

if ! command -v java >/dev/null 2>&1; then
    echo "ERROR: no java on PATH — run step 1 (root apt install) first." >&2
    exit 1
fi
java -version 2>&1 | head -1

if [ ! -x "$HOME/gradle-$GRADLE_VER/bin/gradle" ]; then
    echo "Downloading Gradle $GRADLE_VER ..."
    curl -sL "https://services.gradle.org/distributions/gradle-$GRADLE_VER-bin.zip" -o /tmp/gradle.zip
    unzip -q /tmp/gradle.zip -d "$HOME"
    rm -f /tmp/gradle.zip
fi

if ! grep -q "gradle-$GRADLE_VER/bin" "$HOME/.profile" 2>/dev/null; then
    echo "export PATH=\"\$HOME/gradle-$GRADLE_VER/bin:\$PATH\"" >> "$HOME/.profile"
    echo "Added gradle to ~/.profile (re-login or source it for this shell)."
fi

"$HOME/gradle-$GRADLE_VER/bin/gradle" --version
echo "Setup OK. Build with: cd wp2/arms/PRO && gradle --no-daemon build"
