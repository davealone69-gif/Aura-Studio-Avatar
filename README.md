# Aura Studio

Focused Android app for designing custom sexy AI model avatars.

## Current features (working)

- **Avatar Creator** with full controls:
  - Name, Age (18+ only)
  - Ethnicity, Body type, Breast size
  - Eye colour, Hair colour & style, Skin tone
  - **No Clothes (Nude) toggle**
  - Extra details (tattoos, makeup, etc.)
  - Live prompt preview
- **Avatar List** — create, open, delete
- **Avatar Detail** — view all settings + prompt + Generate button (placeholder)
- Full navigation flow: List → Create / Detail → Edit
- Room persistence (survives app restart)
- Hilt + ViewModel + Jetpack Compose
- Dark / light theme with pink accent

## How to run

1. Clone: `git clone https://github.com/davealone69-gif/Aura-Studio-Avatar.git`
2. Open in Android Studio
3. Sync Gradle
4. Run on device or emulator

## Project layout

```
app/src/main/java/com/aura/studio/
├── AuraApp.kt
├── MainActivity.kt
├── avatar/AvatarSpec.kt
├── data/ (Room)
├── di/DatabaseModule.kt
└── ui/
    ├── AuraNavGraph.kt
    ├── AvatarListScreen.kt
    ├── AvatarCreatorScreen.kt
    ├── AvatarDetailScreen.kt
    ├── AvatarViewModel.kt
    └── theme/Theme.kt
```

## Status

Core avatar design + save/load loop is complete and usable.
Image generation button is present as a placeholder for the next stage.
