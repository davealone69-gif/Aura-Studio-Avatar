# Aura Studio

**Local-first explicit avatar lab for Android.** Zero cloud tokens.

## Features

- Cyber Avatar Designer (nude, face/body/pose/effects)
- Dolphin Opinion + Enhance (uncensored local LLM)
- Data-driven Rooms (Bedroom, Dungeon, Studio, Club)
- Generate image/video + room picker
- Gallery history + favorites
- Models file picker
- Settings (steps, CFG, size)
- System Monitor + self-repair
- Room DB v3 with migrations
- Save to Pictures/AuraStudio

## Architecture

```
UI → AIService / DolphinService
   → AvatarEngine · RoomEngine · MemoryEngine · RelationshipEngine
   → Dolphin / Diffusion / Video
   → SystemMonitor (HealthMonitor + SelfRepairService)
```

See ARCHITECTURE.md and NATIVE.md.

## Run

```bash
./gradlew :app:assembleDebug
```

## Routes

list · designer · generate/{id} · models · gallery · settings · diagnostics

## Build status

CI runs `assembleDebug` on every push to `main`. Download the `app-debug-and-logs` artifact from the Actions tab.
