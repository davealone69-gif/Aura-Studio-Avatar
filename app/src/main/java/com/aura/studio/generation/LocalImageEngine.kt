package com.aura.studio.generation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.aura.studio.model.LocalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface LocalImageEngine {
    suspend fun isReady(): Boolean
    suspend fun load(model: LocalModel): Boolean
    suspend fun unload()
    suspend fun generate(
        prompt: String,
        negativePrompt: String = DEFAULT_NEGATIVE,
        width: Int = 256,
        height: Int = 256,
        steps: Int = 8,
        cfg: Float = 7f,
        seed: Long = -1L
    ): Bitmap?

    companion object {
        const val DEFAULT_NEGATIVE =
            "lowres, bad anatomy, bad hands, text, error, missing fingers, " +
            "extra digit, fewer digits, cropped, worst quality, low quality, " +
            "jpeg artifacts, signature, watermark, username, blurry"
        const val DEFAULT_SERVER_URL = "http://127.0.0.1:1234"
    }
}

class DiffusionImageEngine : LocalImageEngine {
    private var currentModel: LocalModel? = null
    private var loaded = false
    private var serverUrl = LocalImageEngine.DEFAULT_SERVER_URL

    override suspend fun isReady(): Boolean = loaded && currentModel != null

    override suspend fun load(model: LocalModel): Boolean = withContext(Dispatchers.IO) {
        currentModel = model
        serverUrl = LocalImageEngine.DEFAULT_SERVER_URL
        loaded = runCatching {
            val connection = URL("$serverUrl/").openConnection() as HttpURLConnection
            connection.connectTimeout = 1500
            connection.readTimeout = 1500
            connection.requestMethod = "GET"
            connection.responseCode in 200..499
        }.getOrDefault(false)
        loaded
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
        if (!loaded || prompt.isBlank()) return@withContext null

        val payload = JSONObject().apply {
            put("prompt", prompt)
            put("negative_prompt", negativePrompt)
            put("width", width.coerceIn(64, 512))
            put("height", height.coerceIn(64, 512))
            put("steps", steps.coerceIn(1, 12))
            put("cfg_scale", cfg)
            if (seed >= 0) put("seed", seed)
        }.toString()

        val connection = (URL("$serverUrl/sdapi/v1/txt2img").openConnection() as HttpURLConnection).apply {
            connectTimeout = 5000
            readTimeout = 120000
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
        }

        try {
            connection.outputStream.use { it.write(payload.toByteArray(Charsets.UTF_8)) }
            if (connection.responseCode !in 200..299) return@withContext null

            val body = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8)).use { it.readText() }
            val image = JSONObject(body).optJSONArray("images")?.optString(0).orEmpty()
            if (image.isBlank()) return@withContext null

            val clean = image.substringAfter("base64,", image)
            Base64.decode(clean, Base64.DEFAULT).let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        } finally {
            connection.disconnect()
        }
    }
}
