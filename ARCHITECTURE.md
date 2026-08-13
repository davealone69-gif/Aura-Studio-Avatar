# Aura Studio — Ultimate Architecture

```
Compose UI → GameCore / AIService / DolphinService
  AvatarStateEngine · RoomEngine · MemoryBrainEngine
  StoryEngine · AnimationEngine · EmotionSystem · VoiceService
  → Local backends (Dolphin / SD / Video) + Room DB + SystemMonitor
```

Data-driven: RoomDefinition, InteractiveObject, AvatarState, AvatarMemory, StoryState, AvatarAnimation.

AiChatService is provider-agnostic (LocalAiChatService = Dolphin).
