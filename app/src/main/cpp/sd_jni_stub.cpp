#include <jni.h>
#include <android/log.h>
#define LOG_TAG "AuraSD"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
extern "C" {
JNIEXPORT jboolean JNICALL
Java_com_aura_studio_nativebridge_SdBridge_load(JNIEnv *env, jobject, jstring path) {
    const char *p = env->GetStringUTFChars(path, nullptr);
    LOGI("sd load stub: %s", p);
    env->ReleaseStringUTFChars(path, p);
    return JNI_FALSE;
}
JNIEXPORT void JNICALL
Java_com_aura_studio_nativebridge_SdBridge_free(JNIEnv *, jobject) { LOGI("sd free stub"); }
JNIEXPORT jintArray JNICALL
Java_com_aura_studio_nativebridge_SdBridge_txt2img(JNIEnv *, jobject, jstring, jstring, jint, jint, jint, jfloat, jlong) {
    return nullptr;
}
}
