package com.aura.studio.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AvatarEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun avatarDao(): AvatarDao
}
