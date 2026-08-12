package com.aura.studio.engine

import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.AvatarRepository
import com.aura.studio.generation.DolphinLlmEngine
import com.aura.studio.generation.PromptTemplates
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarEngine @Inject constructor(
    private val repo: AvatarRepository,
    private val llm: DolphinLlmEngine
) {
    fun observeAll(): Flow<List<AvatarSpec>> = repo.getAll()
    suspend fun get(id: String) = repo.getById(id)
    suspend fun save(spec: AvatarSpec) = repo.save(spec)
    suspend fun delete(id: String) = repo.delete(id)
    fun buildPrompt(spec: AvatarSpec) = spec.toPrompt()
    fun buildVideoPrompt(spec: AvatarSpec) = spec.toVideoPrompt()
    suspend fun opinion(seedHint: String = ""): AvatarSpec {
        llm.load(LocalModel(name = "Dolphin", type = ModelType.LLM, path = "/sdcard/Models/dolphin.gguf"))
        return llm.opinion(seedHint)
    }
    suspend fun enhance(prompt: String, forVideo: Boolean = false): String {
        llm.load(LocalModel(name = "Dolphin", type = ModelType.LLM, path = "/sdcard/Models/dolphin.gguf"))
        val system = if (forVideo) PromptTemplates.VIDEO_MOTION else PromptTemplates.AVATAR_SYSTEM
        return llm.generate(system, prompt)
    }
}
