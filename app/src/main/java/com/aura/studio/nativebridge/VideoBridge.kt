package com.aura.studio.nativebridge

object VideoBridge {
    private var libraryLoaded = false
    init {
        try { System.loadLibrary("aura_video"); libraryLoaded = true } catch (_: UnsatisfiedLinkError) { libraryLoaded = false }
    }
    fun isAvailable(): Boolean = libraryLoaded
    external fun load(path: String): Boolean
    external fun free()
    external fun txt2video(prompt: String, negativePrompt: String, width: Int, height: Int, frames: Int, fps: Int, steps: Int, seed: Long): String?
    fun loadSafe(path: String): Boolean {
        if (!libraryLoaded) return false
        return try { load(path) } catch (_: Exception) { false }
    }
    fun txt2videoSafe(prompt: String, negativePrompt: String = "", width: Int = 512, height: Int = 512, frames: Int = 24, fps: Int = 8, steps: Int = 20, seed: Long = -1L): String? {
        if (!libraryLoaded) return null
        return try { txt2video(prompt, negativePrompt, width, height, frames, fps, steps, seed) } catch (_: Exception) { null }
    }
    fun freeSafe() { if (libraryLoaded) try { free() } catch (_: Exception) { } }
}
