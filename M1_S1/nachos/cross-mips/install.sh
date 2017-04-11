#! /bin/bash

# Starting inside directory cross-mips
DST=$PWD/target/
pushd source

    # Binutils (Also installs `ld` and `as`)
    dtrx binutils-*.tar.*
    pushd binutils-*/
        ./configure --target=mipsel-linux-gnu --prefix $DST
        make
        make install
    popd

    # GCC
    dtrx gcc-*.tar.*
    pushd gcc-*/
        ./configure --target=mipsel-linux-gnu --with-gnu-as --with-gnu-ld --prefix $DST
        touch gcc/libgcc.a
        touch gcc/libgcc.mvars
        touch gcc/libgcc2.a
        touch gcc/libgcc2.mvars
        make LANGUAGES=c
        # Error related to libgcc, ignore
        make install LANGUAGES=c
        # Error related to libgcc, ignore
    popd

popd
