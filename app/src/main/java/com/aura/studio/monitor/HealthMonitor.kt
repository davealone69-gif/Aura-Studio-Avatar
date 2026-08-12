package com.aura.studio.monitor

import android.content.Context
import android.net.Uri
import android.os.StatFs
import com.aura.studio.data.AppDatabase
import com.aura.studio.data.prefs.UserPrefs
import com.aura.studio.generation.DiffusionImageEngine
import com.aura.studio.generation.DiffusionVideoEngine
import com.aura.studio.generation.DolphinLlmEngine
import com.aura.studio.nativebridge.LlamaBridge
import com.aura.studio.nativebridge.SdBridge
import com.aura.studio.nativebridge.VideoBridge
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: AppDatabase,
    private val prefs: UserPrefs,
    private val dolphin: DolphinLlmEngine,
    private val imageEngine: DiffusionImageEngine,
    private val videoEngine: DiffusionVideoEngine
) {
    suspend fun probe(): List<ComponentHealth> = withContext(Dispatchers.IO) {
        listOf(
            checkRoomDb(), checkDataStore(), checkModelPath(),
            checkLlamaNative(), checkSdNative(), checkVideoNative(),
            checkDolphin(), checkImageEngine(), checkVideoEngine(), checkStorage()
        )
    }

    private suspend fun checkRoomDb(): ComponentHealth = try {
        db.avatarDao().getById("__health_ping__")
        ComponentHealth(ComponentId.ROOM_DB, HealthLevel.HEALTHY, "Room database", "aura.db reachable")
    } catch (e: Exception) {
        ComponentHealth(ComponentId.ROOM_DB, HealthLevel.UNHEALTHY, "Room database", "DB error: ${e.message}", repairable = true)
    }

    private suspend fun checkDataStore(): ComponentHealth = try {
        prefs.genDefaults.first()
        ComponentHealth(ComponentId.DATASTORE, HealthLevel.HEALTHY, "Preferences", "DataStore readable")
    } catch (e: Exception) {
        ComponentHealth(ComponentId.DATASTORE, HealthLevel.UNHEALTHY, "Preferences", "DataStore error: ${e.message}", repairable = true)
    }

    private suspend fun checkModelPath(): ComponentHealth {
        val path = prefs.genDefaults.first().defaultLlmPath.ifBlank {
            "/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf"
        }
        val ok = pathResolvable(path)
        return when {
            path.isBlank() -> ComponentHealth(ComponentId.MODEL_PATH, HealthLevel.UNHEALTHY, "Dolphin model path", "No path configured", repairable = true)
            ok -> ComponentHealth(ComponentId.MODEL_PATH, HealthLevel.HEALTHY, "Dolphin model path", path)
            else -> ComponentHealth(ComponentId.MODEL_PATH, HealthLevel.DEGRADED, "Dolphin model path", "File not found: $path (simulator still works)", repairable = true)
        }
    }

    private fun checkLlamaNative() = ComponentHealth(
        ComponentId.LLAMA_NATIVE,
        if (LlamaBridge.isAvailable()) HealthLevel.HEALTHY else HealthLevel.DEGRADED,
        "llama.cpp bridge",
        if (LlamaBridge.isAvailable()) "libaura_llama loaded" else "Native .so not linked — using simulator",
        repairable = !LlamaBridge.isAvailable()
    )

    private fun checkSdNative() = ComponentHealth(
        ComponentId.SD_NATIVE,
        if (SdBridge.isAvailable()) HealthLevel.HEALTHY else HealthLevel.DEGRADED,
        "Diffusion bridge",
        if (SdBridge.isAvailable()) "libaura_sd loaded" else "Native SD not linked — placeholder images",
        repairable = !SdBridge.isAvailable()
    )

    private fun checkVideoNative() = ComponentHealth(
        ComponentId.VIDEO_NATIVE,
        if (VideoBridge.isAvailable()) HealthLevel.HEALTHY else HealthLevel.DEGRADED,
        "Video bridge",
        if (VideoBridge.isAvailable()) "libaura_video loaded" else "Native video not linked",
        repairable = !VideoBridge.isAvailable()
    )

    private suspend fun checkDolphin(): ComponentHealth {
        val ready = dolphin.isReady()
        return ComponentHealth(ComponentId.DOLPHIN_LLM, if (ready) HealthLevel.HEALTHY else HealthLevel.DEGRADED, "Dolphin engine", if (ready) "Model session loaded" else "Not loaded yet", repairable = true)
    }

    private suspend fun checkImageEngine(): ComponentHealth {
        val ready = imageEngine.isReady()
        return ComponentHealth(ComponentId.IMAGE_ENGINE, if (ready) HealthLevel.HEALTHY else HealthLevel.DEGRADED, "Image engine", if (ready) "Ready" else "Idle", repairable = true)
    }

    private suspend fun checkVideoEngine(): ComponentHealth {
        val ready = videoEngine.isReady()
        return ComponentHealth(ComponentId.VIDEO_ENGINE, if (ready) HealthLevel.HEALTHY else HealthLevel.DEGRADED, "Video engine", if (ready) "Ready" else "Idle", repairable = true)
    }

    private fun checkStorage(): ComponentHealth = try {
        val stat = StatFs(context.filesDir.absolutePath)
        val freeMb = stat.availableBytes / (1024 * 1024)
        when {
            freeMb < 50 -> ComponentHealth(ComponentId.MEMORY, HealthLevel.UNHEALTHY, "Device storage", "Only ${freeMb}MB free", repairable = true)
            freeMb < 200 -> ComponentHealth(ComponentId.MEMORY, HealthLevel.DEGRADED, "Device storage", "${freeMb}MB free", repairable = true)
            else -> ComponentHealth(ComponentId.MEMORY, HealthLevel.HEALTHY, "Device storage", "${freeMb}MB free")
        }
    } catch (e: Exception) {
        ComponentHealth(ComponentId.MEMORY, HealthLevel.UNKNOWN, "Device storage", e.message ?: "unknown")
    }

    private fun pathResolvable(path: String): Boolean {
        if (path.isBlank()) return false
        return try {
            when {
                path.startsWith("content://") || path.startsWith("file://") -> Uri.parse(path).scheme != null
                else -> File(path).let { it.exists() && it.isFile }
            }
        } catch (_: Exception) { false }
    }
}
