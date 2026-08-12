package com.aura.studio.engine

import com.aura.studio.data.GenerationDao
import com.aura.studio.data.GenerationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryEngine @Inject constructor(private val generationDao: GenerationDao) {
    fun history(): Flow<List<GenerationEntity>> = generationDao.getAll()
    fun favorites(): Flow<List<GenerationEntity>> = generationDao.getFavorites()
    fun forAvatar(avatarId: String) = generationDao.getForAvatar(avatarId)
    suspend fun remember(generation: GenerationEntity) = generationDao.insert(generation)
    suspend fun setFavorite(id: String, fav: Boolean) = generationDao.setFavorite(id, fav)
    suspend fun forget(id: String) = generationDao.delete(id)
    suspend fun clearHistory() = generationDao.clear()
}
