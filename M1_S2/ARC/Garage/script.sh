#!/bin/bash

pushd ~/projet_asic/Garage/srcvhd
./script.sh
popd

pushd ~/projet_asic/Garage/synth
./script.sh
popd

pushd ~/projet_asic/Garage/bench 
./script.sh
popd

vsim -coverage -assertdebug LIB_GARAGE_BENCH.config1
