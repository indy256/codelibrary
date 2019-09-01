cmake_minimum_required(VERSION 3.11.4)
project(codelibrary)

set(CMAKE_CXX_STANDARD 17)
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")

#set(Boost_DEBUG 1)
#set(BOOST_ROOT D:/boost_1_70_0)
#set(BOOST_LIBRARYDIR ${BOOST_ROOT}/stage/lib)
#find_package(Boost REQUIRED regex)
#include_directories(${BOOST_ROOT})
#link_directories(${BOOST_LIBRARYDIR})

file(GLOB_RECURSE SOURCE_FILES "*.cpp")
add_executable(dummy-aggregate ${SOURCE_FILES})

add_executable(java8-quicksort-anti-test misc/java8-anti-quicksort-test.cpp)

add_executable(test structures/ordered_set.cpp)
