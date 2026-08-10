# Aura Studio - Avatar Creator

Lite, focused Android app for designing custom sexy AI avatars.

## What works right now

- Create detailed avatars (age 18+, body, breasts, ethnicity, eyes, hair, skin, clothing / nude)
- Live prompt preview
- Save / edit / delete avatars (Room)
- List screen + Creator screen + Detail screen
- Full navigation between screens
- Hilt + ViewModel + Compose
- Dark / light theme

## Project structure

```
app/src/main/java/com/aura.studio/
├── AuraApp.kt
├── MainActivity.kt
├── avatar/
│   └── AvatarSpec.kt
├── data/
│   ├── AvatarEntity.kt
│   ├── AvatarDao.kt
│   ├── AppDatabase.kt
│   └── AvatarRepository.kt
├── di/
│   └── DatabaseModule.kt
└── ui/
    ├── AuraNavGraph.kt
    ├── AvatarListScreen.kt
    ├── AvatarCreatorScreen.kt
    ├── AvatarDetailScreen.kt
    ├── AvatarViewModel.kt
    └── theme/Theme.kt
```

## How to run

1. Open in Android Studio
2. Sync Gradle
3. Run on device / emulator

## Next (when ready)

- Real image generation from the prompt
- Video generation later
- Chat with the avatar

No Swarm / background worker code included — kept deliberately simple.
