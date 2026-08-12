cmake_minimum_required(VERSION 3.22.1)
project(aura_native)
set(CMAKE_CXX_STANDARD 17)

add_library(aura_llama SHARED llama_jni_stub.cpp)
target_link_libraries(aura_llama android log)

add_library(aura_sd SHARED sd_jni_stub.cpp)
target_link_libraries(aura_sd android log jnigraphics)

add_library(aura_video SHARED video_jni_stub.cpp)
target_link_libraries(aura_video android log)
