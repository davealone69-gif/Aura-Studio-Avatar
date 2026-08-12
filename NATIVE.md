# Native backends (llama.cpp / SD / Video)

## Libraries

| Lib | Kotlin | C++ stub | Purpose |
|-----|--------|----------|--------|
| `libaura_llama.so` | `LlamaBridge` | `llama_jni_stub.cpp` | Dolphin / GGUF chat |
| `libaura_sd.so` | `SdBridge` | `sd_jni_stub.cpp` | Text-to-image |
| `libaura_video.so` | `VideoBridge` | `video_jni_stub.cpp` | Text-to-video |

Stubs always build. Real inference requires replacing stubs with real backends.

## Enable NDK in app/build.gradle.kts

```kotlin
android {
    defaultConfig {
        ndk { abiFilters += listOf("arm64-v8a") }
        externalNativeBuild {
            cmake {
                cppFlags += "-O3"
                arguments += listOf("-DANDROID_STL=c++_shared")
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}
```

## Models on device

```
/sdcard/Models/dolphin-3.0-*.Q4_K_M.gguf
/sdcard/Models/*.safetensors
```

Register paths in the app Models screen. No content filters in the intended path.
