cmake_minimum_required(VERSION 3.8)
project(HWC2)

find_package (Threads)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES main.c HWC1/buffer.h HWC1/msg.h HWC1/buffer.c HWC1/buffer_test.c HWC1/msg_string.h HWC1/msg_string.c provider.c provider.h dispatcher.c dispatcher.h poison_pill.c poison_pill.h hwc2list/list.c hwc2list/list.h hwc2list/test hwc2list/TestDriver.c reader.c reader.h)
add_executable(HWC2 ${SOURCE_FILES})


target_link_libraries (HWC2 ${CMAKE_THREAD_LIBS_INIT})

TARGET_LINK_LIBRARIES (HWC2 cunit)
