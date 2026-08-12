#include <jni.h>
#include <android/log.h>
#define LOG_TAG "AuraLlama"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
extern "C" {
JNIEXPORT jboolean JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_load(JNIEnv *env, jobject, jstring path, jint nCtx, jint nGpuLayers) {
    const char *p = env->GetStringUTFChars(path, nullptr);
    LOGI("llama load stub: %s", p);
    env->ReleaseStringUTFChars(path, p);
    return JNI_FALSE;
}
JNIEXPORT void JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_free(JNIEnv *, jobject) { LOGI("llama free stub"); }
JNIEXPORT jstring JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_chat(JNIEnv *env, jobject, jstring, jstring, jint, jfloat) {
    return env->NewStringUTF("[native llama not linked]");
}
}
