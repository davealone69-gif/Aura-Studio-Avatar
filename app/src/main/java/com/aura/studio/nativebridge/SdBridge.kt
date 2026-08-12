package com.aura.studio.nativebridge

import android.graphics.Bitmap

object SdBridge {
    private var libraryLoaded = false
    init {
        try { System.loadLibrary("aura_sd"); libraryLoaded = true } catch (_: UnsatisfiedLinkError) { libraryLoaded = false }
    }
    fun isAvailable(): Boolean = libraryLoaded
    external fun load(path: String): Boolean
    external fun free()
    external fun txt2img(prompt: String, negativePrompt: String, width: Int, height: Int, steps: Int, cfg: Float, seed: Long): IntArray?
    fun loadSafe(path: String): Boolean {
        if (!libraryLoaded) return false
        return try { load(path) } catch (_: Exception) { false }
    }
    fun txt2imgBitmap(prompt: String, negativePrompt: String, width: Int, height: Int, steps: Int, cfg: Float, seed: Long): Bitmap? {
        if (!libraryLoaded) return null
        return try {
            val pixels = txt2img(prompt, negativePrompt, width, height, steps, cfg, seed) ?: return null
            Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
        } catch (_: Exception) { null }
    }
    fun freeSafe() { if (libraryLoaded) try { free() } catch (_: Exception) { } }
}
