#!/bin/bash
MY_PATH=$(dirname $(readlink -f $0))
cd $MY_PATH

PKG="elfutils_0.158.orig.tar.bz2"
PKG_FOLDER="elfutils-0.158"
PKG_INSTALL_FOLDER="$PKG_FOLDER"_install

if [ ! -f $PKG ]; then
	#https://launchpad.net/ubuntu/+source/elfutils/0.158-0ubuntu5.2
	wget https://launchpad.net/ubuntu/+archive/primary/+files/$PKG
else
	echo "$PKG already downloaded"
fi

if  [ ! -d $PKG_FOLDER ]; then
	echo "Extraction of $PKG in $PKG_FOLDER"	
	tar jxf $PKG
else
	echo "$PKG_FOLDER already extracted"
fi

if  [ ! -d $PKG_INSTALL_FOLDER ]; then
	echo "Creation of $PKG_INSTALL_FOLDER"	
	mkdir $PKG_INSTALL_FOLDER
	
	PWD_OLD=$PWD
	cd $PKG_FOLDER

	aclocal
	autoconf
	./configure --prefix="$PWD_OLD/$PKG_INSTALL_FOLDER"
	make -s
	make -s install

else
	echo "$PKG_INSTALL_FOLDER already created"
fi