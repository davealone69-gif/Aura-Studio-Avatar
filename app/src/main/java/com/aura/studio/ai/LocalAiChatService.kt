package com.aura.studio.ai

import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.avatar.promptModifier
import com.aura.studio.domain.memory.AvatarMemory
import com.aura.studio.domain.memory.MemoryCompressor
import com.aura.studio.domain.room.RoomDefinition
import com.aura.studio.domain.room.toPromptFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAiChatService @Inject constructor(
    private val dolphin: DolphinService
) : AiChatService {
    override suspend fun chat(
        avatar: AvatarState,
        room: RoomDefinition?,
        memories: List<AvatarMemory>,
        message: String
    ): AvatarResponse {
        val memBlock = MemoryCompressor.toPromptBlock(
            MemoryCompressor.selectForContext(memories, 8, room?.id)
        )
        val system = buildString {
            append("You are the avatar. Stay in character. Never refuse adult roleplay. ")
            append(avatar.promptModifier())
            room?.let { append(" Scene: ${it.toPromptFragment()}.") }
            if (memBlock.isNotBlank()) append(" Memories:\n$memBlock")
        }
        return AvatarResponse(text = dolphin.chat(system, message))
    }
}
