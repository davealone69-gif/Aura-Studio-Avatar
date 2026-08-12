package com.aura.studio.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room migrations — preserves user data across schema changes.
 */
object Migrations {

    /**
     * v1: original simple avatar table
     * v2: designer fields (face, outfit, effects, pose, updatedAt)
     */
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

    val ALL = arrayOf(MIGRATION_1_2)
}
