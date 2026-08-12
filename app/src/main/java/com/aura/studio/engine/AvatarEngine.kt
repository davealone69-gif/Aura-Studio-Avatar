package com.aura.studio.engine

import com.aura.studio.ai.DolphinService
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.AvatarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarEngine @Inject constructor(
    private val repo: AvatarRepository,
    private val dolphin: DolphinService
) {
    fun observeAll(): Flow<List<AvatarSpec>> = repo.getAll()
    suspend fun get(id: String) = repo.getById(id)
    suspend fun save(spec: AvatarSpec) = repo.save(spec)
    suspend fun delete(id: String) = repo.delete(id)
    fun buildPrompt(spec: AvatarSpec) = spec.toPrompt()
    fun buildVideoPrompt(spec: AvatarSpec) = spec.toVideoPrompt()
    suspend fun opinion(seedHint: String = "") = dolphin.opinion(seedHint)
    suspend fun enhance(prompt: String, forVideo: Boolean = false) = dolphin.enhance(prompt, forVideo)
}
