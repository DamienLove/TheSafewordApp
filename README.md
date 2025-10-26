,# SafeWord (Android & iOS)

![Android CI](https://github.com/DamienLove/TheSafewordApp/actions/workflows/android.yml/badge.svg)

Native safety application delivering on-device "safe word" voice/SMS detection, rapid escalation workflows, and optional peer-to-peer bridging between Android and iOS builds.

## Download Android APK

### Latest Build

You can download the latest Android APK in two ways:

1. **From GitHub Actions Artifacts** (requires GitHub account):
   - Go to the [Actions tab](https://github.com/DamienLove/TheSafewordApp/actions)
   - Click on the latest successful "Android CI" workflow run
   - Scroll down to the "Artifacts" section
   - Download the `app-debug` artifact (contains the APK)

2. **From Releases** (no account required):
   - Visit the [Releases page](https://github.com/DamienLove/TheSafewordApp/releases)
   - Download the latest `app-debug.apk` file from the most recent release

### Installing the APK

1. Download the APK file to your Android device
2. Open the APK file to install (you may need to enable "Install from Unknown Sources" in your device settings)
3. Follow the on-screen prompts to complete installation

**Note**: The APK is built automatically on every push to the master branch and on pull requests.

## Project layout
- `shared/` — Kotlin Multiplatform shared domain logic, storage abstractions, and the cross-OS bridge contracts.
- `androidApp/` — Android app module (Min SDK 26, Target 35) implementing background recognition, SMS detection, emergency handling, and UI.
- `iosApp/` — SwiftUI app consuming the shared logic via Kotlin/Native framework, providing parity UI and peer bridge hooks that comply with iOS background limitations.
- `docs/` — References, including the v1 technical specification.

## Getting started
1. **Tooling**  
   - Android Studio Giraffe+, Kotlin 1.9.x CLI, JDK 17, and Android SDK platforms 26–35.
   - Xcode 15+, Swift 5.9 toolchain, CocoaPods/XcodeGen (optional) for the iOS bootstrap script.
   - Gradle 8.7+ (run `gradle wrapper` once to add `gradlew` if desired).

2. **Shared bootstrap**
   ```bash
   # from repo root
   gradle wrapper                  # optional, creates ./gradlew
   ./gradlew clean shared:assemble 
   ```

### Android build & run
- Import the root project into Android Studio and select the `androidApp` configuration.
- First launch will prompt for microphone, SMS, contacts, and location permissions; accept to enable automation.
- Foreground services:
  - `VoiceRecognitionService` keeps hotword detection running.
  - `SafeWordPeerService` maintains the UDP multicast bridge; verify via the persistent notification.
- CLI build:
  ```bash
  ./gradlew androidApp:assembleDebug
  adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
  ```
- Recommended manual checks:
  - Toggle listening, trip the siren/test mode, and verify SMS intents (emulators logcat).
  - Confirm contacts CRUD via the built-in list.
  - Observe multicast packets on port `45230` (`adb shell tcpdump udp port 45230`).

### iOS build & run
1. Generate the Kotlin/Native framework:
   ```bash
   ./gradlew shared:syncFramework
   ```
   The XCFramework is emitted under `shared/build/XCFrameworks/release/Shared.xcframework`.

2. Create the Xcode project (one time):
   ```bash
   bash iosApp/scripts/bootstrap.sh
   xcodegen -s iosApp/Generated/SafeWord.xcodeproj.json
   ```

3. Open the generated project, add `Shared.xcframework` to the target, and run on a device/simulator.

4. On first launch, grant notification permissions (used for emergency prompts). iOS cannot silently place calls/SMS; the dispatcher posts rich notifications instead.ios
