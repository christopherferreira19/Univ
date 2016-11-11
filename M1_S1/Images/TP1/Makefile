#######################################
# Makefile PBM                        #
#                                     #
# E.B.                                #
#######################################


PROG = pgmtopgm ppmto3ppm ppmtopgm ppmtobrighterpgm

all : $(PROG)

# Variables for file compilation
CC        =  gcc
CFLAGS    =  -g -Wall
CPPFLAGS  =  -DDEBUG
LDFLAGS   =  -g -lm

#pxmtopxm : pxmtopxm.o Util.o

pgmtopgm : pgmtopgm.o Util.o

ppmto3ppm : ppmto3ppm.o Util.o

ppmtopgm : ppmtopgm.o Util.o

ppmtobrighterpgm : ppmtobrighterpgm.o Util.o

clean :
	@rm -f *.o

cleanall : clean
	@rm -f $(PROG)

