package com.aura.studio.engine

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelationshipEngine @Inject constructor() {
    private val affinity = mutableMapOf<String, Float>()

    fun getAffinity(avatarId: String): Float = affinity[avatarId] ?: 0f

    fun boostAffinity(avatarId: String, delta: Float = 0.03f) {
        val next = ((affinity[avatarId] ?: 0f) + delta).coerceIn(0f, 1f)
        affinity[avatarId] = next
    }

    fun setAffinity(avatarId: String, value: Float) {
        affinity[avatarId] = value.coerceIn(0f, 1f)
    }

    fun reset(avatarId: String) {
        affinity.remove(avatarId)
    }

    fun promptModifier(avatarId: String): String {
        val a = getAffinity(avatarId)
        return when {
            a > 0.75f -> "close bond, high trust"
            a > 0.4f -> "friendly rapport"
            a > 0.15f -> "warming up"
            else -> ""
        }
    }
}
