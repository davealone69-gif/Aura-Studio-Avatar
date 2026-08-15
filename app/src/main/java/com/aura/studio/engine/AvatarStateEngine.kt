package com.aura.studio.engine

import com.aura.studio.domain.avatar.AvatarState
import com.aura.studio.domain.avatar.Emotion
import com.aura.studio.domain.avatar.EmotionTag
import com.aura.studio.domain.avatar.resolveEmotion
import com.aura.studio.domain.avatar.toPromptModifier
import com.aura.studio.domain.emotion.EmotionPresentation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarStateEngine @Inject constructor() {
    private val states = mutableMapOf<String, AvatarState>()

    fun get(avatarId: String) = states.getOrPut(avatarId) { AvatarState(avatarId = avatarId) }

    fun update(avatarId: String, transform: (AvatarState) -> AvatarState): AvatarState {
        val next = transform(get(avatarId)).copy(updatedAt = System.currentTimeMillis())
        val resolved = next.copy(emotion = next.resolveEmotion(), lastEmotion = next.resolveEmotion())
        states[avatarId] = resolved
        return resolved
    }

    fun moveToRoom(avatarId: String, roomId: String): AvatarState =
        update(avatarId) { it.copy(currentRoomId = roomId) }

    fun setRoom(avatarId: String, roomId: String?) = update(avatarId) { it.copy(currentRoomId = roomId) }

    fun onInteraction(avatarId: String, text: String): Pair<AvatarState, EmotionPresentation> {
        val next = applySocialTick(avatarId)
        val emotion = next.resolveEmotion()
        val presentation = EmotionPresentation(
            emotion = EmotionTag.entries.firstOrNull { it.name == emotion.name } ?: EmotionTag.CALM,
            expression = emotion.name.lowercase(),
            animationId = "anim_${emotion.name.lowercase()}",
            voiceTone = "neutral",
            ambienceHint = "default"
        )
        return next to presentation
    }

    fun applySocialTick(avatarId: String, deltaAffection: Float = 0.03f, deltaTrust: Float = 0.02f) =
        update(avatarId) {
            it.copy(
                affection = (it.affection + deltaAffection).coerceIn(0f, 1f),
                trust = (it.trust + deltaTrust).coerceIn(0f, 1f),
                relationshipLevel = when {
                    it.affection > 0.8f && it.trust > 0.7f -> maxOf(it.relationshipLevel, 3)
                    it.affection > 0.5f && it.trust > 0.4f -> maxOf(it.relationshipLevel, 2)
                    it.affection > 0.25f -> maxOf(it.relationshipLevel, 1)
                    else -> it.relationshipLevel
                },
                energy = (it.energy - 0.02f).coerceIn(0.1f, 1f)
            )
        }

    fun setMood(avatarId: String, mood: Float) = update(avatarId) { it.copy(mood = mood.coerceIn(0f, 1f)) }

    fun noteEvent(avatarId: String, summary: String) =
        update(avatarId) { it.copy(lastEventSummary = summary.take(200)) }

    fun presentation(avatarId: String): EmotionPresentation {
        val st = get(avatarId)
        val emotion = st.resolveEmotion()
        return EmotionPresentation(
            emotion = EmotionTag.entries.firstOrNull { it.name == emotion.name } ?: EmotionTag.CALM,
            expression = emotion.name.lowercase(),
            animationId = "anim_${emotion.name.lowercase()}",
            voiceTone = "neutral",
            ambienceHint = "default"
        )
    }

    fun promptModifier(avatarId: String) = get(avatarId).toPromptModifier()

    fun reset(avatarId: String) {
        states[avatarId] = AvatarState(avatarId = avatarId)
    }
}
