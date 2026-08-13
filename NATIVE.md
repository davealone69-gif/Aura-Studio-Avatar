# Native backends

## Model paths

| Role | Example |
|------|--------|
| LLM | `/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf` or SAF URI from Models picker |
| Image / Video | Models picker → DataStore |

## Build with NDK

```bash
./gradlew :app:assembleDebug -Paura.enableNdk=true
```

arm64-v8a only. Without the flag, pure Kotlin builds (simulator).

## Real llama.cpp

1. Vendor under `app/src/main/cpp/llama.cpp`
2. CMake: link `llama` to `aura_llama`
3. Fill TODOs in `llama_wrapper.cpp`

JNI package: `com.aura.studio.nativebridge.LlamaBridge`

## OOM

`n_ctx` default 2048. Unload LLM before SD. Use Diagnostics + `memoryUsed()`.
