# iOS Build Quickstart

This project ships an iOS Client that consumes the shared Kotlin Multiplatform framework. Follow these steps on macOS to produce a build that you can install to a simulator or device.

## Prerequisites

- macOS with Xcode 15 or newer and the iOS 17 SDK installed
- Xcode command-line tools (`xcode-select --install`)
- Homebrew (for toolchain installs)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen) (`brew install xcodegen`)
- JDK 17 (Temurin works well) and the standard Gradle toolchain

Optional but helpful:

- `ruby` with `bundler` if you prefer to manage Apple build dependencies via gems
- A configured signing identity/provisioning profile if you intend to run on a physical device (the default script builds unsigned simulator binaries)

## One-Time Project Bootstrap

1. From the repo root, build the shared XCFramework:
   ```bash
   ./gradlew shared:assembleSharedXCFramework
   ```
2. Generate the Xcode project wrapper:
   ```bash
   iosApp/scripts/bootstrap.sh
   xcodegen -s iosApp/Generated/SafeWord.xcodeproj.json
   ```
3. Open `iosApp/SafeWord.xcodeproj` in Xcode and allow it to download packages the first time.

## Automated Build Script

A convenience script ties the steps together:
```bash
IOS_DESTINATION="generic/platform=iOS Simulator" \
CONFIGURATION=Debug \
iosApp/scripts/build.sh
```
The script will:
- invoke `./gradlew shared:assembleSharedXCFramework`
- regenerate the Xcode project definition
- call `xcodebuild` to compile the SafeWord app for the chosen configuration/destination

Set `CONFIGURATION=Release` and a device destination (e.g. `generic/platform=iOS`) when you need a release build. Add `CODE_SIGNING_ALLOWED=YES` and the appropriate signing flags if you want to archive or install on hardware.

## Installing to a Simulator

After running the build script, the app binary lives under `iosApp/build/Build/Products/<CONFIGURATION>-iphonesimulator/SafeWord.app`. Install it via:
```bash
xcrun simctl install booted iosApp/build/Build/Products/Debug-iphonesimulator/SafeWord.app
```

## Notes

- Any time shared Kotlin code changes, rebuild the XCFramework before running Xcode.
- The generated project is intentionally lightweight; customize `iosApp/Generated/SafeWord.xcodeproj.json` and rerun `xcodegen` as needed.
- For CI, run the same `gradlew` task followed by `xcodebuild` on a macOS runner.
