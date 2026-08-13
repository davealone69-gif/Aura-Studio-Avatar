# Acceptance checklist

Copy and tick as you verify on device / CI.

## Functional

- [ ] **Opinion** updates all avatar fields offline (simulator or native).
- [ ] **Missing GGUF** shows a clear error and a Models CTA.
- [ ] **Generate + room** appends the scene fragment to the prompt and writes a `GenerationEntity`.
- [ ] **LLM unload** runs before every image/video generation (`AIService.generate` → `DolphinService.unload()`).
- [ ] **With native + GGUF:** chat/opinion tokens come from Dolphin (not skeleton text).
- [ ] **With SD linked:** a real image is produced and can be saved to Pictures/AuraStudio.
- [ ] **Diagnostics Auto-repair** does **not** clear generation history.

## Build

- [ ] `./gradlew :app:assembleDebug` succeeds **without** NDK (JDK 17).
- [ ] `./gradlew :app:assembleDebug -Paura.enableNdk=true` succeeds with NDK + CMake.
- [ ] CI (`.github/workflows/android-ci.yml`) green on `main` / PRs.

## Package rules (do not violate)

| Allowed | Forbidden |
|---------|-----------|
| `com.aura.studio.nativebridge` | `com.aura.studio.native` |
| `com.aura.studio.ai.DolphinService` | `com.aura.studio.llm.DolphinServiceImpl` |
| NDK via `-Paura.enableNdk=true` | Always-on `externalNativeBuild` |

## Assets / inputs for the coder

Provide these before P0 (real inference):

| Input | Example |
|-------|--------|
| Dolphin GGUF | Exact file, e.g. `dolphin-3.0-llama3.1-8b.Q4_K_M.gguf` (or `Dolphin-3.0.gguf` if you standardize that name) |
| SD model for phone | Filename + quant, e.g. mobile-friendly ONNX/GGUF/safetensors |
| Min Android API | e.g. 26+ |
| Min RAM | e.g. 4 GB |
| Distribution | Sideload only vs Play Store |
| Style refs | Reference images / notes already shared in chat |

## Suggested order

1. P0 real llama.cpp + GGUF  
2. P0 SD native  
3. P1 model path UX  
4. P2 generate polish  
5. P4 quality / empty states / icon  
6. P3 game layer (optional)  
