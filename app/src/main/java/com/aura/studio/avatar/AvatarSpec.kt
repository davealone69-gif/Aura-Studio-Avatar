package com.aura.studio.avatar

data class AvatarSpec(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "New Girl",
    val age: Int = 22,
    val ethnicity: String = "Caucasian",
    val bodyType: String = "Slim",
    val breastSize: String = "C",
    val eyeColor: String = "Blue",
    val hairColor: String = "Brown",
    val hairStyle: String = "Long wavy",
    val skinTone: String = "Fair",
    val clothing: String = "None",          // "None" = nude
    val extra: String = "",
    val isNude: Boolean = true
) {
    fun toPrompt(): String {
        val base = buildString {
            append("$age year old $ethnicity woman, ")
            append("$bodyType body, $breastSize cup breasts, ")
            append("$eyeColor eyes, $hairColor $hairStyle hair, $skinTone skin")
            
            if (isNude || clothing.equals("None", ignoreCase = true)) {
                append(", completely nude, detailed anatomy, realistic skin")
            } else {
                append(", wearing $clothing")
            }
            
            if (extra.isNotBlank()) append(", $extra")
            append(", highly detailed, photorealistic")
        }
        return base
    }
}

object AvatarOptions {
    val ethnicities = listOf("Caucasian", "East Asian", "Black", "Latina", "South Asian", "Mixed")
    val bodyTypes   = listOf("Petite", "Slim", "Athletic", "Curvy", "Hourglass")
    val breastSizes = listOf("A", "B", "C", "D", "DD", "E")
    val eyeColors   = listOf("Blue", "Green", "Brown", "Hazel", "Gray")
    val hairColors  = listOf("Black", "Brown", "Blonde", "Red", "Pink", "White")
    val hairStyles  = listOf("Short", "Bob", "Long straight", "Long wavy", "Pigtails", "Ponytail")
    val skinTones   = listOf("Fair", "Light", "Medium", "Tan", "Dark")
    val clothing    = listOf("None", "Bikini", "Lingerie", "Fishnet", "Latex", "Casual")
}
