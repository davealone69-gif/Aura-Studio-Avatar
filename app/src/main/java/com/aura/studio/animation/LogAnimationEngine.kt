package com.aura.studio.animation

import android.util.Log
import com.aura.studio.domain.avatar.Emotion

class LogAnimationEngine : AnimationEngine {
    private val currentByAvatar = mutableMapOf<String, AnimationPlayback>()

    override fun play(name: String) {
        Log.d("AnimationEngine", "play: $name")
    }

    override fun stop() {
        Log.d("AnimationEngine", "stop")
        currentByAvatar.clear()
    }

    override fun playEmotion(avatarId: String, emotion: Emotion): AnimationPlayback {
        val playback = AnimationPlayback(
            animationId = "anim_${emotion.name.lowercase()}",
            emotion = emotion
        )
        currentByAvatar[avatarId] = playback
        Log.d("AnimationEngine", "playEmotion avatar=$avatarId emotion=$emotion -> ${playback.animationId}")
        return playback
    }

    override fun current(avatarId: String): AnimationPlayback =
        currentByAvatar[avatarId] ?: AnimationPlayback("anim_idle")
}
