package com.aura.studio.nativebridge

object LlamaBridge {
    private var libraryLoaded = false

    init {
        try {
            System.loadLibrary("aura_llama")
            libraryLoaded = true
        } catch (_: UnsatisfiedLinkError) {
            libraryLoaded = false
        }
    }

    fun isAvailable(): Boolean = libraryLoaded

    external fun load(path: String, nCtx: Int, nGpuLayers: Int): Boolean
    external fun free()
    external fun chat(systemPrompt: String, userPrompt: String, maxTokens: Int, temperature: Float): String
    external fun nativeIsLoaded(): Boolean
    external fun nativeGetMemoryUsed(): Long
    external fun nativeGetMaxContext(): Int

    fun loadSafe(path: String, nCtx: Int = 2048, nGpuLayers: Int = 0): Boolean {
        if (!libraryLoaded || path.isBlank()) return false
        return try { load(path, nCtx, nGpuLayers) } catch (_: Exception) { false }
    }

    fun chatSafe(systemPrompt: String, userPrompt: String, maxTokens: Int = 512, temperature: Float = 0.75f): String? {
        if (!libraryLoaded) return null
        return try { chat(systemPrompt, userPrompt, maxTokens, temperature) } catch (_: Exception) { null }
    }

    fun freeSafe() {
        if (!libraryLoaded) return
        try { free() } catch (_: Exception) {}
    }

    fun isModelLoaded(): Boolean =
        if (!libraryLoaded) false else try { nativeIsLoaded() } catch (_: Exception) { false }

    fun memoryUsed(): Long =
        if (!libraryLoaded) 0L else try { nativeGetMemoryUsed() } catch (_: Exception) { 0L }

    fun maxContext(): Int =
        if (!libraryLoaded) 0 else try { nativeGetMaxContext() } catch (_: Exception) { 0 }
}
