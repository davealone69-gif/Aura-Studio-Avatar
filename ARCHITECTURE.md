# Aura Studio — Ultimate Architecture

```
Compose UI → ViewModels → GAME CORE
  AvatarEngine · AvatarStateEngine · RoomEngine · StoryEngine
  MemoryEngine · ConversationMemory · RelationshipEngine
  AnimationEngine · VoiceService · SystemMonitor
       → AIService / DolphinService → local native backends
```

Data-driven: RoomDefinition, InteractiveObject, AvatarState, StoryBeat, AvatarMemory.
Emotion pipeline: state → expression → animation → voice → ambience.
Admin: Settings → Developer / Admin.
