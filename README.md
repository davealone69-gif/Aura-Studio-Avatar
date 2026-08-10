# Aura Studio

Local-first Android app for designing sexy AI model avatars and generating pictures.

## Current Features
- Full Avatar Creator (age, body, breasts, ethnicity, eyes, hair, skin, clothing / nude)
- Live prompt generation
- Room database persistence
- Avatar list with create / edit / delete
- Main home screen
- Generate screen (prompt ready, image gen placeholder)
- Navigation between all screens
- Hilt + ViewModel + Compose

## How to run
1. Open in Android Studio
2. Sync Gradle
3. Run on device/emulator

## Project structure
```
app/src/main/java/com/aura.studio/
├── MainActivity.kt
├── AuraStudioApp.kt
├── avatar/AvatarSpec.kt
├── data/ (Room + Repository)
├── di/DatabaseModule.kt
└── ui/
    ├── AuraNavHost.kt
    ├── MainScreen.kt
    ├── AvatarListScreen.kt
    ├── AvatarCreatorScreen.kt
    ├── GenerateScreen.kt
    └── theme/Theme.kt
```

## Next
- Wire real image generation
- Video generation later
- Chat / roleplay
