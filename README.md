# Aura Studio — Local Avatar Designer

Dark futuristic Android app for designing explicit AI avatars and preparing them for **local** image & text-to-video generation.

## Features

- **Avatar Designer** — full dark cyber UI with left/right micro-panels, hologram-style preview, glass bottom dock
- **Strong Nude toggle** — one-tap “No Clothes” control
- Full body / face / hair / outfit / pose / expression / effects control
- **Live unfiltered prompt** built from all settings
- **Local model registry** — register your own model files (no cloud tokens)
- Image + Text-to-Video generation modes
- Room persistence
- Hilt + ViewModel + Compose

## Design language

- Background `#05070C`
- Neon cyan `#00F0FF` + magenta accents
- Glassmorphism panels + subtle grid
- Micro-animations ready

## Status

- Core designer UI + navigation + data layer complete
- Generation screen ready for local backend wiring (stable-diffusion.cpp / LiteRT / MNN / etc.)
- Model manager scaffold in place

## Run

Open in Android Studio → Sync → Run.

Requires Android Studio with recent AGP + Kotlin + Compose.

## Notes

- Age range locked 18–45
- No content guardrails in the prompt path
- All generation is designed to stay on-device
