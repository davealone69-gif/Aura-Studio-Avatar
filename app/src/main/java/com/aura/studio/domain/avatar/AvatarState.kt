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
    val personality: PersonalityTraits = PersonalityTraits(),
    val goals: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val habits: List<String> = emptyList(),
    val conversationStyle: String = "warm, direct",
    val lastEmotion: EmotionTag = EmotionTag.CALM,
    val updatedAt: Long = System.currentTimeMillis()
)

data class PersonalityTraits(
    val openness: Float = 0.5f,
    val playfulness: Float = 0.5f,
    val intensity: Float = 0.5f,
    val dominance: Float = 0.4f,
    val empathy: Float = 0.6f
)

enum class EmotionTag {
    HAPPY, CALM, CURIOUS, EXCITED, SHY, CONFIDENT,
    PLAYFUL, SAD, ANGRY, SURPRISED, TIRED, AROUSED, NEUTRAL
}

fun AvatarState.withEmotion(tag: EmotionTag): AvatarState = copy(
    lastEmotion = tag,
    mood = when (tag) {
        EmotionTag.HAPPY, EmotionTag.EXCITED, EmotionTag.PLAYFUL, EmotionTag.AROUSED -> (mood + 0.08f).coerceIn(0f, 1f)
        EmotionTag.SAD, EmotionTag.ANGRY, EmotionTag.TIRED -> (mood - 0.08f).coerceIn(0f, 1f)
        else -> mood
    },
    updatedAt = System.currentTimeMillis()
)

fun AvatarState.promptModifier(): String =
    "mood=${\"%.2f\".format(mood)}, energy=${\"%.2f\".format(energy)}, trust=${\"%.2f\".format(trust)}, affection=${\"%.2f\".format(affection)}, confidence=${\"%.2f\".format(confidence)}, level=$relationshipLevel, emotion=${lastEmotion.name.lowercase()}, style=$conversationStyle"
