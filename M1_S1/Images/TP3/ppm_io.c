#include "ppm_io.h"

ppm_t* ppm_create_empty(int cols, int rows, int maxval) {
    ppm_t* ppm = malloc(sizeof(ppm_t));
    ppm->cols = cols;
    ppm->rows = rows;
    ppm->maxval = maxval;
    ppm->pixmap = malloc(ppm->cols * ppm->rows * sizeof(color));

    return ppm;
}

ppm_t* ppm_read(char* filename) {
    FILE* ifp;
    ppm_t* ppm = malloc(sizeof(ppm_t));
    int ich1, ich2;
    pixel_format_t pixel_format;
    int i, j;

    ifp = fopen(filename,"r");
    if (ifp == NULL) {
      printf("error in opening file %s\n", filename);
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
        pixel_format = ALPHA;
      else
        pixel_format = BINARY;

    /* Reading image dimensions */
    ppm->cols = pm_getint( ifp );
    ppm->rows = pm_getint( ifp );
    ppm->maxval = pm_getint( ifp );

    /* Memory allocation  */
    ppm->pixmap = (color*) malloc(ppm->cols * ppm->rows * sizeof(color));

    /* Reading */
    for(i=0; i < ppm->rows; i++) {
      for (j=0; j < ppm->cols ; j++) {
        if (pixel_format == BINARY) {
          PPM_AT(ppm, i, j).red = pm_getrawbyte(ifp) ;
          PPM_AT(ppm, i, j).green = pm_getrawbyte(ifp) ;
          PPM_AT(ppm, i, j).blue = pm_getrawbyte(ifp) ;
        }
        else {
          PPM_AT(ppm, i, j).red = pm_getint(ifp);
          PPM_AT(ppm, i, j).green = pm_getint(ifp);
          PPM_AT(ppm, i, j).blue = pm_getint(ifp);
        }
      }
    }

    fclose(ifp);

    return ppm;
}

ppm_t* ppm_copy(ppm_t* src) {
    ppm_t* cpy = ppm_create_empty(src->cols, src->rows, src->maxval);
    memcpy(cpy->pixmap, src->pixmap, src->cols * src->rows * sizeof(color));
    return cpy;
}

void ppm_write(ppm_t* ppm, pixel_format_t pixel_format) {
	if (pixel_format == ALPHA) printf("P3\n");
    else       printf("P6\n");

    printf("%d %d \n", ppm->cols, ppm->rows);
    printf("%d\n", ppm->maxval);

    if (pixel_format == ALPHA) {
        for (int i=0; i < ppm->rows; i++) {
            for (int j=0; j < ppm->cols ; j++) {
                printf("%d ", PPM_AT(ppm, i, j).red);
                printf("%d ", PPM_AT(ppm, i, j).green);
                printf("%d ", PPM_AT(ppm, i, j).blue);
            }
        }
    }
    else {
        for (int i=0; i < ppm->rows; i++) {
            for (int j=0; j < ppm->cols ; j++) {
                printf("%c",  PPM_AT(ppm, i, j).red);
                printf("%c",  PPM_AT(ppm, i, j).green);
                printf("%c",  PPM_AT(ppm, i, j).blue);
            }
        }
    }
}

void ppm_free(ppm_t* ppm) {
	free(ppm->pixmap);
	free(ppm);
}