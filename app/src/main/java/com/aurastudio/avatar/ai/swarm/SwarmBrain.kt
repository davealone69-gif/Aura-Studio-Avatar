package com.aurastudio.avatar.ai.swarm

import kotlinx.coroutines.CancellationException

/** Canonical AI orchestration entry point for Aura Studio Avatar. */
class SwarmBrain(
    private val agents: List<SwarmAgent>,
) {
    suspend fun execute(task: SwarmTask): SwarmResult {
        val agent = agents.firstOrNull { it.canHandle(task) }
            ?: return SwarmResult.Failure("No configured agent can handle: ${task.type}")
        return try {
            agent.execute(task)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            SwarmResult.Failure(error.message ?: "Agent execution failed")
        }
    }
}

interface SwarmAgent {
    fun canHandle(task: SwarmTask): Boolean
    suspend fun execute(task: SwarmTask): SwarmResult
}

data class SwarmTask(
    val type: TaskType,
    val input: String,
    val metadata: Map<String, String> = emptyMap(),
)

enum class TaskType {
    CHAT,
    AVATAR_IMAGE,
    AVATAR_VIDEO,
    PERSONA,
    RESEARCH,
}

sealed interface SwarmResult {
    data class Success(val value: String, val metadata: Map<String, String> = emptyMap()) : SwarmResult
    data class Failure(val reason: String) : SwarmResult
}
