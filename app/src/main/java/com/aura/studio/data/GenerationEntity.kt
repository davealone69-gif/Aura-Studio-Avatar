package com.aura.studio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "generations")
data class GenerationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val avatarId: String,
    val avatarName: String,
    val prompt: String,
    val mode: String,
    val seed: Long,
    val steps: Int,
    val cfg: Float,
    val width: Int,
    val height: Int,
    val imagePath: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
