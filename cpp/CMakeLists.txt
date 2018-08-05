cmake_minimum_required(VERSION 3.1)
project(codelibrary)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")

set(SOURCE_FILES graphs/flows/max_flow_dinic.cpp)
add_executable(codelibrary ${SOURCE_FILES})