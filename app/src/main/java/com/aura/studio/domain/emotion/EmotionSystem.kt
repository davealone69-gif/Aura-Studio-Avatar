package com.aura.studio.domain.emotion

import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.avatar.EmotionTag
import com.aura.studio.domain.avatar.withEmotion
import javax.inject.Inject
import javax.inject.Singleton

data class EmotionPresentation(
    val emotion: EmotionTag,
    val expression: String,
    val animationId: String,
    val voiceTone: String,
    val ambienceHint: String
)

@Singleton
class EmotionSystem @Inject constructor() {
    fun inferFromText(text: String, state: AvatarState): EmotionTag {
        val t = text.lowercase()
        return when {
            listOf("love", "kiss", "want you").any { it in t } -> EmotionTag.AROUSED
            listOf("haha", "funny", "play").any { it in t } -> EmotionTag.PLAYFUL
            listOf("tired", "sleep").any { it in t } -> EmotionTag.TIRED
            listOf("sorry", "sad").any { it in t } -> EmotionTag.SAD
            listOf("angry", "hate").any { it in t } -> EmotionTag.ANGRY
            listOf("happy", "glad").any { it in t } -> EmotionTag.HAPPY
            state.energy < 0.25f -> EmotionTag.TIRED
            state.confidence > 0.7f -> EmotionTag.CONFIDENT
            else -> EmotionTag.CALM
        }
    }

    fun present(emotion: EmotionTag): EmotionPresentation = when (emotion) {
        EmotionTag.HAPPY -> EmotionPresentation(emotion, "soft smile", "anim_smile", "warm", "brighter")
        EmotionTag.PLAYFUL -> EmotionPresentation(emotion, "smirk", "anim_tease", "teasing", "color accents")
        EmotionTag.AROUSED -> EmotionPresentation(emotion, "parted lips", "anim_intimate", "breathy", "amber")
        EmotionTag.SHY -> EmotionPresentation(emotion, "glance away", "anim_shy", "quiet", "dim")
        EmotionTag.TIRED -> EmotionPresentation(emotion, "heavy lids", "anim_tired", "slow", "low warm")
        else -> EmotionPresentation(emotion, "neutral", "anim_idle", "neutral", "default")
    }

    fun apply(state: AvatarState, text: String): Pair<AvatarState, EmotionPresentation> {
        val tag = inferFromText(text, state)
        return state.withEmotion(tag) to present(tag)
    }
}
