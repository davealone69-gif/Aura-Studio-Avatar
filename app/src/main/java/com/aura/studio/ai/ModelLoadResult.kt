package com.aura.studio.ai

sealed class ModelLoadResult {
    data class Ok(val path: String, val native: Boolean) : ModelLoadResult()
    data class MissingPath(val attempted: String) : ModelLoadResult() {
        val userMessage: String = "No model path set — open Models and pick a GGUF"
    }
    data class FileNotFound(val path: String) : ModelLoadResult()
    data class NativeFailed(val path: String, val reason: String) : ModelLoadResult()
    data class Simulator(val path: String, val reason: String) : ModelLoadResult()
}
