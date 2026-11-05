# LumpForge — Project plan and architecture

**Status:** Initial planning baseline  
**Project name:** LumpForge (provisional)  
**Target:** Android  
**First application:** Doom resource/archive editor; map editing excluded from the initial scope.

## 1. Product vision

Build an Android-native workstation inspired by SLADE 3's resource/archive workflows and MiXplorer Silver's configurable file-browser interactions. It should handle WAD, PK3/ZIP and individual Doom resources, with a modular editor system and a future add-on mechanism for external tools and game engines.

The goal is not to copy another application's source or branding. MiXplorer Silver is an interaction reference; Muestra is the user's own visual prototype and feature reference.

## 2. Repositories and source-of-truth

- `zjavax26-debug/EdPatch`: Android-native LumpForge implementation repository. The initial work branch is `feature/lumpforge-foundation`.
- `zjavax26-debug/Muestra`: existing React 19 + TypeScript + Vite prototype. Use it as a visual/behavior reference only.

The inspected Muestra prototype includes:
- Plus Jakarta Sans for UI text and JetBrains Mono for code/hex text.
- Lucide icons.
- Dark neutral surfaces with rose/pink accent colors.
- Existing prototype components for archive tabs, lump lists, navigation drawer, asset preview, sprite inspector, texture editor, audio preview, script preview, and palette tools.
- Mock archive data; the current prototype is not evidence of a real WAD/PK3 parser or production file I/O.

## 3. Proposed Android technology

### Application and UI
- Kotlin.
- Jetpack Compose for native UI and configurable layouts/gestures.
- Material 3 foundations with a custom token-based theme layer rather than hard-coded colors.
- Android Storage Access Framework (SAF) for user-selected files and folders; do not assume unrestricted filesystem access.

### File and archive engine
- A separate Kotlin/JVM core module with no Android UI dependencies.
- WAD reader/writer with indexed entries (lumps), namespace/marker handling, duplicate-name handling, and safe save operations.
- PK3/ZIP reader/writer with central-directory indexing and ZIP64 support where feasible.
- Streaming and random-access abstractions. Do not read multi-hundred-megabyte or gigabyte archives wholly into RAM.
- Use seekable file descriptors when available. For providers that only expose streams, stage data in app-private temporary storage using bounded buffers.
- Preview only the selected resource and decode images/audio on demand; use bounded caches.
- Save through a temporary output and validate before replacing/exporting the destination. Keep originals untouched until the user confirms a save operation.

### Testing
- Unit tests for archive parsing, malformed inputs, large offsets, duplicate entries, compression, and round-trip integrity.
- Instrumentation tests for SAF providers and Android lifecycle behavior.
- Test fixtures with known-good WAD and PK3 files; do not commit copyrighted game data.

## 4. Application modules

1. **Explorer** — storage roots, folders, bookmarks, history, sorting, filters, list/grid/gallery modes.
2. **Archive workspace** — open WAD/PK3/ZIP, list entries, search, categorize, select, reorder where format permits.
3. **Preview panel** — dock right or bottom, hide/show, icon-only open-in-tab action.
4. **Tabs** — archive and resource editor tabs, close/reopen behavior, unsaved-change indicators.
5. **Editors** — image/sprite, texture composition, audio/music, text/scripts, hex and metadata. No map editor in the initial app.
6. **Settings** — theme/color editor, toolbar customization, gesture settings, layout, language, import/export of settings including theme data.
7. **Add-ons** — versioned and permission-scoped extension points; add-on failure must not crash the core app.
8. **Engine integration (future)** — separate optional runner/add-on. Stage a temporary test set from saved working copies; preserve load order and never overwrite source archives for a test launch.

## 5. Explorer interaction baseline

Defaults, all configurable in Settings:

- Preview panel is on the right when space permits; user can move it to the bottom.
- Preview can be hidden/shown.
- A small external/open-in-tab icon appears inside the preview panel; no large labeled pill.
- Long-press a file row/name opens that resource in a tab.
- Long-press the file icon starts a drag gesture. Show a compact count badge near the finger when dragging multiple items.
- A normal tap selects an item. Once selection mode is active, tapping names can add/remove items and start a drag.
- Long-press a second item after selecting an earlier item selects the intervening range.
- Selection toolbar shows selected item count and aggregate size. Its actions are configurable and horizontally scroll when they do not fit.
- With no selection, the overflow menu includes filtering. With a selection, the menu changes to selection-specific actions such as Properties and Open in tab.
- Selection tools include range, toggle, inverse, previous, next, files only, same extension, same type, none, and all.
- Selection-by-category can narrow the current selection to graphics/sprites, textures, audio, music, maps, scripts, text, and other recognized resource classes. This is distinct from list filtering, which hides nonmatching entries.
- Pinch-to-resize list item icons and names; setting to enable/disable it.
- Scroll can hide/show top bars and tabs, configurable independently.
- Explorer view modes and row details are configurable.

## 6. Visual identity

Use Muestra as a reference, not as a code dependency:
- UI typeface reference: Plus Jakarta Sans.
- Monospaced/code typeface reference: JetBrains Mono.
- Icon reference: Lucide family; use native Android vector assets or a compatible icon package.
- Palette reference: near-black/neutral backgrounds, rose/pink accent, muted secondary text, distinct semantic colors for resource types.
- Image editor reference: sprite inspector with pixelated rendering, zoom, pan, crosshair/origin controls, palette selection, and export/convert actions.

Theme values must be design tokens, editable by users and serializable.

## 7. Localization and settings portability

- Start with Spanish, English, and Brazilian Portuguese.
- All user-facing strings must be resource-based; no UI text literals embedded in composables.
- Settings export/import must include layout, gestures, toolbar arrangement, language, theme name, and custom color tokens.
- Use a versioned settings schema and migrations. Validate imported files before applying them.

## 8. Security and release hardening

- Do not promise that code obfuscation makes an app unhackable. Use R8 shrinking/obfuscation, release signing, dependency review, least-privilege permissions, and secure handling of temporary files.
- Never embed API secrets or signing keys in the app/repository.
- Add-on packages must be validated and constrained; do not grant arbitrary filesystem access by default.
- Keep debug builds and test fixtures separate from release builds.

## 9. Milestones

### M0 — Foundation
- Establish Android project structure, build configuration, CI/build checks, and architecture tests.
- Define archive engine interfaces and data models.
- Extract Muestra design tokens and document interaction behavior.

### M1 — Explorer shell
- SAF storage picker and granted-root navigation.
- File list, sorting, filtering, selection, configurable row sizing, drawer/bookmarks/history.
- Preview panel layout and tab shell with sample data.

### M2 — Read-only archive support
- WAD and PK3/ZIP indexing and browsing.
- Resource type classification, metadata, preview dispatch.
- Large-file and malformed-file tests.

### M3 — Editing and safe save
- First editors (image/sprite and text), import/export of lumps, undo/redo foundation.
- Transactional save/export and recovery behavior.

### M4 — Additional editors
- Audio/music, texture composition, palette tools, hex view, ACS/script tooling as feasible.

### M5 — Add-ons and engine runner
- Extension API and optional engine integration.
- Temporary test workspace and launch profiles.

### M6 — Release readiness
- Localization completion, accessibility, performance profiling, R8, signing, privacy disclosures, Play Store compliance.

## 10. Explicit non-goals for initial app

- No map editor in the initial LumpForge app.
- No direct modification of original archives merely by previewing or launching a test.
- No requirement to implement every SLADE feature before a usable first release.
