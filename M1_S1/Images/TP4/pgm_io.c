#include <string.h>
#include "pgm_io.h"

pgm_t* pgm_create_empty(int cols, int rows, int maxval) {
    pgm_t* img = malloc(sizeof(pgm_t));
    img->cols = cols;
    img->rows = rows;
    img->maxval = maxval;
    img->graymap = malloc(img->cols * img->rows * sizeof(gray));

    return img;
}

pgm_t* pgm_read(char* filename) {
    FILE* ifp;
    pgm_t* img = malloc(sizeof(pgm_t));
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
    if(ich2 != '2' && ich2 != '5')
      pm_erreur(" wrong file type ");
    else
      if(ich2 == '2')
        pixel_format = ALPHA;
      else
        pixel_format = BINARY;

    /* Reading image dimensions */
    img->cols = pm_getint( ifp );
    img->rows = pm_getint( ifp );
    img->maxval = pm_getint( ifp );

    /* Memory allocation  */
    img->graymap = (gray*) malloc(img->cols * img->rows * sizeof(gray));

    /* Reading */
    for(i=0; i < img->rows; i++)
      for (j=0; j < img->cols ; j++)
        if(pixel_format == BINARY)
          img->graymap[i * img->cols + j] = pm_getrawbyte(ifp) ;
        else
          img->graymap[i * img->cols + j] = pm_getint(ifp);

    fclose(ifp);

    return img;
}

pgm_t* pgm_copy(pgm_t* src) {
    pgm_t* cpy = pgm_create_empty(src->cols, src->rows, src->maxval);
    memcpy(cpy->graymap, src->graymap, src->cols * src->rows * sizeof(gray));
    return cpy;
}

void pgm_write(pgm_t* img, pixel_format_t pixel_format) {
    pgm_write_file(img, pixel_format, stdout);
}

void pgm_write_filename(pgm_t* img, pixel_format_t pixel_format, char* filename, char* suffix) {
    int base_len = strlen(filename);
    int suff_len = strlen(suffix);
    char* outname = malloc(base_len + suff_len + 2);
    char* dot = strrchr(filename, '.');
    *dot = '\0';
    sprintf(outname, "%s_%s.pgm", filename, suffix);
    *dot = '.';

    FILE* out = fopen(outname, "w");
    pgm_write_file(img, pixel_format, out);
    fclose(out);
    free(outname);
}

void pgm_write_file(pgm_t* img, pixel_format_t pixel_format, FILE* file) {
    if (pixel_format == ALPHA) fprintf(file, "P2\n");
    else       fprintf(file, "P5\n");

    fprintf(file, "%d %d \n", img->cols, img->rows);
    fprintf(file, "%d\n", img->maxval);

    if (pixel_format == ALPHA)
        for (int i=0; i < img->rows; i++)
            for (int j=0; j < img->cols ; j++)
                fprintf(file, "%d ", img->graymap[i * img->cols + j]);
    else
        for (int i=0; i < img->rows; i++)
            for (int j=0; j < img->cols ; j++)
                fprintf(file, "%c",  img->graymap[i * img->cols + j]);
}

void pgm_free(pgm_t* img) {
	free(img->graymap);
	free(img);
}