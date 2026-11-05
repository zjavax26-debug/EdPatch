# LumpForge

Android file explorer and game-resource editor, focused initially on Doom WAD and PK3 archives.

## Product scope
The interface takes inspiration from advanced file managers, but it is not intended to reproduce every feature or menu from MiX Silver. Only features relevant to exploring, inspecting, organizing, and editing game-resource files will be included. Unrelated general-purpose actions (for example, email sharing, encryption/decryption, and similar utilities) are out of scope unless explicitly requested later.

## Platform
- Minimum Android version: 8.0 (API 26)
- Language: Kotlin
- UI: Jetpack Compose
- Build: Gradle / Android Gradle Plugin

## Build
The GitHub Actions workflow builds a debug APK on every push and pull request. Download it from the workflow run's Artifacts section.
