#######################################
# Makefile PBM                        #
#                                     #
# E.B.                                #
#######################################

PROG_SRC=sobel.c

PROG_BIN=$(patsubst %.c,%, $(PROG_SRC))

# Variables for file compilation
CC        =  gcc
CFLAGS    =  -g -Wall
CPPFLAGS  =  -DDEBUG
LDFLAGS   =  -g -lm

# Rules
all : $(PROG_BIN)

${PROG_BIN}: % : %.o Util.o pgm_io.o

clean :
	@rm -f *.o

cleanall : clean
	@rm -f $(PROG_BIN)

