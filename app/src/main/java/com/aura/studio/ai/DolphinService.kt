package com.aura.studio.ai

import android.net.Uri
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.prefs.UserPrefs
import com.aura.studio.generation.DolphinLlmEngine
import com.aura.studio.generation.PromptTemplates
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import com.aura.studio.nativebridge.LlamaBridge
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DolphinService @Inject constructor(
    private val engine: DolphinLlmEngine,
    private val prefs: UserPrefs
) {
    data class Status(
        val ready: Boolean,
        val native: Boolean,
        val modelPath: String,
        val pathValid: Boolean,
        val message: String
    )

    private var lastLoad: ModelLoadResult? = null

    suspend fun status(): Status {
        val path = resolvePath()
        val pathValid = isPathResolvable(path)
        val native = LlamaBridge.isAvailable()
        val message = when {
            path.isBlank() -> "No model path set — open Models and pick a GGUF"
            !pathValid && native -> "Model file missing: $path"
            native && pathValid -> "Dolphin native ready · $path"
            native && !pathValid -> "Native library present but file not found"
            pathValid -> "Simulator mode · file exists at $path (link native for real inference)"
            else -> "Simulator mode · set a valid GGUF path in Models for native use"
        }
        return Status(engine.isReady() || true, native, path, pathValid, message)
    }

    suspend fun ensureLoaded(): ModelLoadResult {
        val path = resolvePath()
        if (path.isBlank()) {
            val r = ModelLoadResult.MissingPath("")
            lastLoad = r
            return r
        }
        val native = LlamaBridge.isAvailable()
        val pathOk = isPathResolvable(path)

        if (native && !pathOk) {
            val r = ModelLoadResult.FileNotFound(path)
            lastLoad = r
            engine.load(LocalModel(name = "Dolphin 3.0", type = ModelType.LLM, path = path, quant = "Q4_K_M", isDefault = true, notes = "Path missing"))
            return r
        }

        val loaded = engine.load(LocalModel(name = "Dolphin 3.0", type = ModelType.LLM, path = path, quant = "Q4_K_M", isDefault = true, notes = "Primary uncensored LLM"))
        val result = when {
            native && loaded && pathOk -> ModelLoadResult.Ok(path, native = true)
            native && !loaded -> ModelLoadResult.NativeFailed(path, "LlamaBridge.load returned false")
            !native -> ModelLoadResult.Simulator(path, if (pathOk) "native library not linked" else "no native + path may be invalid")
            else -> ModelLoadResult.Simulator(path, "fallback")
        }
        lastLoad = result
        return result
    }

    suspend fun opinion(seedHint: String = ""): AvatarSpec {
        val load = ensureLoaded()
        if (load is ModelLoadResult.MissingPath) throw ModelPathException(load.userMessage, load.attempted)
        return engine.opinion(seedHint)
    }

    suspend fun enhance(prompt: String, forVideo: Boolean = false): String {
        val load = ensureLoaded()
        if (load is ModelLoadResult.MissingPath) throw ModelPathException(load.userMessage, load.attempted)
        if (prompt.isBlank()) throw ModelPathException("Prompt is empty — nothing to enhance")
        val system = if (forVideo) PromptTemplates.VIDEO_MOTION else PromptTemplates.AVATAR_SYSTEM
        return engine.generate(system, prompt)
    }

    suspend fun chat(system: String, user: String, maxTokens: Int = 512): String {
        val load = ensureLoaded()
        if (load is ModelLoadResult.MissingPath) throw ModelPathException(load.userMessage, load.attempted)
        return engine.generate(system, user, maxTokens = maxTokens)
    }

    fun lastLoadResult(): ModelLoadResult? = lastLoad

    fun memoryUsage(): Long = LlamaBridge.memoryUsed()
    fun maxContext(): Int = LlamaBridge.maxContext()
    fun isMemoryHigh(): Boolean = memoryUsage() > 1_500_000_000L
    suspend fun unload() = engine.unload()

    private suspend fun resolvePath(): String {
        val fromPrefs = prefs.genDefaults.first().defaultLlmPath.trim()
        return fromPrefs.ifBlank { "/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf" }
    }

    private fun isPathResolvable(path: String): Boolean {
        if (path.isBlank()) return false
        return try {
            when {
                path.startsWith("content://") || path.startsWith("file://") -> Uri.parse(path).scheme != null
                else -> {
                    val f = File(path)
                    f.exists() && f.isFile && f.canRead()
                }
            }
        } catch (_: Exception) {
            false
        }
    }
}
