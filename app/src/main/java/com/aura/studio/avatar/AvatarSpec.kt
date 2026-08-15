package com.aura.studio.avatar

data class AvatarSpec(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "New Girl",
    val age: Int = 22,
    val ethnicity: String = "Caucasian",
    val bodyType: String = "Slim",
    val breastSize: String = "C",
    val eyeColor: String = "Blue",
    val eyeShape: String = "Almond",
    val nose: String = "Straight",
    val mouth: String = "Full",
    val faceShape: String = "Oval",
    val hairColor: String = "Brown",
    val hairStyle: String = "Long wavy",
    val hairLength: Float = 0.7f,
    val skinTone: String = "Fair",
    val clothing: String = "None",
    val outfitStyle: String = "None",
    val accentColor: String = "Black",
    val glow: Float = 0.4f,
    val depth: Float = 0.5f,
    val shadow: Float = 0.4f,
    val filter: String = "None",
    val pose: String = "Standing",
    val expression: String = "Seductive",
    val extra: String = "",
    val isNude: Boolean = true
) {
    fun toPrompt(): String {
        return buildString {
            append("$age year old $ethnicity woman, ")
            append("$bodyType body, $breastSize cup breasts, ")
            append("$eyeColor $eyeShape eyes, $nose nose, $mouth lips, $faceShape face, ")
            append("$hairColor $hairStyle hair, $skinTone skin")
            if (isNude || clothing.equals("None", ignoreCase = true)) {
                append(", completely nude, detailed anatomy, realistic skin")
            } else {
                append(", wearing $clothing, $outfitStyle style")
            }
            append(", pose: $pose, expression: $expression")
            if (extra.isNotBlank()) append(", $extra")
            append(", highly detailed, photorealistic")
        }
    }

    fun toVideoPrompt(): String =
        toPrompt() + ", subtle natural motion, gentle camera drift, cinematic lighting"
}

object AvatarOptions {
    val ethnicities = listOf("Caucasian", "East Asian", "Black", "Latina", "South Asian", "Mixed", "Middle Eastern")
    val bodyTypes   = listOf("Petite", "Slim", "Athletic", "Curvy", "Hourglass", "Voluptuous")
    val breastSizes = listOf("A", "B", "C", "D", "DD", "E", "F")
    val eyeColors   = listOf("Blue", "Green", "Brown", "Hazel", "Gray")
    val eyeShapes   = listOf("Almond", "Round", "Hooded", "Upturned")
    val noses       = listOf("Straight", "Button", "Roman", "Upturned")
    val mouths      = listOf("Full", "Thin", "Heart", "Wide")
    val faceShapes  = listOf("Oval", "Heart", "Round", "Square", "Diamond")
    val hairColors  = listOf("Black", "Brown", "Blonde", "Red", "Pink", "White")
    val hairStyles  = listOf("Short", "Bob", "Long straight", "Long wavy", "Pigtails", "Ponytail")
    val skinTones   = listOf("Fair", "Light", "Medium", "Tan", "Dark", "Deep", "Olive")
    val clothing    = listOf("None", "Bikini", "Lingerie", "Fishnet", "Latex", "Casual", "Harness")
    val poses       = listOf("Standing", "Sitting", "Lying", "Kneeling", "Arching", "Spread", "From behind")
}
