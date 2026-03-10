#!/usr/bin/env bash
set -uo pipefail

echo "[hook] running focused checks"
if ./gradlew spotlessApply && ./gradlew test; then
  exit 0
else
  echo "[hook] spotlessApply or tests failed; blocking git commit/push (exit 2)"
  exit 2
fi
