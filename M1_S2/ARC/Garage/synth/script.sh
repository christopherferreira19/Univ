#!/bin/bash

# source ../confmodelsim/config

## compilation
vcom -work LIB_GARAGE garage_binary.vhd -cover bfs
vcom -work LIB_GARAGE garage_onehot2.vhd -cover bfs
vcom -work LIB_GARAGE garage_gray2.vhd -cover bfs
