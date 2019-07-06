cmake_minimum_required(VERSION 3.4.1)

set(lib_DIR ${CMAKE_CURRENT_SOURCE_DIR})

add_library(libcrypto STATIC IMPORTED )

set_target_properties(libcrypto PROPERTIES IMPORTED_LOCATION ${lib_DIR}/openssl/lib/${ANDROID_ABI}/libcrypto.so)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             cBase64.cpp
             cJSON.cpp
             Packet_Tools.cpp
             native-lib.cpp
             TRC4.cpp
             GenerateKey.cpp
             ZlibDll.cpp
             )


find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_include_directories(native-lib PRIVATE
                           ${lib_DIR}/openssl/include
                           )

target_link_libraries( # Specifies the target library.
                       native-lib
                       libcrypto
                       ${log-lib} )