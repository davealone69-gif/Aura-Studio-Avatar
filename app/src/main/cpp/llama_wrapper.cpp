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
static int g_n_ctx = 2048;

bool load_model(const std::string &path, int n_ctx, int n_gpu_layers, std::string &err) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (path.empty()) {
        err = "empty model path";
        ALOGE("%s", err.c_str());
        return false;
    }
    g_n_ctx = n_ctx > 0 ? n_ctx : 2048;
    g_path = path;
    // TODO: llama_model_load_from_file + llama_init_from_model
    g_loaded = true;
    ALOGI("load_model skeleton OK path=%s n_ctx=%d n_gpu_layers=%d", path.c_str(), g_n_ctx, n_gpu_layers);
    (void)n_gpu_layers;
    return true;
}

void free_model() {
    std::lock_guard<std::mutex> lock(g_mutex);
    g_loaded = false;
    g_path.clear();
    ALOGI("free_model");
}

std::string chat(const std::string &system_prompt, const std::string &user_prompt, int max_tokens, float temperature) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (!g_loaded) return "ERROR: model not loaded";
    (void)max_tokens;
    (void)temperature;
    return "[aura_llama skeleton] sys=" + system_prompt.substr(0, 80) + " | user=" + user_prompt.substr(0, 200);
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
