# Aura Studio - Avatar Creator (Lite)

Clean, lightweight avatar designer for the Aura Studio Android app.

## Features
- Detailed avatar creation (age 18+, body, breasts, ethnicity, eyes, hair, skin, clothing/nude)
- Live prompt generation
- Room persistence
- List + Create/Edit screens
- Hilt + ViewModel architecture

## Structure
```
app/src/main/java/com/aura/studio/
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
    ├── AvatarViewModel.kt
    ├── AvatarCreatorScreen.kt
    └── AvatarListScreen.kt
```

## Next steps
- Wire into main navigation
- Add real image generation
- Background Swarm / WorkManager integration
