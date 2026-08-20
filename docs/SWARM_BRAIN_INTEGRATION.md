# Aura Studio Avatar Swarm Brain

Aura Studio Avatar is the canonical product. Swarm capabilities are integrated as an internal AI orchestration layer, not as a second app.

## Sources audited
- Swarm-OS: orchestration/agent command-centre concepts.
- MandelaMatrixOS: LLM client/model abstraction and AI-oriented modules.
- ai-generated-android-app-3: candidate AI implementation source, to be selectively integrated only after source-level verification.

## Rules
- Keep Aura Studio Avatar's Android architecture authoritative.
- Copy only source components that are self-contained, licensed/owned, buildable, and useful.
- No placeholder providers, fake model responses, or dead example endpoints.
- Providers must be isolated behind a common interface.
- API credentials never belong in source control.
- Local/offline fallbacks remain available where practical.
- Swarm orchestration must not block normal avatar/chat/studio operation.

## Target brain
Avatar UI -> ViewModels -> SwarmBrain -> AgentRouter -> Provider adapters -> Gemini/OpenAI/other configured providers -> Media/Memory repositories.

SwarmBrain is responsible for selecting an appropriate agent/provider, maintaining task state, and returning structured results. UI code must not call provider APIs directly.
