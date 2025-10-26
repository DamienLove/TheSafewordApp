# SafeWord MCP Android Studio Bridge

This package exposes SafeWord's Gradle wrapper and ADB utilities over the Model Context Protocol so Codex (or any MCP-capable client) can ask the project to build, test, and inspect Android artifacts.

## Prerequisites
- Node.js 18+ (Node 25 is already available in this environment).
- Android SDK and platform tools (`adb`) on `PATH`.
- Gradle wrapper files in the repository root (`gradlew`, `gradlew.bat`).

## Installation
```bash
cd tools/mcp-android-studio
npm install
```

> If this environment blocks outbound network calls, install the dependencies on a host with access and copy the resulting `node_modules` directory into place.

## Usage
1. Register the server with Codex by ensuring `codex.mcp.json` in the repository root contains:
   ```json
   {
     "servers": [
       {
         "name": "android-studio",
         "command": ["node", "tools/mcp-android-studio/index.js"],
         "cwd": ".",
         "env": {
           "JAVA_HOME": "C:\\\\Program Files\\\\Android\\\\Android Studio\\\\jbr",
           "ANDROID_HOME": "%USERPROFILE%\\\\AppData\\\\Local\\\\Android\\\\Sdk"
         }
       }
     ]
   }
   ```

2. Launch the Codex CLI in the repository root; the CLI will spawn the MCP server automatically.

3. Within Codex, invoke the exposed tools:
   - `gradle-task` — run Gradle wrapper tasks, e.g. `{"tasks":["androidApp:assembleDebug"]}`.
   - `adb-shell` — execute `adb shell` commands, e.g. `{"args":["logcat","-d"]}`.

4. The server also exposes resources (`project/gradle-tasks`, `project/gradle-properties`) that Codex can pull for additional context during conversations.

## Extending
- Add more MCP tools by calling `server.tool("<name>", ...)` in `index.js`.
- Mirror additional project files through `server.resource(...)`.
- Wrap long-running commands with streaming or chunking if output exceeds the default 16 MB buffer.
