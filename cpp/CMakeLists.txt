cmake_minimum_required(VERSION 3.1)
project(codelibrary)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")

file(GLOB_RECURSE SOURCE_FILES "*.cpp")
add_executable(dummy-aggregate ${SOURCE_FILES})

add_executable(java8-quicksort-anti-test misc/java8-anti-quicksort-test.cpp)

add_executable(test structures/heavy_light_decomposition.cpp)
