package com.aura.studio.domain.memory

import java.util.UUID

data class AvatarMemory(
    val id: String = UUID.randomUUID().toString(),
    val avatarId: String,
    val summary: String,
    val importance: Float = 0.5f,
    val roomId: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

object MemoryCompressor {
    fun selectForContext(memories: List<AvatarMemory>, limit: Int = 8, roomId: String? = null): List<AvatarMemory> {
        return memories.map { m ->
            var score = m.importance
            if (roomId != null && m.roomId == roomId) score += 0.2f
            val ageDays = (System.currentTimeMillis() - m.createdAt) / 86_400_000f
            score += (1f / (1f + ageDays)).coerceAtMost(0.3f)
            m to score
        }.sortedByDescending { it.second }.take(limit).map { it.first }
    }

    fun toPromptBlock(memories: List<AvatarMemory>): String =
        if (memories.isEmpty()) "" else memories.joinToString("\n") { "- ${it.summary}" }
}
