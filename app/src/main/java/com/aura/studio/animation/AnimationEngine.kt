package com.aura.studio.animation

import com.aura.studio.domain.avatar.Emotion

data class AnimationPlayback(
    val animationId: String,
    val emotion: Emotion? = null
)

interface AnimationEngine {
    fun play(name: String)
    fun stop()
    fun playEmotion(avatarId: String, emotion: Emotion): AnimationPlayback
    fun current(avatarId: String): AnimationPlayback
}
