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

## License
Proprietary — internal use only.
