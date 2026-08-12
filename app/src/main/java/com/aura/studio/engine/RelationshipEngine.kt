package com.aura.studio.engine

import javax.inject.Inject
import javax.inject.Singleton

data class RelationshipState(
    val avatarId: String,
    val affinity: Float = 0.5f,
    val trust: Float = 0.5f,
    val intensity: Float = 0.5f,
    val lastTone: String = "neutral",
    val notes: String = ""
)

@Singleton
class RelationshipEngine @Inject constructor() {
    private val states = mutableMapOf<String, RelationshipState>()
    fun get(avatarId: String) = states.getOrPut(avatarId) { RelationshipState(avatarId) }
    fun update(avatarId: String, transform: (RelationshipState) -> RelationshipState) {
        states[avatarId] = transform(get(avatarId))
    }
    fun boostAffinity(avatarId: String, delta: Float = 0.05f) {
        update(avatarId) { it.copy(affinity = (it.affinity + delta).coerceIn(0f, 1f)) }
    }
    fun promptModifier(avatarId: String): String {
        val s = get(avatarId)
        return when {
            s.affinity > 0.75f -> "familiar, intimate connection"
            s.affinity > 0.4f -> "comfortable chemistry"
            else -> "first encounters, exploratory"
        }
    }
}
