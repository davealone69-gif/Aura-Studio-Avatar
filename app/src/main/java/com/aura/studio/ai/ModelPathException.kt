package com.aura.studio.ai

class ModelPathException(
    message: String,
    val path: String = "",
    cause: Throwable? = null
) : Exception(message, cause)

sealed class ModelLoadResult {
    data class Ok(val path: String, val native: Boolean) : ModelLoadResult()
    data class MissingPath(val attempted: String) : ModelLoadResult()
    data class FileNotFound(val path: String) : ModelLoadResult()
    data class NativeFailed(val path: String, val detail: String) : ModelLoadResult()
    data class Simulator(val path: String, val reason: String) : ModelLoadResult()

    val isUsable: Boolean
        get() = this is Ok || this is Simulator

    val userMessage: String
        get() = when (this) {
            is Ok -> "Model loaded: $path"
            is MissingPath -> "No model path set. Open Models and pick a Dolphin GGUF."
            is FileNotFound -> "Model file not found:\n$path\nPlace the GGUF or pick it in Models."
            is NativeFailed -> "Native load failed for $path — $detail"
            is Simulator -> "Using simulator ($reason). Path: $path"
        }
}
