package com.aura.studio.engine

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelationshipEngine @Inject constructor() {
    private val affinity = mutableMapOf<String, Float>()

    fun getAffinity(avatarId: String): Float = affinity[avatarId] ?: 0f

    fun boostAffinity(avatarId: String, delta: Float) {
        val next = ((affinity[avatarId] ?: 0f) + delta).coerceIn(0f, 1f)
        affinity[avatarId] = next
    }

    fun setAffinity(avatarId: String, value: Float) {
        affinity[avatarId] = value.coerceIn(0f, 1f)
    }

    fun reset(avatarId: String) {
        affinity.remove(avatarId)
    }
}
