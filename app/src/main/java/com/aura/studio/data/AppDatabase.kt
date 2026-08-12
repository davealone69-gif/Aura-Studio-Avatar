package com.aura.studio.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AvatarEntity::class, GenerationEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun avatarDao(): AvatarDao
    abstract fun generationDao(): GenerationDao
}
