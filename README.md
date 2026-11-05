# LumpForge

LumpForge is a planned Android workstation for creating and editing Doom engine resources.

The initial scope is archive and resource editing: WAD, PK3/ZIP, and individual lumps such as graphics, sprites, flats, textures, audio, music, text, and scripts. A map editor is explicitly out of scope for the first application milestone and may be considered as a separate future app.

## Project status

This repository is the Android-native implementation workspace. The existing `Muestra` repository is a visual/interaction reference, not the application codebase.

- Project name: **LumpForge** (provisional)
- Platform: Android
- Initial UI stack: Kotlin + Jetpack Compose
- Initial development branch: `feature/lumpforge-foundation`
- Current milestone: architecture and foundation

See [docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md) for scope, architecture, interaction requirements, and milestones.

## Design references

The `Muestra` repository provides visual references for colors, typography, icons, the sprite/image inspector, texture composition, audio preview, and script editor concepts. Its current implementation is a React/Vite web prototype with mock archive data; it is not a native Android app or a production archive engine.

## Principles

1. Never load an entire large archive into memory just to list or preview its contents.
2. Keep archive parsing and editing independent from the UI.
3. Preserve original files until the user explicitly saves or exports.
4. Use temporary working copies for engine testing.
5. Make explorer gestures, panel placement, toolbars, themes, and language configurable.
6. Keep the first release focused: no map editor in the initial app.
