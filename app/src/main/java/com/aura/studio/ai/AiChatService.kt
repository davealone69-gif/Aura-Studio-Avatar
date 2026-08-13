package com.aura.studio.ai

import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.memory.AvatarMemory
import com.aura.studio.domain.room.RoomDefinition

data class AvatarResponse(val text: String, val emotionHint: String? = null)

interface AiChatService {
    suspend fun chat(
        avatar: AvatarState,
        room: RoomDefinition?,
        memories: List<AvatarMemory>,
        message: String
    ): AvatarResponse
}
