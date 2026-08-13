package com.aura.studio.domain.room

object RoomObjects {
    fun forRoom(roomId: String): List<InteractiveObject> = when (roomId) {
        "room_bedroom" -> listOf(
            InteractiveObject("bed", "Bed", "king bed", "lie on the bed"),
            InteractiveObject("wardrobe", "Wardrobe", "open wardrobe", "choose an outfit"),
            InteractiveObject("mirror", "Mirror", "full-length mirror", "look in the mirror"),
            InteractiveObject("window", "Window", "city window", "look out the window"),
            InteractiveObject("music", "Music", "bedside speaker", "play soft music")
        )
        "room_dungeon" -> listOf(
            InteractiveObject("bench", "Bench", "padded bench", "use the bench"),
            InteractiveObject("frame", "Frame", "wooden frame", "approach the frame")
        )
        "room_studio" -> listOf(
            InteractiveObject("backdrop", "Backdrop", "seamless backdrop", "stand at the backdrop"),
            InteractiveObject("stool", "Stool", "black stool", "sit on the stool")
        )
        "room_club" -> listOf(
            InteractiveObject("booth", "VIP Booth", "leather booth", "sit in the booth"),
            InteractiveObject("bar", "Bar", "black bar", "order at the bar"),
            InteractiveObject("dancefloor", "Dancefloor", "neon floor", "step onto the dancefloor")
        )
        else -> emptyList()
    }
}
