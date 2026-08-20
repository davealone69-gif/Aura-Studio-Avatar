package com.aurastudio.avatar.ai.swarm

/** Selects the best available agent without coupling UI to providers. */
class AgentRouter(
    private val agents: List<SwarmAgent>,
) {
    fun route(task: SwarmTask): SwarmAgent? = agents
        .asSequence()
        .filter { it.canHandle(task) }
        .firstOrNull()
}
