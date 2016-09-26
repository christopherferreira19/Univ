#!/bin/bash
MY_PATH=$(dirname $(readlink -f $0))
cd $MY_PATH

PKG="student_elf_linker-1.0.tar.gz"
PKG_FOLDER="elf_linker-1.0"
ARM_SIM="arm_simulator"

if [ ! -f $PKG ]; then
	#http://inf352.forge.imag.fr/Projet/Editeur_de_liens/
	wget http://inf352.forge.imag.fr/Projet/Editeur_de_liens/mandelbrot_2015-2016/$PKG
else
	echo "$PKG already downloaded"
fi

if  [ ! -d $PKG_FOLDER ]; then
	echo "Extraction of $PKG in $PKG_FOLDER"	
	tar zxvf $PKG
else
	echo "$PKG_FOLDER already extracted"
fi

if  [ -z $ARM_PREFIX ]; then
	echo "Need to ./setenvarm.sh for $PATH/.configure"
	cd $PKG_FOLDER
	./configure
	make
else
	echo "./setenvarm.sh ok"
	cd $PKG_FOLDER
	./configure
	make
fi

if  [ ! -f $ARM_SIM ]; then
	#http://inf352.forge.imag.fr/Projet/Editeur_de_liens/
	wget http://inf352.forge.imag.fr/Projet/Editeur_de_liens/mandelbrot_2015-2016/$ARM_SIM
	chmod +x $ARM_SIM
else
	echo "arm_simulator already downloaded"
fi

exit 0