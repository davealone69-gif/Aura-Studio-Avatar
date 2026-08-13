# Native integration (no package moves)

## Package (locked)

| Use | Do not use |
|-----|------------|
| `com.aura.studio.nativebridge` | `com.aura.studio.native` |
| `com.aura.studio.ai.DolphinService` | `com.aura.studio.llm.*` |

## A — Sources (already present)

```
app/src/main/cpp/
  CMakeLists.txt
  include/llama_wrapper.h
  llama_wrapper.cpp
  llama_bridge.cpp
  sd_jni_stub.cpp
  video_jni_stub.cpp

app/src/main/java/com/aura/studio/nativebridge/
  LlamaBridge.kt   # soft-fails if .so missing
  SdBridge.kt
  VideoBridge.kt
```

**Debug stubs:** do **not** add a second `LlamaBridge` under `src/debug/java/...` while the main class exists — that causes **duplicate class** errors.  
`LlamaBridge` already does `try { System.loadLibrary("aura_llama") } catch ...` so Kotlin-only builds work.

## C — Conditional NDK (already in `app/build.gradle.kts`)

```kotlin
val enableNdk: Boolean =
    (project.findProperty("aura.enableNdk") as? String)?.equals("true", ignoreCase = true) == true

if (enableNdk) {
    // abiFilters arm64-v8a + externalNativeBuild cmake path
}
```

```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleDebug -Paura.enableNdk=true
```

## B — CI (already)

`.github/workflows/android-ci.yml`

- Job **assemble-kotlin**: no NDK  
- Job **assemble-ndk**: installs NDK + `-Paura.enableNdk=true`  

Main stays safe: default path never requires NDK.

## PR checklist (coder)

- [ ] No `com.aura.studio.native` package  
- [ ] No new `DolphinServiceImpl` / no move of `ai.DolphinService`  
- [ ] C++ only under `app/src/main/cpp/`  
- [ ] JNI names = `Java_com_aura_studio_nativebridge_LlamaBridge_*`  
- [ ] `externalNativeBuild` only when `aura.enableNdk=true`  
- [ ] Feature branch + PR (avoid direct main for large native diffs)  
- [ ] Kotlin assemble green without NDK  
- [ ] Optional NDK assemble green with flag  

## Next real work

1. Vendor llama.cpp under `cpp/`  
2. Fill TODOs in `llama_wrapper.cpp`  
3. Link in CMakeLists  
4. Device test Opinion/Enhance with real GGUF  
