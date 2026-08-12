package com.aura.studio.data

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2_preservesData_andAddsColumns() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS avatars (
                    id TEXT NOT NULL PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    ethnicity TEXT NOT NULL,
                    bodyType TEXT NOT NULL,
                    breastSize TEXT NOT NULL,
                    eyeColor TEXT NOT NULL,
                    hairColor TEXT NOT NULL,
                    hairStyle TEXT NOT NULL,
                    skinTone TEXT NOT NULL,
                    clothing TEXT NOT NULL,
                    extra TEXT NOT NULL,
                    isNude INTEGER NOT NULL,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO avatars (
                    id, name, age, ethnicity, bodyType, breastSize,
                    eyeColor, hairColor, hairStyle, skinTone,
                    clothing, extra, isNude, createdAt
                ) VALUES (
                    'test-id-1', 'Luna', 22, 'Caucasian', 'Curvy', 'D',
                    'Blue', 'Red', 'Long wavy', 'Fair',
                    'None', 'tattoos', 1, 1700000000000
                )
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, Migrations.MIGRATION_1_2)

        db.query("SELECT name, age, isNude, createdAt FROM avatars WHERE id = 'test-id-1'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Luna", cursor.getString(0))
            assertEquals(22, cursor.getInt(1))
            assertEquals(1, cursor.getInt(2))
            assertEquals(1700000000000L, cursor.getLong(3))
        }

        db.query(
            """
            SELECT eyeShape, nose, mouth, faceShape, hairLength,
                   outfitStyle, accentColor, glow, depth, shadow,
                   filter, pose, expression, updatedAt
            FROM avatars WHERE id = 'test-id-1'
            """.trimIndent()
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Almond", cursor.getString(0))
            assertEquals("Straight", cursor.getString(1))
            assertEquals("Full", cursor.getString(2))
            assertEquals("Oval", cursor.getString(3))
            assertEquals(0.7, cursor.getDouble(4), 0.001)
            assertEquals("None", cursor.getString(5))
            assertEquals("Black", cursor.getString(6))
            assertEquals(0.3, cursor.getDouble(7), 0.001)
            assertEquals(0.5, cursor.getDouble(8), 0.001)
            assertEquals(0.4, cursor.getDouble(9), 0.001)
            assertEquals("None", cursor.getString(10))
            assertEquals("Standing", cursor.getString(11))
            assertEquals("Neutral", cursor.getString(12))
            assertEquals(1700000000000L, cursor.getLong(13))
        }

        db.query("SELECT COUNT(*) FROM avatars").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2_emptyDatabase_succeeds() {
        helper.createDatabase(TEST_DB + "-empty", 1).apply {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS avatars (
                    id TEXT NOT NULL PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    ethnicity TEXT NOT NULL,
                    bodyType TEXT NOT NULL,
                    breastSize TEXT NOT NULL,
                    eyeColor TEXT NOT NULL,
                    hairColor TEXT NOT NULL,
                    hairStyle TEXT NOT NULL,
                    skinTone TEXT NOT NULL,
                    clothing TEXT NOT NULL,
                    extra TEXT NOT NULL,
                    isNude INTEGER NOT NULL,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(
            TEST_DB + "-empty", 2, true, Migrations.MIGRATION_1_2
        )

        db.query("SELECT COUNT(*) FROM avatars").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(0, cursor.getInt(0))
        }

        db.query("SELECT eyeShape, updatedAt FROM avatars").use { cursor ->
            assertFalse(cursor.moveToFirst())
        }
    }
}
