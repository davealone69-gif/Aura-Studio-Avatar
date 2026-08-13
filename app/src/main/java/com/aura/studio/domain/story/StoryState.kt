package com.aura.studio.domain.story

import com.aura.studio.domain.room.RoomType

data class StoryState(
    val chapter: Int = 1,
    val objective: String = "Get to know each other",
    val location: RoomType = RoomType.BEDROOM,
    val relationshipLevel: Int = 0,
    val actionPrompt: String = "",
    val flags: Set<String> = emptySet(),
    val completedObjectives: List<String> = emptyList()
) {
    fun promptFrame(): String =
        "Story chapter $chapter. Objective: $objective. Location: ${location.name}. Relationship level: $relationshipLevel." +
            if (actionPrompt.isNotBlank()) " Direction: $actionPrompt" else ""
}

data class StoryChapter(
    val chapter: Int,
    val title: String,
    val objective: String,
    val location: RoomType,
    val minRelationship: Int = 0,
    val actionPrompt: String = ""
)

object StoryCatalog {
    val chapters = listOf(
        StoryChapter(1, "First Meeting", "Break the ice", RoomType.STUDIO, 0, "first encounter"),
        StoryChapter(2, "Private Space", "Visit the bedroom", RoomType.BEDROOM, 1, "invite closer"),
        StoryChapter(3, "Nightlife", "Share a night out", RoomType.CLUB, 2, "playful energy"),
        StoryChapter(4, "Intensity", "Explore trust", RoomType.DUNGEON, 3, "consensual intensity")
    )
    fun forLevel(level: Int) = chapters.lastOrNull { level >= it.minRelationship } ?: chapters.first()
}
