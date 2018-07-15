#!/bin/bash

# source ../confmodelsim/config 

## nettoyage
if [ -d ../libs/LIB_ADD1 ] 
then /bin/rm -rf ../libs/LIB_ADD1 
fi

## creation de la librairie de travail
vlib ../libs/LIB_ADD1

## compilation
vcom -work LIB_ADD1 add1.vhd
