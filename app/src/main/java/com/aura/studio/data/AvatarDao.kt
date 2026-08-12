package com.aura.studio.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AvatarDao {
    @Query("SELECT * FROM avatars ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<AvatarEntity>>

    @Query("SELECT * FROM avatars WHERE id = :id")
    suspend fun getById(id: String): AvatarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(avatar: AvatarEntity)

    @Delete
    suspend fun delete(avatar: AvatarEntity)

    @Query("DELETE FROM avatars WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM avatars")
    suspend fun deleteAll()
}
