cmake_minimum_required(VERSION 3.8)
project(HWC1)

find_package (Threads)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES main.c buffer.h msg.h buffer.c buffer_test.c msg_string.h msg_string.c)
add_executable(HWC1 ${SOURCE_FILES})

target_link_libraries (HWC1 ${CMAKE_THREAD_LIBS_INIT})

TARGET_LINK_LIBRARIES (HWC1 cunit)