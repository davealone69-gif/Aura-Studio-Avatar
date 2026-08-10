# Aura Studio

Focused Android app for designing custom sexy AI model avatars and preparing them for image generation.

## Working features

- **Avatar Creator**
  - Name, Age (18–45 only)
  - Ethnicity, Body type, Breast size
  - Eye colour, Hair colour & style, Skin tone
  - **No Clothes (Nude) toggle**
  - Extra details field
  - Live prompt preview

- **Avatar List** — create, open, delete with nice empty state
- **Avatar Detail** — full summary + show/hide prompt
- **Generate screen** — shows final prompt, one-tap copy to clipboard, Generate button (placeholder ready for real API)
- Full navigation: List → Detail → Generate / Edit / Create
- Room persistence
- Hilt + ViewModel + Compose
- Dark / light theme

## Run it

```bash
git clone https://github.com/davealone69-gif/Aura-Studio-Avatar.git
```

Open in Android Studio → Sync → Run.

## Status

Core avatar design + save/load + prompt export is complete.
Image generation is intentionally left as a clean placeholder so you can plug in any model/API later without rewriting the UI.
