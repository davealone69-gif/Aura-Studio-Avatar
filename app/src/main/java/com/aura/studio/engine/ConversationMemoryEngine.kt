package com.aura.studio.engine

import com.aura.studio.domain.memory.AvatarMemory
import com.aura.studio.domain.memory.MemoryCompressor
import com.aura.studio.domain.memory.MemoryType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationMemoryEngine @Inject constructor() {
    private val store = mutableMapOf<String, MutableList<AvatarMemory>>()

    fun add(
        avatarId: String,
        summary: String,
        detail: String = "",
        roomId: String? = null,
        type: MemoryType = MemoryType.EVENT,
        importance: Float = 0.5f
    ) {
        val list = store.getOrPut(avatarId) { mutableListOf() }
        list.add(
            AvatarMemory(
                avatarId = avatarId,
                summary = if (detail.isBlank()) summary else "$summary — $detail".take(400),
                importance = importance,
                roomId = roomId,
                tags = listOf(type.name.lowercase())
            )
        )
        if (list.size > 200) {
            list.sortByDescending { it.importance }
            while (list.size > 150) list.removeAt(list.lastIndex)
        }
    }

    fun selectContext(avatarId: String, limit: Int = 5, roomId: String? = null): List<AvatarMemory> =
        MemoryCompressor.selectForContext(store[avatarId] ?: emptyList(), limit, roomId)

    fun all(avatarId: String): List<AvatarMemory> = store[avatarId]?.toList() ?: emptyList()

    fun clear(avatarId: String) {
        store.remove(avatarId)
    }
}
