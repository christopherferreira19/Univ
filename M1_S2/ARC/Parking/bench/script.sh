#!/bin/bash

## nettoyage
if [ -d ../libs/LIB_PARKING_BENCH ] 
then /bin/rm -rf ../libs/LIB_PARKING_BENCH
fi

## creation de la librairie de travail
vlib ../libs/LIB_PARKING_BENCH


## compilation
vcom -work LIB_PARKING_BENCH testParking.vhd

