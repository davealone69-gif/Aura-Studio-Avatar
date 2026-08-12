package com.aura.studio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aura.studio.avatar.AvatarSpec

@Entity(tableName = "avatars")
data class AvatarEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val ethnicity: String,
    val bodyType: String,
    val breastSize: String,
    val skinTone: String,
    val eyeColor: String,
    val eyeShape: String,
    val nose: String,
    val mouth: String,
    val faceShape: String,
    val hairColor: String,
    val hairStyle: String,
    val hairLength: Float,
    val isNude: Boolean,
    val clothing: String,
    val outfitStyle: String,
    val accentColor: String,
    val glow: Float,
    val depth: Float,
    val shadow: Float,
    val filter: String,
    val extra: String,
    val pose: String,
    val expression: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toSpec() = AvatarSpec(
        id = id, name = name, age = age, ethnicity = ethnicity, bodyType = bodyType,
        breastSize = breastSize, skinTone = skinTone, eyeColor = eyeColor, eyeShape = eyeShape,
        nose = nose, mouth = mouth, faceShape = faceShape, hairColor = hairColor,
        hairStyle = hairStyle, hairLength = hairLength, isNude = isNude, clothing = clothing,
        outfitStyle = outfitStyle, accentColor = accentColor, glow = glow, depth = depth,
        shadow = shadow, filter = filter, extra = extra, pose = pose, expression = expression,
        createdAt = createdAt, updatedAt = updatedAt
    )

    companion object {
        fun fromSpec(spec: AvatarSpec) = AvatarEntity(
            id = spec.id, name = spec.name, age = spec.age, ethnicity = spec.ethnicity,
            bodyType = spec.bodyType, breastSize = spec.breastSize, skinTone = spec.skinTone,
            eyeColor = spec.eyeColor, eyeShape = spec.eyeShape, nose = spec.nose,
            mouth = spec.mouth, faceShape = spec.faceShape, hairColor = spec.hairColor,
            hairStyle = spec.hairStyle, hairLength = spec.hairLength, isNude = spec.isNude,
            clothing = spec.clothing, outfitStyle = spec.outfitStyle, accentColor = spec.accentColor,
            glow = spec.glow, depth = spec.depth, shadow = spec.shadow, filter = spec.filter,
            extra = spec.extra, pose = spec.pose, expression = spec.expression,
            createdAt = spec.createdAt, updatedAt = System.currentTimeMillis()
        )
    }
}
