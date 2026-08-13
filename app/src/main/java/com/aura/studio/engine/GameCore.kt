package com.aura.studio.engine

import com.aura.studio.ai.AiChatService
import com.aura.studio.ai.AvatarResponse
import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.emotion.EmotionPresentation
import com.aura.studio.domain.room.InteractiveObject
import com.aura.studio.domain.room.RoomDefinition
import com.aura.studio.domain.room.RoomObjects
import com.aura.studio.domain.story.StoryState
import javax.inject.Inject
import javax.inject.Singleton

data class GameTurnResult(
    val state: AvatarState,
    val emotion: EmotionPresentation,
    val response: AvatarResponse,
    val story: StoryState,
    val animationId: String
)

@Singleton
class GameCore @Inject constructor(
    private val roomEngine: RoomEngine,
    private val avatarState: AvatarStateEngine,
    private val memory: MemoryBrainEngine,
    private val story: StoryEngine,
    private val animation: AnimationEngine,
    private val relationship: RelationshipEngine,
    private val chat: AiChatService
) {
    fun rooms(): List<RoomDefinition> = roomEngine.listRooms()
    fun objectsIn(roomId: String): List<InteractiveObject> = RoomObjects.forRoom(roomId)
    fun state(avatarId: String): AvatarState = avatarState.get(avatarId)
    fun story(avatarId: String): StoryState = story.get(avatarId)

    fun enterRoom(avatarId: String, roomId: String): AvatarState {
        memory.rememberEvent(avatarId, "Entered room $roomId", roomId, 0.4f)
        return avatarState.moveToRoom(avatarId, roomId)
    }

    suspend fun talk(avatarId: String, message: String): GameTurnResult {
        val st = avatarState.get(avatarId)
        val room = st.currentRoomId?.let { roomEngine.get(it) }
        val response = chat.chat(st, room, memory.all(avatarId), message)
        val (nextState, emotion) = avatarState.onInteraction(avatarId, message + " " + response.text)
        val anim = animation.playEmotion(avatarId, nextState.lastEmotion)
        relationship.boostAffinity(avatarId, 0.03f)
        memory.rememberEvent(avatarId, "User: ${message.take(80)} | Avatar: ${response.text.take(80)}", st.currentRoomId, 0.55f)
        val storyState = story.advanceIfReady(avatarId, nextState.relationshipLevel)
        return GameTurnResult(nextState, emotion, response, storyState, anim.animationId)
    }

    fun interactObject(avatarId: String, objectId: String): AvatarState {
        val st = avatarState.get(avatarId)
        val roomId = st.currentRoomId ?: return st
        val obj = RoomObjects.forRoom(roomId).find { it.id == objectId } ?: return st
        memory.rememberEvent(avatarId, "Used ${obj.name}: ${obj.interactionPrompt}", roomId, 0.45f)
        return avatarState.onInteraction(avatarId, obj.interactionPrompt).first
    }

    fun debugSnapshot(avatarId: String): Map<String, String> {
        val st = avatarState.get(avatarId)
        return mapOf(
            "avatarId" to avatarId,
            "roomId" to (st.currentRoomId ?: "none"),
            "mood" to "%.2f".format(st.mood),
            "energy" to "%.2f".format(st.energy),
            "trust" to "%.2f".format(st.trust),
            "affection" to "%.2f".format(st.affection),
            "relationshipLevel" to st.relationshipLevel.toString(),
            "emotion" to st.lastEmotion.name,
            "memoryCount" to memory.count(avatarId).toString(),
            "storyChapter" to story.get(avatarId).chapter.toString(),
            "animation" to animation.current(avatarId).animationId
        )
    }

    fun resetBrain(avatarId: String) {
        memory.clear(avatarId)
        avatarState.update(avatarId) { AvatarState(avatarId = avatarId) }
        story.set(avatarId, StoryState())
    }
}
