#!/bin/bash

# source ../confmodelsim/config 

## nettoyage
if [ -d ../libs/LIB_PARKING ] 
then /bin/rm -rf ../libs/LIB_PARKING 
fi

## creation de la librairie de travail
vlib ../libs/LIB_PARKING

## compilation
vcom -work LIB_PARKING parking1.vhd
