package com.aura.studio.generation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.aura.studio.model.LocalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface LocalImageEngine {
    suspend fun isReady(): Boolean
    suspend fun load(model: LocalModel): Boolean
    suspend fun unload()
    suspend fun generate(
        prompt: String,
        negativePrompt: String = DEFAULT_NEGATIVE,
        width: Int = 512,
        height: Int = 768,
        steps: Int = 20,
        cfg: Float = 7f,
        seed: Long = -1L
    ): Bitmap?

    companion object {
        const val DEFAULT_NEGATIVE =
            "lowres, bad anatomy, bad hands, text, error, missing fingers, " +
            "extra digit, fewer digits, cropped, worst quality, low quality, " +
            "jpeg artifacts, signature, watermark, username, blurry"
    }
}

class DiffusionImageEngine : LocalImageEngine {
    private var currentModel: LocalModel? = null
    private var loaded = false
    private val nativeWired = false

    override suspend fun isReady(): Boolean = loaded && currentModel != null

    override suspend fun load(model: LocalModel): Boolean = withContext(Dispatchers.IO) {
        currentModel = model
        loaded = true
        true
    }

    override suspend fun unload() = withContext(Dispatchers.IO) {
        currentModel = null
        loaded = false
    }

    override suspend fun generate(
        prompt: String,
        negativePrompt: String,
        width: Int,
        height: Int,
        steps: Int,
        cfg: Float,
        seed: Long
    ): Bitmap? = withContext(Dispatchers.IO) {
        if (!loaded) return@withContext null
        if (nativeWired) return@withContext nativeGenerate(prompt, negativePrompt, width, height, steps, cfg, seed)
        delay(800)
        placeholderBitmap(width, height, prompt)
    }

    private fun nativeGenerate(
        prompt: String, negativePrompt: String, width: Int, height: Int,
        steps: Int, cfg: Float, seed: Long
    ): Bitmap? {
        error("Set nativeWired=true and implement SdCppBridge")
    }

    private fun placeholderBitmap(w: Int, h: Int, prompt: String): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.parseColor("#0B0F18"))
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#00F0FF")
            textSize = 28f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("LOCAL DIFFUSION", w / 2f, h / 2f - 40f, paint)
        paint.textSize = 18f
        paint.color = Color.parseColor("#8A9BB8")
        val preview = if (prompt.length > 60) prompt.take(57) + "…" else prompt
        canvas.drawText(preview, w / 2f, h / 2f + 10f, paint)
        canvas.drawText("Wire stable-diffusion.cpp / MNN for real output", w / 2f, h / 2f + 50f, paint)
        return bmp
    }

    companion object {
        const val DEFAULT_NEGATIVE = LocalImageEngine.DEFAULT_NEGATIVE
    }
}
