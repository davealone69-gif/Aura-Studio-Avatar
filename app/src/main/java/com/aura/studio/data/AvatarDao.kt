package com.aura.studio.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AvatarDao {
    @Query("SELECT * FROM avatars ORDER BY createdAt DESC")
    fun getAll(): Flow<List<AvatarEntity>>

    @Query("SELECT * FROM avatars WHERE id = :id")
    suspend fun getById(id: String): AvatarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(avatar: AvatarEntity)

    @Delete
    suspend fun delete(avatar: AvatarEntity)

    @Query("DELETE FROM avatars WHERE id = :id")
    suspend fun deleteById(id: String)
}
