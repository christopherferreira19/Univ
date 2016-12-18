PROG_SRC=main.c

PROG_BIN=$(patsubst %.c,%, $(PROG_SRC))

# Variables for file compilation
CC        =  gcc
CFLAGS    =  -g -Wall
CPPFLAGS  =  -DDEBUG
LDFLAGS   =  -g -lm

# Rules
all : $(PROG_BIN)

${PROG_BIN}: % : %.o Util.o pgm_io.o dmap.o

.PHONY: jpg
jpg:
	for img in img/*.pgm; do ppmtojpeg "$$img" > img/$$(basename $$img .pgm).jpg; done

.PHONY: clean
clean :
	@rm -f *.o

.PHONY: cleanimg
cleanimg:
	@rm -f img/LenaDark_*
	@rm -f img/*.jpg

.PHONY: cleanall
cleanall : clean cleanimg
	@rm -f $(PROG_BIN)
