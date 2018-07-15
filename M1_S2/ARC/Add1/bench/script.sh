#!/bin/bash

## nettoyage
if [ -d ../libs/LIB_ADD1_BENCH ] 
then /bin/rm -rf ../libs/LIB_ADD1_BENCH
fi

## creation de la librairie de travail
vlib ../libs/LIB_ADD1_BENCH


## compilation
vcom -work LIB_ADD1_BENCH testAdd1.vhd

