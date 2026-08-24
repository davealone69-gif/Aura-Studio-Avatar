# Native Llama JNI bridge (#12)

## Package (important)

Use **`com.aura.studio.nativebridge.LlamaBridge`** only.

Do **not** add `com.aura.studio.native` — it conflicts with this app.

## Files

| Path | Role |
|------|------|
| `app/src/main/cpp/CMakeLists.txt` | Builds `libaura_llama.so` (+ sd/video stubs) |
| `app/src/main/cpp/include/llama_wrapper.h` | Simple + extended C++ API |
| `app/src/main/cpp/llama_wrapper.cpp` | Session + skeleton inference |
| `app/src/main/cpp/llama_bridge.cpp` | JNI for nativebridge |
| `app/src/main/java/.../nativebridge/LlamaBridge.kt` | Kotlin load/chat/free + stats |

## JNI entry points

Primary (Kotlin `external fun`):

- `load(path, nCtx, nGpuLayers)`
- `chat(system, user, maxTokens, temperature)`
- `free()`
- `nativeIsLoaded` / `nativeGetMemoryUsed` / `nativeGetMaxContext`

Also exported (Copilot-style names under **nativebridge**):

- `nativeLoad(path)`
- `nativeChat(prompt)`
- `nativeFree()`

## Build

```bash
./gradlew :app:assembleDebug -Paura.enableNdk=true
```

Without the flag, Kotlin builds and uses the simulator path.

## Acceptance

- [x] CMake + wrapper + JNI present
- [x] Kotlin `LlamaBridge` loads library safely
- [x] Missing path / unload / memory helpers for OOM-safe generate
- [ ] Real GGUF tokens (fill TODOs + vendor llama.cpp)

## Next

1. Vendor llama.cpp under `app/src/main/cpp/`
2. Replace TODOs in `llama_wrapper.cpp`
3. Link `llama` in CMakeLists
