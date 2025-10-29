#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
IOS_DIR="${ROOT_DIR}/iosApp"
PROJECT_NAME="SafeWord"
XCFRAMEWORK_TASK="shared:assembleSharedXCFramework"
CONFIGURATION=${CONFIGURATION:-Debug}
DESTINATION=${DESTINATION:-"generic/platform=iOS Simulator"}

if ! command -v xcodegen >/dev/null 2>&1; then
  echo "xcodegen is required. Install with 'brew install xcodegen'." >&2
  exit 1
fi

pushd "${ROOT_DIR}" >/dev/null
./gradlew ${XCFRAMEWORK_TASK}
popd >/dev/null

"${IOS_DIR}/scripts/bootstrap.sh"

pushd "${IOS_DIR}" >/dev/null
xcodegen -s Generated/${PROJECT_NAME}.xcodeproj.json
xcodebuild -project ${PROJECT_NAME}.xcodeproj \
  -scheme ${PROJECT_NAME} \
  -configuration ${CONFIGURATION} \
  -destination "${DESTINATION}" \
  CODE_SIGNING_ALLOWED=NO
popd >/dev/null
