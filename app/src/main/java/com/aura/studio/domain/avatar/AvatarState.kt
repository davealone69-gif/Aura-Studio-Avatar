package com.aura.studio.domain.avatar

data class AvatarState(
    val avatarId: String,
    val mood: Float = 0.5f,
    val energy: Float = 1.0f,
    val trust: Float = 0.0f,
    val affection: Float = 0.0f,
    val confidence: Float = 0.5f,
    val relationshipLevel: Int = 0,
    val currentRoomId: String? = null,
    val personality: PersonalityProfile = PersonalityProfile(),
    val emotion: Emotion = Emotion.CALM,
    val goals: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val habits: List<String> = emptyList(),
    val conversationStyle: String = "warm, direct",
    val lastEventSummary: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class PersonalityProfile(
    val openness: Float = 0.5f,
    val playfulness: Float = 0.5f,
    val intensity: Float = 0.5f,
    val warmth: Float = 0.6f,
    val dominance: Float = 0.4f,
    val tags: List<String> = listOf("curious", "affectionate")
)

enum class Emotion {
    HAPPY, CALM, CURIOUS, EXCITED, SHY, CONFIDENT,
    PLAYFUL, SAD, ANGRY, SURPRISED, TIRED, AROUSED, NEUTRAL
}

fun AvatarState.resolveEmotion(): Emotion = when {
    energy < 0.25f -> Emotion.TIRED
    mood > 0.75f && affection > 0.5f -> Emotion.HAPPY
    mood > 0.6f && personality.playfulness > 0.6f -> Emotion.PLAYFUL
    confidence > 0.7f -> Emotion.CONFIDENT
    trust < 0.2f && affection < 0.3f -> Emotion.SHY
    mood < 0.3f -> Emotion.SAD
    energy > 0.8f && mood > 0.6f -> Emotion.EXCITED
    else -> emotion
}

fun AvatarState.toPromptModifier(): String = buildString {
    append("emotion: ${resolveEmotion().name.lowercase()}")
    append(", mood ${"%.0f".format(mood * 100)}%")
    append(", energy ${"%.0f".format(energy * 100)}%")
    if (affection > 0.5f) append(", affectionate")
    if (trust > 0.6f) append(", trusting")
    if (conversationStyle.isNotBlank()) append(", tone: $conversationStyle")
    if (lastEventSummary.isNotBlank()) append(", recent: $lastEventSummary")
}
