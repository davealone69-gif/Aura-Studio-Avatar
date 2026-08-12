#include <jni.h>
#include <android/log.h>
#define LOG_TAG "AuraVideo"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
extern "C" {
JNIEXPORT jboolean JNICALL
Java_com_aura_studio_nativebridge_VideoBridge_load(JNIEnv *env, jobject, jstring path) {
    const char *p = env->GetStringUTFChars(path, nullptr);
    LOGI("video load stub: %s", p);
    env->ReleaseStringUTFChars(path, p);
    return JNI_FALSE;
}
JNIEXPORT void JNICALL
Java_com_aura_studio_nativebridge_VideoBridge_free(JNIEnv *, jobject) { LOGI("video free stub"); }
JNIEXPORT jstring JNICALL
Java_com_aura_studio_nativebridge_VideoBridge_txt2video(JNIEnv *, jobject, jstring, jstring, jint, jint, jint, jint, jint, jlong) {
    return nullptr;
}
}
