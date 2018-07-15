#!/bin/bash

## nettoyage
if [ -d ../libs/LIB_GARAGE ]
then /bin/rm -rf ../libs/LIB_GARAGE
fi

## creation de la librairie de travail
vlib ../libs/LIB_GARAGE


## compilation
vcom -work LIB_GARAGE garage.vhd -cover bfs
