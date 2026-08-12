# Aura Studio — Local Avatar Designer

Dark futuristic Android app for designing explicit AI avatars and generating images/video **fully on-device**.

## Features

- **Avatar Designer** — cyber UI, nude toggle, hologram preview, glass dock
- **Dolphin Opinion** — one tap sets all avatar fields
- **Enhance with Dolphin** — expands prompt, no refusals
- **Local image generation** — diffusion engine + result display
- **Local text-to-video** — scaffold ready
- **Model registry** — GGUF / weights on device (token-free)
- Room + Hilt + Compose

## Architecture

```
UI → AvatarSpec / Opinion / Enhance
  → DolphinLlmEngine → LlamaBridge → libaura_llama.so
  → DiffusionImageEngine → SdBridge → libaura_sd.so
  → DiffusionVideoEngine → VideoBridge → libaura_video.so
```

See **NATIVE.md** for real backends.

## Run

Android Studio (AGP 8.2+, JDK 17, NDK + CMake) → Sync → Run on arm64-v8a.

Place models under `/sdcard/Models/` and register in-app.

## Notes

- Age 18–45 only
- No content guardrails
- Local-first
