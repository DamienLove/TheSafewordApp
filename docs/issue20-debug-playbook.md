# Issue 20 Debugging Playbook

Use this checklist to capture the diagnostics we need for the SafeWord contact link/ping regression before we move on to issues 21 and the dashboard/widget overhaul.

## 1. Prepare a Working Branch
- `git checkout master`
- `git pull`
- `git checkout -b fix/issue-20-ping-handshake`

Keep all logging and fixes for issue&nbsp;20 on this branch so the later UI/workflow branches stay clean.

## 2. Ready Both Android Devices
1. Enable Developer Options (tap *Build number* seven times) and turn on USB debugging on **both** phones.
2. Connect one device at a time; confirm with `adb devices`.
3. Install the latest debug build on each phone:  
   `./gradlew.bat androidApp:installFreeDebug`
4. After each install, clear SafeWord state so we always test the full link flow:  
   `adb shell pm clear com.safeword.free.debug`
5. In Settings, disable carrier/OEM SMS spam filters or RCS “chat” conversions—SafeWord relies on raw SMS delivery.

## 3. Open Logcat Sessions
Run one command per device (replace `<device-id>` with the value from `adb devices`):

```bash
adb -s <device-id> logcat -v time SafeWordSmsSender:V SafeWordEngine:V PeerBridge:V AndroidEmergencyDispatcher:V *:S | tee logs/issue20/<device-label>-issue20.log
```

Suggested labels: `sender` for the initiating phone, `receiver` for the contact.

## 4. Reproduce the Problem
1. Launch SafeWord on both phones; complete onboarding and grant microphone, notifications, contacts, SMS, and DND access.
2. On the **sender** handset:
   - Add the receiver as a contact (ensure the phone number matches the SIM’s SMS identity).
   - Send a SafeWord link/invite; confirm the receiver’s ringer raises and DND disables.
3. Once the link shows as **Linked**, send at least three signals (ping, call, or non-emergency message). Note whether the receiver’s SMS still contains the `[SAFEWORD] CONTACT ...` payload or falls back to plain text.

## 5. Capture SMS Payloads
Immediately after the failed attempt on each device:

```bash
adb -s <device-id> shell content query --uri content://sms/inbox \
  --projection "_id,address,date,body" --sort "date DESC" --limit 5 \
  > logs/issue20/<device-label>-sms.txt
```

These dumps show the actual message bodies SafeWord received.

## 6. Package the Evidence
- `logs/issue20/sender-issue20.log`
- `logs/issue20/receiver-issue20.log`
- `logs/issue20/sender-sms.txt`
- `logs/issue20/receiver-sms.txt`
- A short note describing what you observed (ringer behavior, toast text, notifications).

Commit or share these artifacts so we can trace the handshake state machine and adjust the encoding.

## 7. Next Steps After Issue 20
Once the regression is fixed, branch anew for issue&nbsp;21 (`git checkout master && git pull && git checkout -b fix/issue-21-...`) and plan the widget/UI overhaul on a dedicated feature branch to keep history organized.
