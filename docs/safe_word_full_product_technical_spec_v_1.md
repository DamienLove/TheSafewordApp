# SafeWord — Full Product & Technical Specification (v1.0)

## 1) Product summary
**SafeWord** is a personal safety app that listens for pre‑chosen spoken phrases ("safe words") and/or incoming SMS keywords, then escalates: crank ringer volume, optionally play a loud alert, broadcast SMS with your location to trusted contacts, and prompt a one‑tap call to local emergency services. The system favors offline, on‑device processing and minimal data collection.

### Goals
- Ultra‑fast trigger (< 1s from detection to action) with low false positives.
- Work reliably in the background on modern Android (Android 8–15) with battery/Doze constraints.
- Zero server dependency; privacy‑first.

### Non‑goals
- Continuous cloud transcription of all speech; storing audio off‑device.

---

## 2) Platforms & minimums
- **Android** (primary) — Min SDK 26; Target SDK 35. Kotlin.
- **iOS** (secondary roadmap) — SwiftUI prototype for UI parity; no background voice recognition (Apple restrictions); consider push/SOS workflows.

---

## 3) Core features & behaviors
1) **Voice SafeWord detection**
   - Foreground service with continuous speech recognition.
   - Two configurable safe words.
   - Sensitivity slider maps to VAD (voice activity detection) thresholds and post‑processing (exact mapping in §8.5).
   - Hysteresis + debounce: ignore repeats within a cooldown window (e.g., 10s).
   - Optional **"Test Mode"** to simulate a detection without real alerts.

2) **SMS SafeWord detection**
   - BroadcastReceiver watches incoming SMS and scans for safe words.
   - Works only when app is in **Incoming Mode**; **Outgoing Mode** disables auto‑reaction.

3) **Emergency escalation** (triggered by either path)
   - Raise ringer volume to max; optionally play an audible siren.
   - Send SMS to a contact list (1–5 contacts), up to N retries per contact with backoff.
   - Include last known location (geo URI) when permission is granted.
   - High‑priority notification with action to dial local emergency number (localized; default 911/112 config).

4) **Contacts**
   - Add/edit/delete contacts (name, phone, email). Import from device contacts.
   - Optional "invite" template via email/SMS.

5) **Settings**
   - Two safe words.
   - Sensitivity (1–100).
   - Toggles: Voice listening on/off, Include location in alerts, Play siren, Test Mode.
   - Mode switch: Incoming vs Outgoing.

6) **UX niceties**
   - Quick Settings tile to toggle Voice Listening.
   - Persistent status notification (“Listening for safe words…”).
   - Onboarding permission wizard.

---

## 4) App architecture (Android)
**Pattern:** MVVM + Service + BroadcastReceiver; DI optional (Hilt) but not required.

**Layers**
- **UI layer**: Activities/Fragments for Main, Settings, Contacts.
- **Domain**: Use‑cases (StartListening, StopListening, SaveSettings, AddContact, TriggerEmergency).
- **Data**: SharedPreferences (settings) + Room (contacts, history).
- **Platform services**: VoiceRecognitionService (foreground), EmergencyHandlerService (background), SmsReceiver (broadcast).

**Packages**
- `com.safeword.ui` (activities/fragments/views)
- `com.safeword.service` (VoiceRecognitionService, EmergencyHandlerService)
- `com.safeword.receiver` (SmsReceiver)
- `com.safeword.data` (Room entities/dao, Prefs)
- `com.safeword.util` (NotificationHelper, RingerManager, SoundManager, PermissionsHelper, LocationHelper)

---

## 5) Data model
### 5.1 Preferences (EncryptedSharedPreferences recommended)
- `safe_word_1` : String
- `safe_word_2` : String
- `sensitivity` : Int (1–100; default 50)
- `listening_enabled` : Boolean
- `incoming_mode_enabled` : Boolean (true = incoming mode; false = outgoing)
- `include_location` : Boolean (default true)
- `play_siren` : Boolean (default false)

> **Note:** Use one naming convention consistently (snake_case), and migrate existing keys on first run.

### 5.2 Room database
**Entities**
- `Contact`
  - `id` (PK, auto)
  - `name` (String)
  - `phone` (E164 normalized String)
  - `email` (String?)
  - `createdAt` (Long epoch)

- `AlertEvent`
  - `id` (PK)
  - `source` (enum: VOICE|SMS|TEST)
  - `detectedWord` (String)
  - `time` (Long)
  - `locationLat` (Double?)
  - `locationLon` (Double?)
  - `smsSent` (Boolean)
  - `contactsNotified` (Int)

**DAO**
- `ContactDao`: insert/update/delete/queryAll
- `AlertEventDao`: insert/queryLastN

---

## 6) Screens & UX
### 6.1 Main
- **Controls**: Toggle (Listening), Button (Record/Configure SafeWords), Button (Settings), Button (Contacts), Button (Mode switch w/ label and icon).
- **States**: Listening Active/Inactive; Mode label: Incoming/Outgoing.
- **Empty state**: If no safe words or contacts, show prompts.

### 6.2 Settings
- Text fields: Safe Word 1, Safe Word 2.
- Slider: Sensitivity (1–100) with preview bar.
- Switches: Include location, Play siren, Test Mode (pressing “Run Test” triggers EmergencyHandlerService with mock payload).
- Save button validates and stores.

### 6.3 Contacts
- List of contacts with Add and Edit.
- Add flow: Name, Phone (E164 validation), Email optional. Import picker.
- “Invite to set a SafeWord” (shares pre‑filled message via chooser).

### 6.4 Onboarding
- Multi‑step permission flow: Notifications → Microphone (for voice) → SMS (receive & send) → Location (optional).
- Battery optimization whitelist prompt (explain tradeoffs).

---

## 7) VoiceRecognitionService (behavioral spec)
- Runs as a **foreground service** with a persistent notification.
- Initializes `SpeechRecognizer` once; restarts listening on errors/timeouts.
- Lowercases and trims recognized text; compares against both safe words.
- Debounce window to avoid multiple triggers from the same utterance.
- On match: dispatch `TriggerEmergency` use‑case.
- Returns `START_STICKY` from `onStartCommand`; properly implements `onDestroy` and `onBind` (returns null).
- Foreground service type: `microphone`.

**Sensitivity mapping**
- Map 1–100 to recognizer settings and a confidence threshold `T`.
  - `T = 0.4 + (100 - s) * 0.004` → 1 ⇒ 0.796, 100 ⇒ 0.4 (example curve).
  - Only accept matches with confidence ≥ `T`.

---

## 8) Emergency escalation
### 8.1 Steps
1. Raise ringer volume to device maximum.
2. Optional siren: play looping alarm for 10s (or until user taps Stop).
3. Build message: `"SafeWord '<word>' detected. Location: <maps link or 'unavailable'>"`.
4. Send SMS to each contact; retry up to N (default 3) with 5s delay.
5. Show high‑priority notification with action button to dial local emergency number.
6. Log `AlertEvent`.

### 8.2 Failure handling
- If SEND_SMS denied: show actionable notification linking to permissions.
- If location unavailable in 5s: send SMS without location and continue.

### 8.3 Localization of emergency number
- Configurable per region with default fallback (911/112). Store in resources.

---

## 9) SMS receiver
- Enabled only in **Incoming Mode**.
- Observes `android.provider.Telephony.SMS_RECEIVED` broadcast.
- Parses PDU(s), concatenates message parts, checks for safe words (case‑insensitive).
- On match: dispatch `TriggerEmergency` use‑case; also bring ringer to max.

---

## 10) Notifications
- **Channels**
  - `safeword_listening` (LOW): “Listening for safe words…”
  - `safeword_emergency` (HIGH): “Emergency detected — tap to call.”
- **IDs**
  - Foreground service: 1
  - Emergency alerts: auto‑increment

---

## 11) Permissions (Android)
Required:
- `RECORD_AUDIO` (voice)
- `FOREGROUND_SERVICE` and `FOREGROUND_SERVICE_MICROPHONE`
- `RECEIVE_SMS` (keyword detection)
- `SEND_SMS` (outgoing alerts)
- `POST_NOTIFICATIONS` (Android 13+)
- `ACCESS_COARSE_LOCATION` / `ACCESS_FINE_LOCATION` (optional, for maps link)
- `MODIFY_AUDIO_SETTINGS` (ringer volume)

Optional:
- `READ_CONTACTS` (import contacts)
- `VIBRATE`

**Battery optimization**: prompt user to allow app to run unrestricted.

---

## 12) Manifest (reference implementation)
```xml
<manifest ...>
  <uses-permission android:name="android.permission.RECORD_AUDIO"/>
  <uses-permission android:name="android.permission.RECEIVE_SMS"/>
  <uses-permission android:name="android.permission.SEND_SMS"/>
  <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
  <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
  <uses-permission android:name="android.permission.CALL_PHONE"/>
  <uses-permission android:name="android.permission.READ_CONTACTS"/>
  <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
  <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MICROPHONE"/>

  <application ...>
    <activity android:name=".ui.MainActivity" .../>
    <activity android:name=".ui.SettingsActivity" .../>
    <activity android:name=".ui.ContactActivity" .../>

    <service
        android:name=".service.VoiceRecognitionService"
        android:exported="false"
        android:foregroundServiceType="microphone"/>

    <service
        android:name=".service.EmergencyHandlerService"
        android:exported="false"/>

    <receiver
        android:name=".receiver.SmsReceiver"
        android:enabled="true"
        android:exported="false">
      <intent-filter>
        <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
      </intent-filter>
    </receiver>
  </application>
</manifest>
```

---

## 13) Service, receiver & intent contracts
- **Intent → VoiceRecognitionService**
  - Action: `com.safeword.action.START_LISTENING`
  - Extras: none
- **Intent → EmergencyHandlerService**
  - Extras: `detectedSafeWord: String`
- **Broadcast → SmsReceiver**
  - Action: `android.provider.Telephony.SMS_RECEIVED`

---

## 14) Error states & edge cases
- Mic permission revoked while listening → stop service, post notification to re‑enable.
- Device in DND → still set ring volume; also vibrate + notification.
- No contacts configured → show urgent UI prompt after detection.
- Multiple matches in a single SMS → trigger once.
- App killed by OS → `START_STICKY` restarts service with notification.

---

## 15) Security & privacy
- Use EncryptedSharedPreferences for settings.
- No persistent audio recordings; only transient buffers for recognition.
- Minimal logs; redact phone numbers in non‑PII environments; allow user to clear history.
- All data remains on‑device; no external analytics by default.

---

## 16) Accessibility & localization
- Large tap targets, high‑contrast colors, Dynamic Type support.
- Content descriptions for TalkBack.
- Strings externalized for translation.

---

## 17) Telemetry (optional, privacy‑respecting)
- Local counters: total detections, false‑positive dismissals, average response time.
- Crash reporting: platform default (if user consents).

---

## 18) Testing strategy
- **Unit**: string matching, debounce, sensitivity mapping, SMS message builder.
- **Instrumentation**: Service lifecycle, permission gates, notification intents.
- **UI tests**: Espresso flows for Settings/Contacts/Main.
- **Manual**: Doze/battery restrictions, long‑running foreground service endurance.

---

## 19) Build & dependencies
- Kotlin (1.9+), AndroidX core‑ktx, AppCompat, Lifecycle runtime, Room, Coroutines.
- Play Services Location (optional) or Android location manager.
- Material Components for UI.

**Gradle (high‑level):**
- `implementation`:
  - `androidx.core:core-ktx`
  - `androidx.appcompat:appcompat`
  - `com.google.android.material:material`
  - `androidx.lifecycle:lifecycle-runtime-ktx`
  - `androidx.room:room-runtime` + `kapt room-compiler`
  - `org.jetbrains.kotlinx:kotlinx-coroutines-android`

---

## 20) Android Studio integration
- Import project → Configure min/target SDK (26/35).
- Turn on **Foreground Service** debug notifications.
- Add a debug Quick Settings tile component for fast toggling.
- Set up signing configs for release.

---

## 21) Roadmap (v1.1+)
- On‑device keyword spotting (always‑on hotword) with tiny ML (battery‑friendly) instead of classic SpeechRecognizer.
- WearOS companion app (panic tap).
- iOS: Background alternatives (quick‑launch, widgets, Siri phrase shortcut).

---

## 22) AI app generator hints (structured)
```json
{
  "app": "SafeWord",
  "platform": "android",
  "minSdk": 26,
  "targetSdk": 35,
  "modules": [
    {
      "name": "service.voice",
      "type": "foregroundService",
      "foregroundServiceType": "microphone",
      "notificationChannel": {"id": "safeword_listening", "importance": "LOW", "title": "Voice Recognition"},
      "logic": {
        "recognizer": "speechRecognizer",
        "compareKeys": ["safe_word_1", "safe_word_2"],
        "sensitivityKey": "sensitivity",
        "onMatchIntent": {"service": "service.emergency", "extras": ["detectedSafeWord"]}
      }
    },
    {
      "name": "receiver.sms",
      "type": "broadcastReceiver",
      "intentFilter": ["android.provider.Telephony.SMS_RECEIVED"],
      "logic": {
        "compareKeys": ["safe_word_1", "safe_word_2"],
        "modeGateKey": "incoming_mode_enabled",
        "onMatchIntent": {"service": "service.emergency", "extras": ["detectedSafeWord"]}
      }
    },
    {
      "name": "service.emergency",
      "type": "service",
      "logic": {
        "steps": ["raiseRinger", "optionalSiren", "resolveLocation", "buildMessage", "sendSmsToContacts", "postCallNotification", "logEvent"]
      }
    },
    {
      "name": "ui.main",
      "type": "activity",
      "widgets": ["toggle:listening_enabled", "button:configure_safe_words", "button:settings", "button:contacts", "button:mode_switch"],
      "navigation": {"settings": "ui.settings", "contacts": "ui.contacts"}
    },
    {
      "name": "ui.settings",
      "type": "activity",
      "widgets": [
        "text:safe_word_1",
        "text:safe_word_2",
        "slider:sensitivity(1..100)",
        "switch:include_location",
        "switch:play_siren",
        "button:run_test -> service.emergency"
      ]
    },
    {
      "name": "ui.contacts",
      "type": "activity",
      "widgets": ["list:contacts", "button:add_contact"],
      "entity": "Contact"
    }
  ],
  "permissions": [
    "RECORD_AUDIO", "FOREGROUND_SERVICE", "FOREGROUND_SERVICE_MICROPHONE",
    "RECEIVE_SMS", "SEND_SMS", "POST_NOTIFICATIONS",
    "ACCESS_COARSE_LOCATION", "ACCESS_FINE_LOCATION", "MODIFY_AUDIO_SETTINGS",
    "READ_CONTACTS", "CALL_PHONE"
  ],
  "storage": {
    "prefs": ["safe_word_1", "safe_word_2", "sensitivity", "listening_enabled", "incoming_mode_enabled", "include_location", "play_siren"],
    "room": {
      "entities": [
        {"name": "Contact", "fields": ["id:PK", "name", "phone", "email?", "createdAt:Long"]},
        {"name": "AlertEvent", "fields": ["id:PK", "source:enum", "detectedWord", "time:Long", "locationLat?", "locationLon?", "smsSent:Boolean", "contactsNotified:Int"]}
      ]
    }
  }
}
```

---

## 23) Release checklist
- QA on Android 11–15; microphone + SMS + location flows.
- Verify battery optimization settings and foreground notification visibility.
- Validate permissions UX and graceful degradation.
- Content review for privacy disclosures and store listing.

—

This spec is structured for an AI app generator to synthesize code, manifest, and resources with sensible defaults, while highlighting the spots where platform quirks require manual attention.