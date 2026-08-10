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
    val eyeColor: String,
    val hairColor: String,
    val hairStyle: String,
    val skinTone: String,
    val clothing: String,
    val extra: String,
    val isNude: Boolean,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toSpec() = AvatarSpec(
        id = id,
        name = name,
        age = age,
        ethnicity = ethnicity,
        bodyType = bodyType,
        breastSize = breastSize,
        eyeColor = eyeColor,
        hairColor = hairColor,
        hairStyle = hairStyle,
        skinTone = skinTone,
        clothing = clothing,
        extra = extra,
        isNude = isNude
    )

    companion object {
        fun fromSpec(spec: AvatarSpec) = AvatarEntity(
            id = spec.id,
            name = spec.name,
            age = spec.age,
            ethnicity = spec.ethnicity,
            bodyType = spec.bodyType,
            breastSize = spec.breastSize,
            eyeColor = spec.eyeColor,
            hairColor = spec.hairColor,
            hairStyle = spec.hairStyle,
            skinTone = spec.skinTone,
            clothing = spec.clothing,
            extra = spec.extra,
            isNude = spec.isNude
        )
    }
}
