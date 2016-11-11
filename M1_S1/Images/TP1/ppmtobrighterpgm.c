#include <stdlib.h>
#include <stdio.h>
#include "Util.h"
#include "string.h"

typedef struct {
    unsigned char red;
    unsigned char green;
    unsigned char blue;
} color;

#define BRIGHTER

int main(int argc, char* argv[]) {
    FILE* ifp;
    color* colormap;
    int ich1, ich2, rows, cols, maxval=255, pgmraw;
    int i, j;

    /* Arguments */
    if ( argc != 2 ){
      printf("\nUsage: %s file \n\n", argv[0]);
      exit(0);
    }

    /* Opening */
    ifp = fopen(argv[1],"r");
    if (ifp == NULL) {
      printf("error in opening file %s\n", argv[1]);
      exit(1);
    }

    /*  Magic number reading */
    ich1 = getc( ifp );
    if ( ich1 == EOF )
        pm_erreur( "EOF / read error / magic number" );
    ich2 = getc( ifp );
    if ( ich2 == EOF )
        pm_erreur( "EOF /read error / magic number" );
    if(ich2 != '3' && ich2 != '6')
      pm_erreur(" wrong file type ");
    else
      if(ich2 == '3')
	     pgmraw = 0;
      else pgmraw = 1;

    /* Reading image dimensions */
    cols = pm_getint( ifp );
    rows = pm_getint( ifp );
    maxval = pm_getint( ifp );

    /* Memory allocation  */
    colormap = (color *) malloc(cols * rows * sizeof(color));

    /* Reading */
    for(i=0; i < rows; i++)
      for(j=0; j < cols ; j++) {
        color* c = &(colormap[i * cols + j]);
        if(pgmraw) {
          c->red = pm_getrawbyte(ifp) ;
          c->green = pm_getrawbyte(ifp) ;
          c->blue = pm_getrawbyte(ifp) ;
        }
        else {
          c->red = pm_getint(ifp);
          c->green = pm_getint(ifp);
          c->blue = pm_getint(ifp);
        }
      }



    FILE* ofp;
    int fn_size = strlen(argv[1]);
    char* c =  malloc(fn_size * sizeof(char));
    strcpy(c, argv[1]);
    c[fn_size - 2] = 'g';
    ofp = fopen(c, "w");

    if(pgmraw)
      fprintf(ofp, "P5\n");
    else
      fprintf(ofp, "P2\n");

    fprintf(ofp, "%d %d \n", cols, rows);
    fprintf(ofp, "%d\n",maxval);


    for(i=0; i < rows; i++)
      for(j=0; j < cols ; j++) {
        color* c = &(colormap[i * cols + j]);
#ifdef BRIGHTER
        unsigned char initial_value = (c->red + c->green + c->blue) / 3;
        unsigned char value = initial_value + 50;
        if (initial_value > value) {
          value = 255;
        }
#else
        unsigned char initial_value = (c->red + c->green + c->blue) / 3;
        unsigned char value = initial_value - 50;
        if (initial_value < value) {
          value = 0;
        }
#endif
        if(pgmraw) {
          fprintf(ofp, "%c", value);
        }
        else {
          fprintf(ofp, "%d ", value);
        }
      }

    fclose(ofp);

    /* Closing */
    fclose(ifp);
    return 0;
}
