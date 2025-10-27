# SafeWord Store Readiness

This document tracks everything we can prepare before the Google Play and Apple developer accounts are activated. It covers assets that are already in place, items we finished during this session, and the checklist to run through the minute the accounts are funded.

---

## 1. Build Outputs & Signing

- **Android debug validation:** `./gradlew.bat androidApp:assembleFreeDebug` (already green).
- **Android release bundles (unsigned for now):**
  ```pwsh
  ./gradlew.bat androidApp:bundleFreeRelease
  ./gradlew.bat androidApp:bundleProRelease
  ```
- **Keystore prep:** run once the signing passwords are known.
  ```pwsh
  keytool -genkeypair -v `
    -keystore safeword-release.jks `
    -alias safeword-release `
    -keyalg RSA -keysize 4096 `
    -validity 3650
  ```
  - Place the file somewhere safe, then add its path and passwords to `~/.gradle/gradle.properties` as:
    ```
    ANDROID_KEYSTORE_PATH=C:\\path\\to\\safeword-release.jks
    ANDROID_KEYSTORE_PASSWORD=***
    ANDROID_KEY_ALIAS=safeword-release
    ANDROID_KEY_PASSWORD=***
    ```
  - Rerun the release bundle tasks to generate signed AABs once the credentials exist.
- **iOS signing:** keep the following ready to upload once the Apple account is live:
  - Apple developer team ID
  - Bundle ID (`com.safeword.app` for App Store, adjust if needed)
  - Distribution certificate (.p12) + password
  - Provisioning profile for App Store distribution
  - Optional: app-specific password for Transporter if uploading manually

---

## 2. Google Play Listing Prep

- **Title (<=30 chars):** `SafeWord – Silent SOS`
- **Short description (<=80 chars):** `Trigger silent alarms, ping trusted contacts, and stay protected.`
- **Full description draft (up to 4000 chars):**
  ```
  SafeWord keeps you connected to help without pulling attention to yourself. Configure one or more safe words, assign trusted contacts, and the app quietly listens in the background. Speak your phrase, and SafeWord will alert your network, dial preset numbers, or sound an alarm based on your plan.

  KEY FEATURES
  • Voice-triggered SOS: Activate alerts hands-free with your Safe Word.
  • Contact pings: Send silent nudges to check in with your circle.
  • Incoming Mode (Pro): React to Safe Words spoken by your trusted contacts.
  • Custom audio: Drop your own siren and ping tones in the app resources.
  • Voice & Siri shortcuts: “Hey Google/Hey Siri, start SafeWord listening.”
  • Dashboard insights: Monitor connection status and contact readiness.

  SafeWord Free shows native ads, limits you to three contacts, and restores your ringer after 10 minutes. Upgrade to Pro to remove ads, unlock incoming Safe Word support, expand contacts without limits, and shorten ringer recovery to 5 minutes.
  ```
- **Category:** Lifestyle → Safety (keep consistent between stores).
- **Tags:** `personal safety`, `emergency alerts`, `communication`.
- **App icon:** existing adaptive launcher icon at `androidApp/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`.
- **Screenshots:** capture 16:9 portrait (1080x1920) for free & pro flows once QA sign-off is done (listening screen, contact management, alert).
- **Feature graphic (1024x500):** create a simple gradient card with icon and tagline (`docs/marketing_campaign.md` has palette guidance).
- **Privacy policy URL:** host a rendered version of `docs/safe_word_full_product_technical_spec_v_1.md` or publish a simplified policy derived from it. The policy must cover microphone, SMS, location, notifications, advertising, and data retention.
- **App-ads.txt:** host the root-level `app-ads.txt` at your production domain (`google.com, pub-5327057757821609, DIRECT, f08c47fec0942fa0`).
- **Data Safety form (reference answers):**
  - **Data collected:** microphone audio (transient), location (approximate), personal contacts (user provided), phone numbers, device IDs (for ads), crash logs.
  - **Purpose:** app functionality, analytics, advertising, fraud prevention.
  - **Handling:** data is encrypted in transit; user can request deletion via in-app settings → contact support email.
  - **Optional Declarations:** declare SMS permissions as “optional” and clarify they are only used in the Pro variant for incoming alerts.
- **Permission declarations:** Play Console requires questionnaires for `RECEIVE_SMS`, `SEND_SMS`, `READ_SMS`, `RECORD_AUDIO`, `ACCESS_FINE_LOCATION`, `POST_NOTIFICATIONS`, `CALL_PHONE`, and `READ_CONTACTS`. Prepare short rationales (see section 4 below).

---

## 3. Apple App Store Prep

- **Bundle identifiers:** `com.safeword.app` (release), `com.safeword.app.dev` (TestFlight).
- **Versioning:** mirror Android (`1.0.0`, build number increments per upload).
- **App Store name (<=30 chars):** `SafeWord SOS`.
- **Subtitle (<=30 chars):** `Hands-free safety alerts`.
- **Promotional & subtitle text** can recycle the Google Play copy with minor tweaks.
- **Keywords:** `safety, emergency, sos, alarm, silent alerts, contacts`.
- **Primary category:** `Lifestyle`; secondary `Health & Fitness` or `Utilities`.
- **Privacy policy / EULA:** same URL as Android; confirm compliance with Apple’s microphone, location, and push notification disclosures.
- **App Privacy questionnaire:** align answers with the data collection list above, emphasizing that audio is processed on-device and not stored.
- **ATT prompt:** ensure the iOS implementation sets a localized string explaining why ads (where applicable) need tracking.
- **In-app purchase metadata:** none for v1 (upgrade is handled as a paid binary `pro` SKU rather than in-app purchase).
- **App Store assets once ready:** 6.5" portrait screenshots (1242x2688), 5.5" portrait (1242x2208), optional preview video showing listen/alert flow.

---

## 4. Permission Usage Rationale (for Play Console / App Store Forms)

| Permission | Usage Notes | Flavour |
|------------|-------------|---------|
| `RECORD_AUDIO` | Required to listen for the Safe Word phrase in the foreground service. | Free & Pro |
| `MODIFY_AUDIO_SETTINGS` | Adjusts audio focus and ringer volume during alerts. | Free & Pro |
| `POST_NOTIFICATIONS` | Pushes alert, check-in, and service status notifications. | Free & Pro |
| `READ/RECEIVE/SEND_SMS` | Enables incoming Safe Word detection and outbound SMS alerts in Pro. Feature flag keeps it off in Free. | Pro only (but declared globally) |
| `ACCESS_FINE/COARSE_LOCATION` | Optional location attachment in alerts and contact pings. | Free & Pro |
| `CALL_PHONE` | Allows the app to dial a configured emergency contact. | Free & Pro |
| `READ_CONTACTS` | Lets users pick device contacts to import into SafeWord. | Free & Pro |
| `FOREGROUND_SERVICE_*` | Keeps the listening service alive while respecting modern Android limits. | Free & Pro |
| `CHANGE_WIFI_MULTICAST_STATE` | Supports peer discovery for SafeWord bridge traffic. | Free & Pro |
| `ACCESS_NOTIFICATION_POLICY` | Temporarily overrides Do Not Disturb when alerts fire. | Free & Pro |

Document these justifications in the Play permission declarations and App Store privacy strings so review goes faster.

---

## 5. QA & Compliance Checklist

- [ ] Run smoke tests on Free & Pro builds (listen toggle, alert trigger, contact actions, ad dismissal).
- [ ] Validate SMS workflows on a device that grants carrier access (Pro).
- [ ] Confirm ads render below the dashboard and a11y reading order makes sense.
- [ ] Verify microphone permission prompts are localized and explain the purpose clearly.
- [ ] Capture accessibility audit: talkback navigation, color contrast, and large font scaling.
- [ ] Export Room schema or set `exportSchema=false` to silence build warnings before release.
- [ ] Review crash-free stability using internal testing (Firebase / Play internal track).
- [ ] Verify that hosting domain serves the privacy policy & `app-ads.txt` over HTTPS.

---

## 6. Next Steps Once Accounts Are Live

1. **Google Play:** create the app entry, upload the signed `free` AAB, answer Data Safety/permissions, attach screenshots, and publish to the internal testing track before production.
2. **Apple:** register bundle IDs, configure certificates, create the App Store record, upload the IPA via Transporter or CI, and submit to TestFlight for a first pass.
3. **CI secrets:** populate the GitHub Actions secrets listed in `safeword.txt` to automate signed builds.
4. **Pricing & distribution:** set Free SKU price to `Free`; configure `pro` as a paid app listing or keep distribution limited to a separate binary if required.
5. **Support readiness:** confirm the support email (in-app + store listings) routes to an inbox monitored during launch week.

Everything else in this document is ready now, so once the $25 fee hurdle is cleared you can move straight to uploading and filling in forms without additional engineering work.
