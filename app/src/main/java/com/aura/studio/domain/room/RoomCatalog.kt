package com.aura.studio.domain.room

object RoomCatalog {
    val BEDROOM = RoomDefinition(
        id = "room_bedroom", type = RoomType.BEDROOM, displayName = "Bedroom",
        environment = EnvironmentSpec("intimate modern bedroom", timeOfDay = "night", mood = "sensual, private"),
        lighting = LightingSpec("warm amber bedside lamps", "city glow through sheer curtains", 0.45f, "warm", "soft"),
        furniture = listOf(
            FurnitureItem("bed", "King bed", "king bed with black silk sheets"),
            FurnitureItem("mirror", "Full mirror", "full-length mirror against the wall"),
            FurnitureItem("chair", "Accent chair", "velvet armchair in the corner")
        ),
        ambience = AmbienceSpec(soundscape = "quiet room, distant city hum", atmosphere = "private, intimate, luxurious"),
        interactions = listOf(
            InteractionSpec("lie_bed", "On the bed", "lying on the bed"),
            InteractionSpec("edge_bed", "Edge of bed", "sitting on the edge of the bed"),
            InteractionSpec("mirror", "At the mirror", "standing before the full-length mirror")
        ),
        actionPrompt = "bedroom scene, intimate framing", tags = listOf("indoor", "intimate", "night")
    )
    val DUNGEON = RoomDefinition(
        id = "room_dungeon", type = RoomType.DUNGEON, displayName = "Dungeon",
        environment = EnvironmentSpec("dark private play dungeon", mood = "intense, controlled"),
        lighting = LightingSpec("red accent lighting", "dim overhead spots", 0.35f, "cool-red", "hard"),
        furniture = listOf(
            FurnitureItem("bench", "Bench", "padded leather bench"),
            FurnitureItem("cross", "Frame", "dark wooden frame")
        ),
        ambience = AmbienceSpec(atmosphere = "enclosed, intense, private", temperature = "cool"),
        interactions = listOf(
            InteractionSpec("bench", "On bench", "positioned on the padded bench"),
            InteractionSpec("standing", "Standing center", "standing in the center of the room")
        ),
        actionPrompt = "dungeon interior, dramatic lighting", tags = listOf("fetish", "dark")
    )
    val STUDIO = RoomDefinition(
        id = "room_studio", type = RoomType.STUDIO, displayName = "Photo Studio",
        environment = EnvironmentSpec("professional photo studio with seamless backdrop", timeOfDay = "studio", mood = "clean, focused"),
        lighting = LightingSpec("softbox key light", "rim light", 0.85f, "neutral", "controlled"),
        furniture = listOf(
            FurnitureItem("backdrop", "Backdrop", "seamless gray backdrop"),
            FurnitureItem("stool", "Stool", "simple black stool")
        ),
        ambience = AmbienceSpec(atmosphere = "clinical, professional, high detail"),
        interactions = listOf(
            InteractionSpec("center", "Center frame", "centered in frame, full body"),
            InteractionSpec("three_quarter", "Three-quarter", "three-quarter pose toward camera")
        ),
        actionPrompt = "studio photography, sharp focus, high detail", tags = listOf("studio")
    )
    val CLUB = RoomDefinition(
        id = "room_club", type = RoomType.CLUB, displayName = "Club",
        environment = EnvironmentSpec("dark nightclub interior", mood = "electric, hedonistic"),
        lighting = LightingSpec("neon magenta and cyan lights", "strobe accents", 0.55f, "neon", "deep"),
        furniture = listOf(
            FurnitureItem("booth", "Booth", "leather booth seat"),
            FurnitureItem("bar", "Bar", "glossy black bar counter")
        ),
        ambience = AmbienceSpec(soundscape = "bass-heavy music", atmosphere = "crowded energy, nightlife"),
        interactions = listOf(
            InteractionSpec("booth", "In booth", "seated in a private booth"),
            InteractionSpec("dancefloor", "Dancefloor", "on the dancefloor")
        ),
        actionPrompt = "nightclub, neon, cinematic", tags = listOf("nightlife", "neon")
    )
    fun all() = listOf(BEDROOM, DUNGEON, STUDIO, CLUB)
    fun byId(id: String) = all().find { it.id == id }
}
