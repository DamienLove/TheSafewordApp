# SafeWord (Android & iOS)

Native safety application delivering on-device "safe word" voice/SMS detection, rapid escalation workflows, and optional peer-to-peer bridging between Android and iOS builds.

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
4. On first launch, grant notification permissions (used for emergency prompts). iOS cannot silently place calls/SMS; the dispatcher posts rich notifications instead.

### Cross-OS bridge expectations
- Android and iOS peers broadcast `PeerBridgeEvent` payloads over UDP multicast `224.1.1.29:45230`.
- Ensure both devices share the same Wi‑Fi network and that multicast traffic is allowed (some routers block it by default).
- Trigger a test alert on either platform (`Run Test` button) to observe mirrored notifications/logs on the peer.

## Key capabilities
- Offline/on-device speech & SMS safe word detection
- Configurable contacts, sensitivity, siren, and location sharing
- Emergency escalations with retries and logging
- PeerBridge layer enabling devices to share alert events over local Wi-Fi/Bluetooth without servers

## Prebuilt downloads

### Android
- **Workflow artifacts (debug)**: Every run of the [Build Mobile Apps](https://github.com/DamienLove/TheSafewordApp/actions/workflows/mobile-build.yml) workflow uploads an `android-apks-debug` artifact containing the latest free and pro debug APKs.
- **GitHub Releases (signed)**: When a release is published, the workflow signs the free and pro variants and attaches `androidApp-free-release.apk` and `androidApp-pro-release.apk` directly to the release page.

### iOS
- **Shared XCFramework**: Each workflow run zips `Shared.xcframework` and publishes it as the `shared-xcframework` artifact for reuse in other Apple targets.
- **Simulator build**: Non-release runs upload `ios-simulator-build`, a zipped `.app` bundle you can sideload into the iOS Simulator.
- **Signed IPA**: Release runs export a production-signed `SafeWord.ipa`, available both as a workflow artifact (`ios-app-store-ipa`) and as a GitHub release asset.

## Continuous integration overview
- **Triggering**: The Build Mobile Apps workflow runs on pushes to `main`/`develop`, pull requests, manual dispatches, and published releases.
- **Android lane**: Builds debug APKs on every run, then (for releases) injects signing credentials from repository secrets to produce distributable APKs and attaches them to the release.
- **iOS lane**: Always generates the Kotlin Multiplatform framework, simulator build, and (for releases) codesigns an App Store IPA. Release assets are uploaded automatically via the workflow.
- **Secrets to configure**:  
  - Android: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`  
  - iOS: `IOS_CERT_BASE64`, `IOS_CERT_PASSWORD`, `IOS_PROVISION_PROFILE_BASE64`, `IOS_PROVISIONING_PROFILE_SPECIFIER`, `IOS_CODE_SIGN_IDENTITY`, `IOS_TEAM_ID`, optional `IOS_BUNDLE_ID`

## Voice shortcuts (Hey Google / Hey Siri)
- **Android**
  - Static app shortcuts live in `androidApp/src/main/res/xml/shortcuts.xml` (“Start listening” and “Trigger test alert”).
  - `androidApp/src/main/res/xml/actions.xml` binds those shortcuts to Assistant App Actions so users can say “Hey Google, start SafeWord listening” after verifying Digital Asset Links.
  - `MainActivity` reacts to the custom actions (`com.safeword.action.START_LISTENING`, `com.safeword.action.RUN_TEST`) to toggle listening or fire the test siren hands-free.
- **iOS**
  - `SiriShortcuts.donateStartListening()` / `donateTriggerTest()` donate `NSUserActivity` shortcuts with suggested phrases like “Start SafeWord”.
  - When the user enables listening or runs a test, the app refreshes the donation so Siri recommendations stay up to date.

## Custom siren & alert controls
- Place a branded siren at `androidApp/src/main/res/raw/safeword_alert.(ogg|mp3)` to override the system alarm tone. The app will loop that audio as the emergency alert.
- Alert notifications now include a **Silence alarm** action that stops playback instantly without waiting for timers to expire, and linked SafeWord peers can exchange gentle *Pings* that play a softer tone when you tap the new Ping button on a contact card.
- Direct hooks into "Hey Google" or "Hey Siri" hotwords are not permitted by platform policies; SafeWord continues to run its own on-device recognizer with throttled restarts to minimise beeps and battery drain.

## License
Proprietary - internal use only.
