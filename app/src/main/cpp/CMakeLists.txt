cmake_minimum_required(VERSION 3.22.1)
project(aura_native)

set(CMAKE_CXX_STANDARD 17)

# Optional llama.cpp:
#   git submodule add https://github.com/ggerganov/llama.cpp.git app/src/main/cpp/llama.cpp
set(LLAMA_DIR "")
if (EXISTS "${CMAKE_SOURCE_DIR}/llama.cpp/CMakeLists.txt")
    set(LLAMA_DIR "${CMAKE_SOURCE_DIR}/llama.cpp")
elseif (EXISTS "${CMAKE_SOURCE_DIR}/llama_cpp/CMakeLists.txt")
    set(LLAMA_DIR "${CMAKE_SOURCE_DIR}/llama_cpp")
endif()

set(AURA_LLAMA_DEFINES "")
if (LLAMA_DIR)
    message(STATUS "Aura: found llama.cpp at ${LLAMA_DIR}")
    set(LLAMA_BUILD_TESTS OFF CACHE BOOL "" FORCE)
    set(LLAMA_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
    set(LLAMA_BUILD_SERVER OFF CACHE BOOL "" FORCE)
    set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
    add_subdirectory(${LLAMA_DIR} ${CMAKE_BINARY_DIR}/llama.cpp)
    list(APPEND AURA_LLAMA_DEFINES AURA_HAVE_LLAMA_CPP)
else()
    message(STATUS "Aura: llama.cpp not vendored — skeleton libaura_llama")
endif()

add_library(aura_llama SHARED
    llama_bridge.cpp
    llama_wrapper.cpp
)
target_include_directories(aura_llama PRIVATE ${CMAKE_SOURCE_DIR}/include)
target_compile_options(aura_llama PRIVATE -O3 -fPIC)
if (AURA_LLAMA_DEFINES)
    target_compile_definitions(aura_llama PRIVATE ${AURA_LLAMA_DEFINES})
    target_include_directories(aura_llama PRIVATE ${LLAMA_DIR}/include ${LLAMA_DIR})
    if (TARGET llama)
        target_link_libraries(aura_llama llama android log)
    else()
        target_link_libraries(aura_llama android log)
        message(WARNING "Aura: llama target not found after add_subdirectory")
    endif()
else()
    target_link_libraries(aura_llama android log)
endif()

add_library(aura_sd SHARED sd_jni_stub.cpp)
target_link_libraries(aura_sd android log jnigraphics)

add_library(aura_video SHARED video_jni_stub.cpp)
target_link_libraries(aura_video android log)
