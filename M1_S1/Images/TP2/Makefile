#######################################
# Makefile PBM                        #
#                                     #
# E.B.                                #
#######################################

PROG_SRC=\
		binomial_filter22.c \
		binomial_filter44.c \
		median_filter.c \
		histogram \
		histogram_stretching.c \
		histogram_equalization.c

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

