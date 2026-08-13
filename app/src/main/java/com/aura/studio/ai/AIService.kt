package com.aura.studio.ai

import android.graphics.Bitmap
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.GenerationEntity
import com.aura.studio.domain.memory.MemoryType
import com.aura.studio.engine.AvatarEngine
import com.aura.studio.engine.AvatarStateEngine
import com.aura.studio.engine.ConversationMemoryEngine
import com.aura.studio.engine.MemoryEngine
import com.aura.studio.engine.RelationshipEngine
import com.aura.studio.engine.RoomEngine
import com.aura.studio.engine.StoryEngine
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
    val generation: GenerationEntity,
    val warning: String? = null
)

class LowMemoryException(message: String) : Exception(message)

@Singleton
class AIService @Inject constructor(
    private val avatarEngine: AvatarEngine,
    private val roomEngine: RoomEngine,
    private val memoryEngine: MemoryEngine,
    private val relationshipEngine: RelationshipEngine,
    private val avatarStateEngine: AvatarStateEngine,
    private val storyEngine: StoryEngine,
    private val conversationMemory: ConversationMemoryEngine,
    private val imageEngine: DiffusionImageEngine,
    private val videoEngine: DiffusionVideoEngine,
    private val dolphin: DolphinService
) {
    fun rooms() = roomEngine.listRooms()
    suspend fun opinion(seed: String = "") = avatarEngine.opinion(seed)
    suspend fun enhance(prompt: String, video: Boolean = false) = avatarEngine.enhance(prompt, video)

    suspend fun generate(req: GenerateRequest): GenerateResult {
        val id = req.avatar.id
        if (req.roomId != null) avatarStateEngine.setRoom(id, req.roomId)

        var prompt = if (req.mode == GenerateRequest.Mode.VIDEO)
            avatarEngine.buildVideoPrompt(req.avatar) else avatarEngine.buildPrompt(req.avatar)
        prompt = roomEngine.combineWithAvatarPrompt(prompt, req.roomId, req.interactionId)

        val stateMod = avatarStateEngine.promptModifier(id)
        if (stateMod.isNotBlank()) prompt = "$prompt, $stateMod"
        val rel = relationshipEngine.promptModifier(id)
        if (rel.isNotBlank()) prompt = "$prompt, $rel"
        val story = storyEngine.promptFragment(id)
        if (story.isNotBlank()) prompt = "$prompt, $story"
        val memories = conversationMemory.selectContext(id, limit = 5)
        if (memories.isNotEmpty()) {
            prompt = "$prompt, memories: " + memories.joinToString("; ") { it.summary }
        }
        if (req.enhance) prompt = avatarEngine.enhance(prompt, req.mode == GenerateRequest.Mode.VIDEO)

        val mem = dolphin.memoryUsage()
        val maxCtx = dolphin.maxContext()
        var warning: String? = null
        if (dolphin.isMemoryHigh()) {
            warning = "High LLM memory (${mem / (1024 * 1024)}MB, ctx=$maxCtx) — unloading before diffusion"
        }
        dolphin.unload()

        var bitmap: Bitmap? = null
        var videoPath: String? = null
        when (req.mode) {
            GenerateRequest.Mode.IMAGE -> {
                imageEngine.load(LocalModel(name = "SD", type = ModelType.IMAGE, path = "/sdcard/Models/sd.safetensors"))
                bitmap = imageEngine.generate(prompt, width = req.width, height = req.height, steps = req.steps, cfg = req.cfg, seed = req.seed)
                if (bitmap == null) warning = listOfNotNull(warning, "Image generation failed").joinToString(" · ")
            }
            GenerateRequest.Mode.VIDEO -> {
                videoEngine.load(LocalModel(name = "Video", type = ModelType.VIDEO, path = "/sdcard/Models/video.gguf"))
                videoPath = videoEngine.generate(prompt)
                if (videoPath == null) warning = listOfNotNull(warning, "Video generation failed").joinToString(" · ")
            }
        }

        relationshipEngine.boostAffinity(id)
        avatarStateEngine.applySocialTick(id)
        conversationMemory.add(
            avatarId = id,
            summary = "Generated ${req.mode.name.lowercase()} in ${req.roomId ?: "no room"}",
            detail = prompt.take(300),
            roomId = req.roomId,
            type = MemoryType.EVENT,
            importance = 0.4f
        )

        val record = GenerationEntity(
            avatarId = id, avatarName = req.avatar.name, prompt = prompt,
            mode = req.mode.name, seed = req.seed, steps = req.steps, cfg = req.cfg,
            width = req.width, height = req.height, imagePath = videoPath
        )
        memoryEngine.remember(record)
        return GenerateResult(prompt, bitmap, videoPath, record, warning)
    }
}
