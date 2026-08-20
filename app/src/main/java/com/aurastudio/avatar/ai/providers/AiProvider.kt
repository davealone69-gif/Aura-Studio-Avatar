package com.aurastudio.avatar.ai.providers

import com.aurastudio.avatar.ai.swarm.SwarmTask
import com.aurastudio.avatar.ai.swarm.SwarmResult

/** Provider boundary used by the swarm. Implementations own network/API details. */
interface AiProvider {
    val id: String
    fun supports(task: SwarmTask): Boolean
    suspend fun execute(task: SwarmTask): SwarmResult
}
