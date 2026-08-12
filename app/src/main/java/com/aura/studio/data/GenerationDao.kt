package com.aura.studio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GenerationDao {
    @Query("SELECT * FROM generations ORDER BY createdAt DESC")
    fun getAll(): Flow<List<GenerationEntity>>

    @Query("SELECT * FROM generations WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<GenerationEntity>>

    @Query("SELECT * FROM generations WHERE avatarId = :avatarId ORDER BY createdAt DESC")
    fun getForAvatar(avatarId: String): Flow<List<GenerationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GenerationEntity)

    @Query("UPDATE generations SET isFavorite = :fav WHERE id = :id")
    suspend fun setFavorite(id: String, fav: Boolean)

    @Query("DELETE FROM generations WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM generations")
    suspend fun clear()
}
