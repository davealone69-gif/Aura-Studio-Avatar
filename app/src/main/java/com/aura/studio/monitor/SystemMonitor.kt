package com.aura.studio.monitor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemMonitor @Inject constructor(
    private val health: HealthMonitor,
    private val repair: SelfRepairService
) {
    suspend fun report(): SystemReport = withContext(Dispatchers.IO) {
        val components = health.probe()
        val overall = when {
            components.any { it.level == HealthLevel.UNHEALTHY } -> HealthLevel.UNHEALTHY
            components.any { it.level == HealthLevel.DEGRADED } -> HealthLevel.DEGRADED
            components.any { it.level == HealthLevel.UNKNOWN } -> HealthLevel.UNKNOWN
            else -> HealthLevel.HEALTHY
        }
        SystemReport(
            overall = overall,
            components = components,
            availableRepairs = repair.suggestedRepairs(components),
            recentRepairs = repair.recentRepairs()
        )
    }

    suspend fun runRepair(actionId: String): RepairResult = repair.run(actionId)
    suspend fun autoRepair(): List<RepairResult> = repair.autoRepair()
}
