# LumpForge

Android file explorer and game-resource editor, focused initially on Doom WAD and PK3 archives.

## Product scope
The interface takes inspiration from advanced file managers, but it is not intended to reproduce every feature or menu from MiX Silver. Only features relevant to exploring, inspecting, organizing, and editing game-resource files will be included. Unrelated general-purpose actions (for example, email sharing, encryption/decryption, and similar utilities) are out of scope unless explicitly requested later.

## Platform
- Minimum Android version: 8.0 (API 26)
- Language: Kotlin
- UI: Jetpack Compose
- Build: Gradle / Android Gradle Plugin

## Current prototype
- Select a folder through Android's Storage Access Framework.
- Browse folders and list their files.
- Show basic file type and size information.
- Select a file to inspect its basic details.

Folder access is limited to the location explicitly selected by the user. WAD/PK3 archive inspection, multi-selection, tabs, preview panes, and editing tools will be added in later iterations.

## Build
The GitHub Actions workflow builds a debug APK on every push and pull request. Download it from the workflow run's Artifacts section.
