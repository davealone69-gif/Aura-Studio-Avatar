package com.aura.studio.generation

import com.aura.studio.model.LocalModel
import com.aura.studio.nativebridge.VideoBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface LocalVideoEngine {
    suspend fun isReady(): Boolean
    suspend fun load(model: LocalModel): Boolean
    suspend fun unload()
    suspend fun generate(
        prompt: String,
        negativePrompt: String = "",
        width: Int = 512,
        height: Int = 512,
        frames: Int = 24,
        fps: Int = 8,
        steps: Int = 20,
        seed: Long = -1L
    ): String?
}

class DiffusionVideoEngine : LocalVideoEngine {
    private var currentModel: LocalModel? = null
    private var loaded = false

    override suspend fun isReady(): Boolean = loaded && currentModel != null

    override suspend fun load(model: LocalModel): Boolean = withContext(Dispatchers.IO) {
        if (VideoBridge.isAvailable()) VideoBridge.loadSafe(model.path)
        currentModel = model
        loaded = true
        true
    }

    override suspend fun unload() = withContext(Dispatchers.IO) {
        VideoBridge.freeSafe()
        currentModel = null
        loaded = false
    }

    override suspend fun generate(
        prompt: String,
        negativePrompt: String,
        width: Int,
        height: Int,
        frames: Int,
        fps: Int,
        steps: Int,
        seed: Long
    ): String? = withContext(Dispatchers.IO) {
        if (!loaded) return@withContext null
        if (VideoBridge.isAvailable()) {
            return@withContext VideoBridge.txt2videoSafe(
                prompt, negativePrompt, width, height, frames, fps, steps, seed
            )
        }
        delay(600)
        null
    }
}
