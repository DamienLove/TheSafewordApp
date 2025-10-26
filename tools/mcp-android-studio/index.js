#!/usr/bin/env node

import { Server } from "@modelcontextprotocol/sdk/server";
import { execFile } from "node:child_process";
import { readFile } from "node:fs/promises";
import { fileURLToPath } from "node:url";
import { promisify } from "node:util";
import path from "node:path";
import process from "node:process";

const execFileAsync = promisify(execFile);
const MAX_BUFFER = 1024 * 1024 * 16; // 16 MB

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const repoRoot = path.resolve(__dirname, "..", "..");

const gradleBinary = process.platform === "win32" ? "gradlew.bat" : "./gradlew";
const gradlePath = path.resolve(repoRoot, gradleBinary);

const server = new Server({
  name: "safeword-android",
  version: "0.1.0",
  description: "MCP bridge exposing Gradle and ADB utilities for the SafeWord Android project."
});

function buildExecOptions(extra = {}) {
  return {
    cwd: repoRoot,
    env: { ...process.env, ...extra.env },
    shell: process.platform === "win32",
    maxBuffer: MAX_BUFFER,
    windowsHide: true
  };
}

async function runCommand(command, args = [], options = {}) {
  try {
    const { stdout, stderr } = await execFileAsync(command, args, buildExecOptions(options));
    return { stdout, stderr };
  } catch (error) {
    const stderr = error.stderr ?? error.message;
    const stdout = error.stdout ?? "";
    const exitCode = typeof error.code === "number" ? error.code : undefined;
    const commandLine = [command, ...args].join(" ");
    const prefix = exitCode !== undefined ? `exit code ${exitCode}` : "failed";
    throw new Error(`Command "${commandLine}" ${prefix}:\n${stderr || stdout}`);
  }
}

async function runGradle(taskArgs = [], options = {}) {
  return runCommand(gradlePath, taskArgs, options);
}

const formatOutput = (stdout, stderr) => {
  const cleanedStdout = stdout.trim();
  const cleanedStderr = stderr.trim();
  if (cleanedStdout && cleanedStderr) {
    return `${cleanedStdout}\n\n[stderr]\n${cleanedStderr}`;
  }
  if (cleanedStderr && !cleanedStdout) {
    return `[stderr]\n${cleanedStderr}`;
  }
  return cleanedStdout || "(no stdout)";
};

server.tool("gradle-task", {
  description: "Run one or more Gradle tasks using the project wrapper",
  inputSchema: {
    type: "object",
    required: ["tasks"],
    properties: {
      tasks: {
        type: "array",
        items: { type: "string" },
        minItems: 1,
        description: "Ordered list of tasks/arguments to pass to ./gradlew"
      },
      env: {
        type: "object",
        additionalProperties: { type: "string" },
        description: "Optional environment overrides for the Gradle invocation"
      }
    }
  },
  handler: async ({ tasks, env = {} }) => {
    const { stdout, stderr } = await runGradle(tasks, { env });
    return {
      content: [{ type: "text", text: formatOutput(stdout, stderr) }]
    };
  }
});

server.tool("adb-shell", {
  description: "Execute an adb shell command against the default device",
  inputSchema: {
    type: "object",
    required: ["args"],
    properties: {
      args: {
        type: "array",
        items: { type: "string" },
        minItems: 1,
        description: "Arguments to append after `adb shell`"
      },
      serial: {
        type: "string",
        description: "Optional device serial to target via -s"
      }
    }
  },
  handler: async ({ args, serial }) => {
    const adbArgs = [];
    if (serial) {
      adbArgs.push("-s", serial);
    }
    adbArgs.push("shell", ...args);
    const { stdout, stderr } = await runCommand("adb", adbArgs);
    return {
      content: [{ type: "text", text: formatOutput(stdout, stderr) }]
    };
  }
});

server.resource("project/gradle-tasks", {
  description: "Output of `./gradlew tasks --all --console plain`",
  mimeType: "text/plain",
  handler: async () => {
    const { stdout } = await runGradle(["tasks", "--all", "--console", "plain"]);
    return {
      contents: [
        {
          uri: "project/gradle-tasks",
          mimeType: "text/plain",
          text: stdout
        }
      ]
    };
  }
});

server.resource("project/gradle-properties", {
  description: "Contents of gradle.properties for reference",
  mimeType: "text/plain",
  handler: async () => {
    const gradlePropsPath = path.resolve(repoRoot, "gradle.properties");
    let text = "";
    try {
      text = await readFile(gradlePropsPath, "utf8");
    } catch (error) {
      throw new Error(`Unable to read gradle.properties at ${gradlePropsPath}: ${error.message}`);
    }
    return {
      contents: [
        {
          uri: "project/gradle-properties",
          mimeType: "text/plain",
          text
        }
      ]
    };
  }
});

await server.start();
