package com.aura.studio.engine

import com.aura.studio.domain.story.StoryCatalog
import com.aura.studio.domain.story.StoryState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryEngine @Inject constructor() {
    private val states = mutableMapOf<String, StoryState>()

    fun get(avatarId: String): StoryState =
        states.getOrPut(avatarId) { StoryState() }

    fun set(avatarId: String, state: StoryState) {
        states[avatarId] = state
    }

    fun advanceIfReady(avatarId: String, relationshipLevel: Int): StoryState {
        val current = get(avatarId)
        val chapter = StoryCatalog.forLevel(relationshipLevel)
        if (chapter.chapter > current.chapter) {
            val next = current.copy(
                chapter = chapter.chapter,
                objective = chapter.objective,
                location = chapter.location,
                relationshipLevel = relationshipLevel,
                actionPrompt = chapter.actionPrompt,
                completedObjectives = current.completedObjectives + current.objective
            )
            states[avatarId] = next
            return next
        }
        return current.copy(relationshipLevel = relationshipLevel)
    }
}
