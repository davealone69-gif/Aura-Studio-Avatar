package com.aura.studio.engine

import com.aura.studio.domain.memory.AvatarMemory
import javax.inject.Inject
import javax.inject.Singleton

/** In-memory event brain used by GameCore (distinct from gallery MemoryEngine). */
@Singleton
class MemoryBrainEngine @Inject constructor() {
    private val store = mutableMapOf<String, MutableList<AvatarMemory>>()

    fun rememberEvent(avatarId: String, summary: String, roomId: String?, importance: Float) {
        val list = store.getOrPut(avatarId) { mutableListOf() }
        list.add(
            AvatarMemory(
                avatarId = avatarId,
                summary = summary.take(240),
                importance = importance,
                roomId = roomId
            )
        )
        if (list.size > 200) {
            list.sortByDescending { it.importance }
            while (list.size > 150) list.removeAt(list.lastIndex)
        }
    }

    fun all(avatarId: String): List<AvatarMemory> =
        store[avatarId]?.toList() ?: emptyList()

    fun count(avatarId: String): Int = store[avatarId]?.size ?: 0

    fun clear(avatarId: String) {
        store.remove(avatarId)
    }
}
