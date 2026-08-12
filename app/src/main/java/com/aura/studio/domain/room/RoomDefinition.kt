package com.aura.studio.domain.room

data class RoomDefinition(
    val id: String,
    val type: RoomType,
    val displayName: String,
    val environment: EnvironmentSpec,
    val lighting: LightingSpec,
    val furniture: List<FurnitureItem>,
    val ambience: AmbienceSpec,
    val interactions: List<InteractionSpec>,
    val actionPrompt: String = "",
    val tags: List<String> = emptyList()
)

enum class RoomType { BEDROOM, BATHROOM, LIVING_ROOM, KITCHEN, CLUB, DUNGEON, OUTDOOR, STUDIO, CUSTOM }

data class EnvironmentSpec(val setting: String, val timeOfDay: String = "night", val weather: String? = null, val colorPalette: List<String> = emptyList(), val mood: String = "intimate")
data class LightingSpec(val primary: String, val secondary: String? = null, val intensity: Float = 0.6f, val colorTemp: String = "warm", val shadows: String = "soft")
data class FurnitureItem(val id: String, val name: String, val promptHint: String, val interactive: Boolean = true)
data class AmbienceSpec(val soundscape: String? = null, val scent: String? = null, val temperature: String = "warm", val atmosphere: String = "private, quiet")
data class InteractionSpec(val id: String, val label: String, val promptFragment: String, val requiresConsent: Boolean = false)

fun RoomDefinition.toPromptFragment(interactionId: String? = null): String = buildString {
    append(environment.setting); append(", "); append(environment.timeOfDay)
    environment.weather?.let { append(", "); append(it) }
    append(", lighting: "); append(lighting.primary)
    lighting.secondary?.let { append(", "); append(it) }
    append(", "); append(lighting.shadows); append(" shadows")
    if (furniture.isNotEmpty()) { append(", furniture: "); append(furniture.joinToString(", ") { it.promptHint }) }
    append(", atmosphere: "); append(ambience.atmosphere)
    ambience.soundscape?.let { append(", "); append(it) }
    if (actionPrompt.isNotBlank()) { append(", "); append(actionPrompt) }
    interactionId?.let { id -> interactions.find { it.id == id }?.let { append(", "); append(it.promptFragment) } }
}
