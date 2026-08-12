package com.aura.studio.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE avatars ADD COLUMN eyeShape TEXT NOT NULL DEFAULT 'Almond'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN nose TEXT NOT NULL DEFAULT 'Straight'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN mouth TEXT NOT NULL DEFAULT 'Full'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN faceShape TEXT NOT NULL DEFAULT 'Oval'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN hairLength REAL NOT NULL DEFAULT 0.7")
            db.execSQL("ALTER TABLE avatars ADD COLUMN outfitStyle TEXT NOT NULL DEFAULT 'None'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN accentColor TEXT NOT NULL DEFAULT 'Black'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN glow REAL NOT NULL DEFAULT 0.3")
            db.execSQL("ALTER TABLE avatars ADD COLUMN depth REAL NOT NULL DEFAULT 0.5")
            db.execSQL("ALTER TABLE avatars ADD COLUMN shadow REAL NOT NULL DEFAULT 0.4")
            db.execSQL("ALTER TABLE avatars ADD COLUMN filter TEXT NOT NULL DEFAULT 'None'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN pose TEXT NOT NULL DEFAULT 'Standing'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN expression TEXT NOT NULL DEFAULT 'Neutral'")
            db.execSQL("ALTER TABLE avatars ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
            db.execSQL("UPDATE avatars SET updatedAt = createdAt WHERE updatedAt = 0")
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""CREATE TABLE IF NOT EXISTS generations (
                id TEXT NOT NULL PRIMARY KEY, avatarId TEXT NOT NULL, avatarName TEXT NOT NULL,
                prompt TEXT NOT NULL, mode TEXT NOT NULL, seed INTEGER NOT NULL,
                steps INTEGER NOT NULL, cfg REAL NOT NULL, width INTEGER NOT NULL,
                height INTEGER NOT NULL, imagePath TEXT, isFavorite INTEGER NOT NULL DEFAULT 0,
                createdAt INTEGER NOT NULL)""")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_generations_avatarId ON generations(avatarId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_generations_createdAt ON generations(createdAt)")
        }
    }
    val ALL = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
}
