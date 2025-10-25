# SafeWord iOS Module

SwiftUI app that consumes the shared Kotlin Multiplatform framework and exposes SafeWord functionality within iOS background constraints.

## Bootstrap
1. Run `./gradlew shared:assembleSharedXCFramework` (or `syncFramework` task) to generate `build/XCFrameworks/release/Shared.xcframework`.
2. Execute `scripts/bootstrap.sh` to generate `SafeWord.xcodeproj` and wire the framework reference.
3. Open the project in Xcode, trust microphone/SMS entitlements (SMS limited to test-on simulator), and run on device.

## Targets
- `SafeWordApp`: main SwiftUI executable.
- `SafeWordUITests`: snapshot tests for the flows.

## Entitlements
The generated `SafeWordApp.entitlements` file grants:
- Microphone (`com.apple.security.device.microphone`)
- Location When In Use (`com.apple.security.personal-information.location.when-in-use`)
- Push notifications (for future roadmap)

## Peer bridge
`PeerBridgeManager` wraps MultipeerConnectivity to discover peer SafeWord nodes and share escalation payloads.

