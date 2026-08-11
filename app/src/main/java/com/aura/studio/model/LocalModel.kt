package com.aura.studio.model

import java.util.UUID

/**
 * Local model registry entry.
 * User points the app at model files on device (GGUF preferred for Dolphin).
 */
data class LocalModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: ModelType,
    val path: String,
    val quant: String = "Q4_K_M",
    val isDefault: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class ModelType {
    LLM,
    IMAGE,
    VIDEO
}

object RecommendedModels {
    val DOLPHIN = "Dolphin 3.0 (Llama 3.1 8B) — primary uncensored"
    val HERMES = "Hermes 3 — creative / roleplay"
    val QWEN_UNCENSORED = "Qwen3 abliterated / uncensored"
    val FAST_ROUTER = "Qwen3 0.6B / SmolLM2 1.7B — optional router"
}
