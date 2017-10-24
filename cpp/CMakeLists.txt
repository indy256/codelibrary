cmake_minimum_required(VERSION 3.1)
project(codelibrary)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES ConvexHull.cpp)
add_executable(codelibrary ${SOURCE_FILES})