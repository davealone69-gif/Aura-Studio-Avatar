package com.aura.studio.domain.room

data class InteractiveObject(
    val id: String,
    val name: String,
    val promptHint: String,
    val interactionPrompt: String = "",
    val enabled: Boolean = true,
    val unlockFlag: String? = null
)
