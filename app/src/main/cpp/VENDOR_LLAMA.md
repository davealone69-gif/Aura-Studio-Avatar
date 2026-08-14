# Vendor llama.cpp (real GGUF inference)

JNI names stay unchanged (`com.aura.studio.nativebridge.LlamaBridge`).

## Add submodule

```bash
git submodule add https://github.com/ggerganov/llama.cpp.git app/src/main/cpp/llama.cpp
git submodule update --init --recursive
```

Also accepted path: `app/src/main/cpp/llama_cpp`.

## Build

```bash
./gradlew :app:assembleDebug -Paura.enableNdk=true
```

CMake sets `AURA_HAVE_LLAMA_CPP` and links `llama`.

Without the submodule, NDK still builds a **skeleton** `.so` (CI-safe).

## Device test

1. Pick Dolphin GGUF in **Models**
2. Opinion / Enhance should not start with `[aura_llama skeleton`

## Notes

- Kotlin `loadSafe` default `n_ctx = 2048`
- `n_gpu_layers = 0` → CPU
