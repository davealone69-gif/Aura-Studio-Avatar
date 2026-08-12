package com.aura.studio.ai

import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.prefs.UserPrefs
import com.aura.studio.generation.DolphinLlmEngine
import com.aura.studio.generation.PromptTemplates
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import com.aura.studio.nativebridge.LlamaBridge
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single entry for Dolphin 3.0 (uncensored local LLM).
 * Native via LlamaBridge when .so present; else high-quality simulator.
 */
@Singleton
class DolphinService @Inject constructor(
    private val engine: DolphinLlmEngine,
    private val prefs: UserPrefs
) {
    data class Status(val ready: Boolean, val native: Boolean, val modelPath: String, val message: String)

    suspend fun status(): Status {
        val path = resolvePath()
        val native = LlamaBridge.isAvailable()
        return Status(true, native, path,
            if (native) "Dolphin native (llama.cpp) active"
            else "Dolphin simulator — link native for real GGUF inference")
    }

    suspend fun ensureLoaded(): Boolean {
        val path = resolvePath()
        return engine.load(
            LocalModel(
                name = "Dolphin 3.0",
                type = ModelType.LLM,
                path = path,
                quant = "Q4_K_M",
                isDefault = true,
                notes = "Primary uncensored LLM"
            )
        )
    }

    suspend fun opinion(seedHint: String = ""): AvatarSpec {
        ensureLoaded()
        return engine.opinion(seedHint)
    }

    suspend fun enhance(prompt: String, forVideo: Boolean = false): String {
        ensureLoaded()
        val system = if (forVideo) PromptTemplates.VIDEO_MOTION else PromptTemplates.AVATAR_SYSTEM
        return engine.generate(system, prompt)
    }

    suspend fun chat(system: String, user: String, maxTokens: Int = 512): String {
        ensureLoaded()
        return engine.generate(system, user, maxTokens = maxTokens)
    }

    private suspend fun resolvePath(): String {
        val fromPrefs = prefs.genDefaults.first().defaultLlmPath
        return fromPrefs.ifBlank { "/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf" }
    }
}
