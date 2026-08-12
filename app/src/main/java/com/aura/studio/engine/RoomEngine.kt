package com.aura.studio.engine

import com.aura.studio.domain.room.RoomCatalog
import com.aura.studio.domain.room.RoomDefinition
import com.aura.studio.domain.room.RoomType
import com.aura.studio.domain.room.toPromptFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomEngine @Inject constructor() {
    private val customRooms = linkedMapOf<String, RoomDefinition>()
    fun listRooms(): List<RoomDefinition> = RoomCatalog.all() + customRooms.values.toList()
    fun get(id: String): RoomDefinition? = customRooms[id] ?: RoomCatalog.byId(id)
    fun byType(type: RoomType) = listRooms().filter { it.type == type }
    fun register(room: RoomDefinition) { customRooms[room.id] = room }
    fun scenePrompt(roomId: String, interactionId: String? = null): String =
        get(roomId)?.toPromptFragment(interactionId) ?: ""
    fun combineWithAvatarPrompt(avatarPrompt: String, roomId: String?, interactionId: String?): String {
        if (roomId.isNullOrBlank()) return avatarPrompt
        val scene = scenePrompt(roomId, interactionId)
        return if (scene.isBlank()) avatarPrompt else "$avatarPrompt, $scene"
    }
}
