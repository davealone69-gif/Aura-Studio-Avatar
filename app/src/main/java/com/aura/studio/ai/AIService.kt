package com.aura.studio.ai

import android.graphics.Bitmap
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.GenerationEntity
import com.aura.studio.engine.*
import com.aura.studio.generation.DiffusionImageEngine
import com.aura.studio.generation.DiffusionVideoEngine
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import javax.inject.Inject
import javax.inject.Singleton

data class GenerateRequest(
    val avatar: AvatarSpec,
    val roomId: String? = null,
    val interactionId: String? = null,
    val enhance: Boolean = false,
    val mode: Mode = Mode.IMAGE,
    val seed: Long = -1L,
    val steps: Int = 20,
    val cfg: Float = 7f,
    val width: Int = 512,
    val height: Int = 768
) { enum class Mode { IMAGE, VIDEO } }

data class GenerateResult(
    val prompt: String,
    val bitmap: Bitmap? = null,
    val videoPath: String? = null,
    val generation: GenerationEntity
)

@Singleton
class AIService @Inject constructor(
    private val avatarEngine: AvatarEngine,
    private val roomEngine: RoomEngine,
    private val memoryEngine: MemoryEngine,
    private val relationshipEngine: RelationshipEngine,
    private val imageEngine: DiffusionImageEngine,
    private val videoEngine: DiffusionVideoEngine
) {
    fun rooms() = roomEngine.listRooms()
    suspend fun opinion(seed: String = "") = avatarEngine.opinion(seed)
    suspend fun enhance(prompt: String, video: Boolean = false) = avatarEngine.enhance(prompt, video)

    suspend fun generate(req: GenerateRequest): GenerateResult {
        var prompt = if (req.mode == GenerateRequest.Mode.VIDEO)
            avatarEngine.buildVideoPrompt(req.avatar) else avatarEngine.buildPrompt(req.avatar)
        prompt = roomEngine.combineWithAvatarPrompt(prompt, req.roomId, req.interactionId)
        val rel = relationshipEngine.promptModifier(req.avatar.id)
        if (rel.isNotBlank()) prompt = "$prompt, $rel"
        if (req.enhance) prompt = avatarEngine.enhance(prompt, req.mode == GenerateRequest.Mode.VIDEO)

        var bitmap: Bitmap? = null
        var videoPath: String? = null
        when (req.mode) {
            GenerateRequest.Mode.IMAGE -> {
                imageEngine.load(LocalModel(name = "SD", type = ModelType.IMAGE, path = "/sdcard/Models/sd.safetensors"))
                bitmap = imageEngine.generate(prompt, width = req.width, height = req.height, steps = req.steps, cfg = req.cfg, seed = req.seed)
            }
            GenerateRequest.Mode.VIDEO -> {
                videoEngine.load(LocalModel(name = "Video", type = ModelType.VIDEO, path = "/sdcard/Models/video.gguf"))
                videoPath = videoEngine.generate(prompt)
            }
        }
        relationshipEngine.boostAffinity(req.avatar.id)
        val record = GenerationEntity(
            avatarId = req.avatar.id, avatarName = req.avatar.name, prompt = prompt,
            mode = req.mode.name, seed = req.seed, steps = req.steps, cfg = req.cfg,
            width = req.width, height = req.height, imagePath = videoPath
        )
        memoryEngine.remember(record)
        return GenerateResult(prompt, bitmap, videoPath, record)
    }
}
