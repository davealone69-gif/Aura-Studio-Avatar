package com.aura.studio.animation

import android.util.Log

class LogAnimationEngine : AnimationEngine {
    override fun play(name: String) {
        Log.d("AnimationEngine", "play: $name")
    }

    override fun stop() {
        Log.d("AnimationEngine", "stop")
    }
}
