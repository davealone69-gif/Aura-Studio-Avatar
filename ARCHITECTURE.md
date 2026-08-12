# Aura Studio — Ultimate Architecture

```
Compose UI → AIService → AvatarEngine | RoomEngine | MemoryEngine | RelationshipEngine | Local backends
```

## Data-driven rooms

```kotlin
RoomDefinition(
  type = RoomType.BEDROOM,
  environment = ...,
  lighting = ...,
  furniture = ...,
  ambience = ...,
  interactions = ...,
  actionPrompt = "..."
)
```

Add a room = one catalog entry. No new screen class.

## Engines

| Engine | Role |
|--------|------|
| AvatarEngine | Avatars, prompts, Dolphin |
| RoomEngine | Catalog + scene prompts |
| MemoryEngine | History / favorites |
| RelationshipEngine | Affinity modifiers |
| AIService | Facade for UI |

Local-first. Optional future HTTP backend behind same facade.
