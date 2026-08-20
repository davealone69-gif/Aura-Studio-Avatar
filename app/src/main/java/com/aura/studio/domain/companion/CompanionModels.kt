package com.aura.studio.domain.companion

import java.util.UUID

/**
 * Consolidated companion models adopted from the useful parts of Truth-time.
 * Aura Studio remains the canonical domain model and owns persistence/UI.
 */
enum class MessageSender { USER, AVATAR }

data class CompanionMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val personaId: String,
    val imageUri: String? = null,
    val videoUri: String? = null,
)

data class CompanionPersona(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val styleVibe: String,
    val defaultGreeting: String,
    val primaryColorArgb: Long,
)

enum class CameraMotion(val displayName: String) {
    PAN("Pan Right"),
    TILT("Tilt Up"),
    ZOOM("Slow Zoom In"),
    DOLLY("Dolly Tracking"),
    STATIC("Cinematic Static"),
}

data class StudioScene(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val prompt: String,
    val durationSeconds: Int = 10,
    val cameraMotion: CameraMotion = CameraMotion.ZOOM,
    val characterId: String,
)

enum class MediaType { IMAGE, VIDEO }

data class GeneratedMedia(
    val id: String = UUID.randomUUID().toString(),
    val type: MediaType,
    val localUri: String,
    val createdAt: Long = System.currentTimeMillis(),
    val prompt: String = "",
    val personaId: String? = null,
    val sceneId: String? = null,
)
