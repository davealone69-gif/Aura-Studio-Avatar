#include <jni.h>
#include <string>
#include "llama_wrapper.h"

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_load(
    JNIEnv *env, jobject, jstring jpath, jint nCtx, jint
) {
    if (jpath == nullptr) return JNI_FALSE;
    const char *path = env->GetStringUTFChars(jpath, nullptr);
    if (path == nullptr) return JNI_FALSE;
    aura::set_n_ctx((int)nCtx);
    std::string err;
    bool ok = aura::load_model(std::string(path), err);
    env->ReleaseStringUTFChars(jpath, path);
    return ok ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_chat(
    JNIEnv *env, jobject, jstring jsystem, jstring juser, jint, jfloat
) {
    const char *sys = jsystem ? env->GetStringUTFChars(jsystem, nullptr) : nullptr;
    const char *user = juser ? env->GetStringUTFChars(juser, nullptr) : nullptr;
    std::string prompt;
    if (sys && *sys) { prompt += sys; prompt += "\n\n"; }
    if (user) prompt += user;
    std::string out = aura::chat_once(prompt);
    if (jsystem && sys) env->ReleaseStringUTFChars(jsystem, sys);
    if (juser && user) env->ReleaseStringUTFChars(juser, user);
    return env->NewStringUTF(out.c_str());
}

JNIEXPORT void JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_free(JNIEnv *, jobject) {
    aura::free_model();
}

JNIEXPORT jboolean JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_nativeIsLoaded(JNIEnv *, jobject) {
    return aura::is_loaded() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_nativeGetMemoryUsed(JNIEnv *, jobject) {
    return (jlong)aura::get_stats().memoryUsed;
}

JNIEXPORT jint JNICALL
Java_com_aura_studio_nativebridge_LlamaBridge_nativeGetMaxContext(JNIEnv *, jobject) {
    return (jint)aura::get_stats().maxContext;
}

} // extern "C"
