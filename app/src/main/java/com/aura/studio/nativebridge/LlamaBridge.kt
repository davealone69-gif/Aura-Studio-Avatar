package com.aura.studio.nativebridge

object LlamaBridge {
    private var libraryLoaded = false
    init {
        try { System.loadLibrary("aura_llama"); libraryLoaded = true } catch (_: UnsatisfiedLinkError) { libraryLoaded = false }
    }
    fun isAvailable(): Boolean = libraryLoaded
    external fun load(path: String, nCtx: Int, nGpuLayers: Int): Boolean
    external fun free()
    external fun chat(systemPrompt: String, userPrompt: String, maxTokens: Int, temperature: Float): String
    fun loadSafe(path: String, nCtx: Int = 4096, nGpuLayers: Int = 0): Boolean {
        if (!libraryLoaded) return false
        return try { load(path, nCtx, nGpuLayers) } catch (_: Exception) { false }
    }
    fun chatSafe(systemPrompt: String, userPrompt: String, maxTokens: Int = 512, temperature: Float = 0.75f): String? {
        if (!libraryLoaded) return null
        return try { chat(systemPrompt, userPrompt, maxTokens, temperature) } catch (_: Exception) { null }
    }
    fun freeSafe() { if (libraryLoaded) try { free() } catch (_: Exception) { } }
}
