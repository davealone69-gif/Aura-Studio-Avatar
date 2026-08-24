#include "llama_wrapper.h"
#include <android/log.h>
#include <mutex>
#include <string>
#include <vector>

#define LOG_TAG "aura_llama"
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#if defined(AURA_HAVE_LLAMA_CPP)
#include "llama.h"
#endif

namespace aura {

static std::mutex g_mutex;
static bool g_loaded = false;
static std::string g_path;
static int g_n_ctx = 2048;

#if defined(AURA_HAVE_LLAMA_CPP)
static llama_model * g_model = nullptr;
static llama_context * g_ctx = nullptr;
static bool g_backend_inited = false;

static void ensure_backend() {
    if (!g_backend_inited) {
        llama_backend_init();
        g_backend_inited = true;
    }
}

static void free_model_unlocked() {
    if (g_ctx) { llama_free(g_ctx); g_ctx = nullptr; }
    if (g_model) { llama_model_free(g_model); g_model = nullptr; }
    g_loaded = false;
    g_path.clear();
}
#endif

bool load_model(const std::string &path, std::string &err) {
    return load_model(path, 2048, 0, err);
}

bool load_model(const std::string &path, int n_ctx, int n_gpu_layers, std::string &err) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (path.empty()) {
        err = "empty model path";
        ALOGE("%s", err.c_str());
        return false;
    }
    g_n_ctx = n_ctx > 0 ? n_ctx : 2048;

#if defined(AURA_HAVE_LLAMA_CPP)
    free_model_unlocked();
    ensure_backend();

    llama_model_params mparams = llama_model_default_params();
    mparams.n_gpu_layers = n_gpu_layers;

    g_model = llama_model_load_from_file(path.c_str(), mparams);
    if (!g_model) {
        err = "llama_model_load_from_file failed: " + path;
        ALOGE("%s", err.c_str());
        return false;
    }

    llama_context_params cparams = llama_context_default_params();
    cparams.n_ctx = static_cast<uint32_t>(g_n_ctx);
    cparams.n_batch = 512;

    g_ctx = llama_init_from_model(g_model, cparams);
    if (!g_ctx) {
        err = "llama_init_from_model failed";
        ALOGE("%s", err.c_str());
        llama_model_free(g_model);
        g_model = nullptr;
        return false;
    }

    g_path = path;
    g_loaded = true;
    ALOGI("GGUF loaded path=%s n_ctx=%d n_gpu_layers=%d", path.c_str(), g_n_ctx, n_gpu_layers);
    return true;
#else
    g_path = path;
    g_loaded = true;
    ALOGI("skeleton load (no AURA_HAVE_LLAMA_CPP) path=%s n_ctx=%d", path.c_str(), g_n_ctx);
    (void)n_gpu_layers;
    return true;
#endif
}

void free_model() {
    std::lock_guard<std::mutex> lock(g_mutex);
#if defined(AURA_HAVE_LLAMA_CPP)
    free_model_unlocked();
#else
    g_loaded = false;
    g_path.clear();
#endif
    ALOGI("free_model");
}

std::string chat_once(const std::string &prompt) {
    return chat("", prompt, 256, 0.7f);
}

std::string chat(
    const std::string &system_prompt,
    const std::string &user_prompt,
    int max_tokens,
    float temperature
) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (!g_loaded) return "ERROR: model not loaded";

    std::string prompt;
    if (!system_prompt.empty()) {
        prompt += system_prompt;
        prompt += "\n\n";
    }
    prompt += user_prompt;

    if (max_tokens <= 0) max_tokens = 256;
    if (temperature < 0.f) temperature = 0.7f;

#if defined(AURA_HAVE_LLAMA_CPP)
    if (!g_model || !g_ctx) return "ERROR: model not loaded";

    const llama_vocab * vocab = llama_model_get_vocab(g_model);
    if (!vocab) return "ERROR: no vocab";

    std::vector<llama_token> tokens;
    tokens.resize(prompt.size() + 8);
    int n = llama_tokenize(
        vocab, prompt.c_str(), static_cast<int32_t>(prompt.size()),
        tokens.data(), static_cast<int32_t>(tokens.size()), true, true);
    if (n < 0) {
        tokens.resize(static_cast<size_t>(-n));
        n = llama_tokenize(
            vocab, prompt.c_str(), static_cast<int32_t>(prompt.size()),
            tokens.data(), static_cast<int32_t>(tokens.size()), true, true);
    }
    if (n < 0) return "ERROR: tokenize failed";
    tokens.resize(static_cast<size_t>(n));

    llama_memory_t mem = llama_get_memory(g_ctx);
    if (mem) llama_memory_clear(mem, true);

    llama_batch batch = llama_batch_get_one(tokens.data(), n);
    if (llama_decode(g_ctx, batch) != 0) return "ERROR: decode prompt failed";

    llama_sampler_chain_params sparams = llama_sampler_chain_default_params();
    llama_sampler * smpl = llama_sampler_chain_init(sparams);
    llama_sampler_chain_add(smpl, llama_sampler_init_temp(temperature));
    llama_sampler_chain_add(smpl, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));

    std::string out;
    out.reserve(static_cast<size_t>(max_tokens) * 4);

    for (int i = 0; i < max_tokens; ++i) {
        llama_token id = llama_sampler_sample(smpl, g_ctx, -1);
        if (llama_vocab_is_eog(vocab, id)) break;
        char buf[256];
        int n_chars = llama_token_to_piece(vocab, id, buf, sizeof(buf), 0, true);
        if (n_chars > 0) out.append(buf, static_cast<size_t>(n_chars));
        batch = llama_batch_get_one(&id, 1);
        if (llama_decode(g_ctx, batch) != 0) break;
    }

    llama_sampler_free(smpl);
    ALOGI("chat generated %zu chars", out.size());
    return out.empty() ? std::string("(empty generation)") : out;
#else
    (void)temperature;
    return std::string("[aura_llama skeleton — vendor llama.cpp under cpp/llama.cpp] ")
        + prompt.substr(0, 400);
#endif
}

bool is_loaded() {
    std::lock_guard<std::mutex> lock(g_mutex);
    return g_loaded;
}

ModelStats get_stats() {
    std::lock_guard<std::mutex> lock(g_mutex);
    ModelStats s{};
    s.memoryUsed = 0;
    s.maxContext = static_cast<size_t>(g_n_ctx);
#if defined(AURA_HAVE_LLAMA_CPP)
    if (g_ctx) s.maxContext = static_cast<size_t>(llama_n_ctx(g_ctx));
#endif
    return s;
}

} // namespace aura
