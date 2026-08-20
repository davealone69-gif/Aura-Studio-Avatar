# Aura Studio Avatar consolidation audit

## Canonical repository

`Aura-Studio-Avatar` is the canonical Android product. Truth-time and Grok-Girls are source projects, not additional runtime modules.

## Truth-time contribution

Useful functionality identified in Truth-time:

- Persona model and persona switching concepts
- Chat message model with optional image/video attachments
- Camera-motion scene model
- Generated-media references
- DataStore-backed settings/chat persistence concepts
- Gemini-backed chat and resilient fallback behavior

These concepts are consolidated into `domain/companion/CompanionModels.kt` using Aura Studio's package and domain conventions. The old `com.example` models are deliberately not copied into the app.

## Grok-Girls contribution

The repository was audited at its current `main` tree. Its video export page is explicitly a `PlaceholderPage`, and its service layer is a small web prototype. It does not contain a production implementation that should be transplanted into the Android app. Its useful product concepts are therefore retained as requirements, not copied as code.

In particular, `VideoExportPage.tsx` describes HD resolution, format, FPS, progress, save and share requirements, but delegates all behaviour to a placeholder. Aura Studio must implement those requirements natively rather than importing placeholder code.

## Architecture rule

Do not create parallel Truth-time/Grok-Girls packages. Aura Studio owns:

- `com.aura.studio.domain` for models
- repositories for persistence and media
- `AiChatService` for chat providers
- the avatar engine for state/animation
- Studio for generation, playback and export

## Remaining merge work

1. Connect `CompanionPersona` to the canonical avatar/persona repository.
2. Persist `CompanionMessage` and `GeneratedMedia` through Room rather than string-packed DataStore history.
3. Replace Studio's simulated video preview with real local MP4 playback.
4. Implement real export/share using Android media APIs.
5. Add integration tests covering persona -> chat -> generated media -> playback.
6. Keep API credentials out of source and build-time APK constants.
