#!/bin/bash

## nettoyage
if [ -d ../libs/LIB_GARAGE_BENCH ]
then /bin/rm -rf ../libs/LIB_GARAGE_BENCH
fi

## creation de la librairie de travail
vlib ../libs/LIB_GARAGE_BENCH


## compilation
vcom -work LIB_GARAGE_BENCH testGarage.vhd -cover bfs

