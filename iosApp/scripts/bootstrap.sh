#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
IOS_DIR="${ROOT_DIR}/iosApp"
PROJECT_NAME="SafeWord"
XCFRAMEWORK_PATH="${ROOT_DIR}/shared/build/XCFrameworks/release/Shared.xcframework"

if [[ ! -d "${XCFRAMEWORK_PATH}" ]]; then
  echo "Shared.xcframework not found at ${XCFRAMEWORK_PATH}" >&2
  echo "Run ./gradlew shared:assembleSharedXCFramework first." >&2
  exit 1
fi

mkdir -p "${IOS_DIR}/Generated"

cat > "${IOS_DIR}/Generated/${PROJECT_NAME}.xcodeproj.json" <<'JSON'
{
  "name": "SafeWord",
  "options": {
    "bundleIdPrefix": "com.safeword"
  },
  "targets": [
    {
      "name": "SafeWord",
      "type": "application",
      "platform": "iOS",
      "sources": ["Sources/SafeWordApp", "Sources/SafeWordUI"],
      "settings": {
        "PRODUCT_BUNDLE_IDENTIFIER": "com.safeword.app",
        "IPHONEOS_DEPLOYMENT_TARGET": "16.0",
        "SWIFT_VERSION": "5.9"
      },
      "dependencies": [
        { "framework": "../shared/build/XCFrameworks/release/Shared.xcframework" }
      ]
    }
  ]
}
JSON

echo "Generated ${IOS_DIR}/Generated/${PROJECT_NAME}.xcodeproj.json"
echo "Run: xcodegen -s iosApp/Generated/${PROJECT_NAME}.xcodeproj.json"
