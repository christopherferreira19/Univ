cmake_minimum_required(VERSION 2.8)
project(tinyproxy C)

set(CMAKE_C_COMPILER gcc)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -pedantic -g -std=c99 -D_GNU_SOURCE")

set(SOURCE_FILES_TINYPROXY
        tinyproxy.c
        utils/csapp.c
        utils/csapp.h
        http.c
        http.h
        logs.c
        logs.h
        filtering.c
        filtering.h
        utils/strmap.c
        utils/strmap.h
        utils/url_parser.c
        utils/url_parser.h
        server.c
        server.h
        utils/strutils.c
        utils/strutils.h
        globalPath.h
        filterWatch.c
        filterWatch.h)

Set(SOURCE_FILES_VALIDCONFIGUPDATE
        validConfigUpdate.c
        utils/csapp.c
        utils/csapp.h
        filtering.c
        filtering.h
        utils/strmap.c
        utils/strmap.h
        utils/url_parser.c
        utils/url_parser.h
        utils/strutils.c
        utils/strutils.h
        globalPath.h)

add_executable(tinyproxy ${SOURCE_FILES_TINYPROXY})
target_link_libraries(tinyproxy pthread rt)

add_executable(valid_config_update ${SOURCE_FILES_VALIDCONFIGUPDATE})
set_target_properties(valid_config_update
        PROPERTIES
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/config")
target_link_libraries(valid_config_update pthread rt)