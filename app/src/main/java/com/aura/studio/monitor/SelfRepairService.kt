package com.aura.studio.monitor

import android.content.Context
import com.aura.studio.ai.DolphinService
import com.aura.studio.data.AppDatabase
import com.aura.studio.data.GenerationDao
import com.aura.studio.generation.DiffusionImageEngine
import com.aura.studio.generation.DiffusionVideoEngine
import com.aura.studio.generation.DolphinLlmEngine
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfRepairService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val monitor: HealthMonitor,
    private val dolphinService: DolphinService,
    private val dolphinEngine: DolphinLlmEngine,
    private val imageEngine: DiffusionImageEngine,
    private val videoEngine: DiffusionVideoEngine,
    private val db: AppDatabase,
    private val generationDao: GenerationDao
) {
    private val repairLog = ArrayDeque<RepairResult>(32)

    fun recentRepairs(): List<RepairResult> = repairLog.toList().asReversed()

    fun suggestedRepairs(components: List<ComponentHealth>): List<RepairAction> {
        val actions = mutableListOf<RepairAction>()
        components.forEach { c ->
            when (c.id) {
                ComponentId.ROOM_DB -> if (c.level != HealthLevel.HEALTHY) {
                    actions += RepairAction("reopen_db", "Reopen database", c.id, "Ping DB")
                }
                ComponentId.DOLPHIN_LLM, ComponentId.MODEL_PATH -> {
                    actions += RepairAction("reload_dolphin", "Reload Dolphin", ComponentId.DOLPHIN_LLM, "Unload + reload")
                }
                ComponentId.IMAGE_ENGINE -> actions += RepairAction("reset_image", "Reset image engine", c.id, "Unload diffusion")
                ComponentId.VIDEO_ENGINE -> actions += RepairAction("reset_video", "Reset video engine", c.id, "Unload video")
                ComponentId.MEMORY -> if (c.level != HealthLevel.HEALTHY) {
                    actions += RepairAction("trim_cache", "Trim app cache", c.id, "Delete temp files")
                    actions += RepairAction("clear_gen_history", "Clear generation history", c.id, "Free Room rows")
                }
                else -> {}
            }
        }
        actions += RepairAction("full_self_repair", "Run full self-repair", ComponentId.OVERALL, "Reload engines, ping DB, trim cache")
        return actions.distinctBy { it.id }
    }

    suspend fun run(actionId: String): RepairResult = withContext(Dispatchers.IO) {
        val result = try {
            when (actionId) {
                "reopen_db" -> {
                    db.avatarDao().getById("__repair__")
                    generationDao.getAll().first()
                    RepairResult(actionId, true, "Database responsive")
                }
                "reload_dolphin" -> {
                    dolphinEngine.unload()
                    val load = dolphinService.ensureLoaded()
                    RepairResult(actionId, load.isUsable, load.userMessage)
                }
                "reset_image" -> { imageEngine.unload(); RepairResult(actionId, true, "Image engine unloaded") }
                "reset_video" -> { videoEngine.unload(); RepairResult(actionId, true, "Video engine unloaded") }
                "trim_cache" -> trimCache()
                "clear_gen_history" -> { generationDao.clear(); RepairResult(actionId, true, "History cleared") }
                "full_self_repair" -> fullRepair()
                else -> RepairResult(actionId, false, "Unknown repair action")
            }
        } catch (e: Exception) {
            RepairResult(actionId, false, "Repair failed: ${e.message}")
        }
        pushLog(result)
        result
    }

    suspend fun autoRepair(): List<RepairResult> = withContext(Dispatchers.IO) {
        val health = monitor.probe()
        suggestedRepairs(health).filter { it.id != "clear_gen_history" }.map { run(it.id) }
    }

    private fun trimCache(): RepairResult = try {
        var freed = 0L
        context.cacheDir.listFiles()?.forEach { f -> freed += sizeOf(f); f.deleteRecursively() }
        RepairResult("trim_cache", true, "Cleared ~${freed / 1024}KB cache")
    } catch (e: Exception) {
        RepairResult("trim_cache", false, e.message ?: "failed")
    }

    private suspend fun fullRepair(): RepairResult {
        val parts = listOf(run("reopen_db"), run("reload_dolphin"), run("reset_image"), run("reset_video"), trimCache())
        val ok = parts.count { it.success }
        return RepairResult("full_self_repair", ok >= 3, "Full repair: $ok/${parts.size} steps succeeded")
    }

    private fun pushLog(r: RepairResult) {
        if (repairLog.size >= 32) repairLog.removeFirst()
        repairLog.addLast(r)
    }

    private fun sizeOf(f: File): Long = if (f.isFile) f.length() else f.listFiles()?.sumOf { sizeOf(it) } ?: 0L
}
