#include "llama_wrapper.h"
#include <android/log.h>
#include <mutex>

#define LOG_TAG "aura_llama"
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace aura {

static std::mutex g_mutex;
static bool g_loaded = false;
static std::string g_path;
static int g_n_ctx = 4096;

void set_n_ctx(int n_ctx) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (n_ctx > 0) g_n_ctx = n_ctx;
}

bool load_model(const std::string &path, std::string &err) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (path.empty()) {
        err = "empty model path";
        ALOGE("%s", err.c_str());
        return false;
    }
    // TODO: llama.cpp load API
    g_path = path;
    g_loaded = true;
    ALOGI("load_model path=%s n_ctx=%d", path.c_str(), g_n_ctx);
    return true;
}

void free_model() {
    std::lock_guard<std::mutex> lock(g_mutex);
    g_loaded = false;
    g_path.clear();
    ALOGI("free_model");
}

std::string chat_once(const std::string &prompt) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (!g_loaded) return std::string("ERROR: model not loaded");
    // TODO: real inference
    ALOGI("chat_once prompt_len=%zu", prompt.size());
    return std::string("simulated response");
}

bool is_loaded() {
    std::lock_guard<std::mutex> lock(g_mutex);
    return g_loaded;
}

ModelStats get_stats() {
    std::lock_guard<std::mutex> lock(g_mutex);
    return ModelStats{0, static_cast<size_t>(g_n_ctx)};
}

} // namespace aura
