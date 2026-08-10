package com.aura.studio.data

import com.aura.studio.avatar.AvatarSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarRepository @Inject constructor(
    private val dao: AvatarDao
) {
    fun getAll(): Flow<List<AvatarSpec>> =
        dao.getAll().map { list -> list.map { it.toSpec() } }

    suspend fun getById(id: String): AvatarSpec? =
        dao.getById(id)?.toSpec()

    suspend fun save(spec: AvatarSpec) {
        dao.upsert(AvatarEntity.fromSpec(spec))
    }

    suspend fun delete(id: String) {
        dao.deleteById(id)
    }
}
