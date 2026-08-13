#ifndef AURA_LLAMA_WRAPPER_H
#define AURA_LLAMA_WRAPPER_H

#include <cstddef>
#include <string>

namespace aura {

struct ModelStats {
    size_t memoryUsed;
    size_t maxContext;
};

bool load_model(const std::string &path, std::string &err);
void free_model();
std::string chat_once(const std::string &prompt);
bool is_loaded();
ModelStats get_stats();
void set_n_ctx(int n_ctx);

} // namespace aura

#endif
