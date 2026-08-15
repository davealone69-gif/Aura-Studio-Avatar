package com.aura.studio.domain.emotion

import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.avatar.EmotionTag
import com.aura.studio.domain.avatar.resolveEmotion

object EmotionPipeline {
    fun present(state: AvatarState): EmotionPresentation {
        val emotion = state.resolveEmotion()
        val tag = EmotionTag.entries.firstOrNull { it.name == emotion.name } ?: EmotionTag.CALM
        return EmotionPresentation(
            emotion = tag,
            expression = emotion.name.lowercase(),
            animationId = "anim_${emotion.name.lowercase()}",
            voiceTone = "neutral",
            ambienceHint = "default"
        )
    }
}
