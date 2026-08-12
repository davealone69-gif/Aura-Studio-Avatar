package com.aura.studio.monitor

enum class ComponentId {
    ROOM_DB, DATASTORE, DOLPHIN_LLM, LLAMA_NATIVE, SD_NATIVE, VIDEO_NATIVE,
    MODEL_PATH, IMAGE_ENGINE, VIDEO_ENGINE, MEMORY, OVERALL
}

enum class HealthLevel { HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN }

data class ComponentHealth(
    val id: ComponentId,
    val level: HealthLevel,
    val title: String,
    val detail: String,
    val repairable: Boolean = false,
    val lastCheckedAt: Long = System.currentTimeMillis()
)

data class RepairAction(
    val id: String,
    val label: String,
    val target: ComponentId,
    val description: String
)

data class RepairResult(
    val actionId: String,
    val success: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SystemReport(
    val overall: HealthLevel,
    val components: List<ComponentHealth>,
    val availableRepairs: List<RepairAction>,
    val recentRepairs: List<RepairResult>,
    val generatedAt: Long = System.currentTimeMillis()
) {
    val unhealthyCount: Int get() = components.count { it.level == HealthLevel.UNHEALTHY }
    val degradedCount: Int get() = components.count { it.level == HealthLevel.DEGRADED }
}
