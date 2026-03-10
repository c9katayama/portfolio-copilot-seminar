#!/usr/bin/env bash
set -euo pipefail

echo "[hook] run format for changed portfolio-copilot modules"
./gradlew spotlessApply
