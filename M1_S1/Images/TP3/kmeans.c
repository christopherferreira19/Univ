#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include "ppm_io.h"

#define PIXEL_CLUSTER_AT(ary, i, j) ((ary)[(i) * (ary)->cols + (j)])

typedef struct {
    int count;
    int red_sum;
    int green_sum;
    int blue_sum;
    color center;
} cluster_data;

int main(int argc, char* argv[]) {
    /* Arguments */
    if (argc != 3 ) {
        printf("\nUsage: %s <file> <clusters count> \n\n", argv[0]);
        return 0;
    }

    ppm_t* src = ppm_read(argv[1]);
    int clusters_count = atoi(argv[2]);
    if (clusters_count < 2) {
        printf("\nParameter <clusters count> must be greater than 1 (got %s)\n", argv[2]);
        return 0;   
    }

    cluster_data* clusters = malloc(clusters_count * sizeof(cluster_data));
    for (int i = 0; i < clusters_count; i++) {
        clusters[i].count = 0;
        clusters[i].red_sum = 0;
        clusters[i].green_sum = 0;
        clusters[i].blue_sum = 0;
        // Choose initial cluster centers randomly
        clusters[i].center = (color) {
            rand() % src->maxval,
            rand() % src->maxval,
            rand() % src->maxval
        };
    }
    /*
    // Forced initial three
    clusters[0].center = (color) { 255, 83, 201 };
    clusters[1].center = (color) { 222, 219, 64 };
    clusters[2].center = (color) { 0, 0, 0 };
    */

    int* pixel_cluster = malloc(src->cols * src->rows * sizeof(int));
    for (;;) {
        bool modified = false;

        for (int i = 0; i < src->rows; i++) {
            for (int j = 0; j < src->cols; j++) {
                color c = PPM_AT(src, i, j);
                int min_distance_sq = INT_MAX;
                int min_cluster = -1;
                for (int cluster = 0; cluster < clusters_count; cluster++) {
                    color center = clusters[cluster].center;
                    int dred = center.red - c.red;
                    int dgreen = center.green - c.green;
                    int dblue = center.blue - c.blue;
                    int distance_sq = dred * dred + dgreen * dgreen + dblue * dblue;
                    if (distance_sq < min_distance_sq) {
                        min_cluster = cluster;
                        min_distance_sq = distance_sq;
                    }
                }

                if (pixel_cluster[i * src->cols + j] != min_cluster) {
                    pixel_cluster[i * src->cols + j] = min_cluster;
                    modified = true;
                }

                clusters[min_cluster].count++;
                clusters[min_cluster].red_sum += c.red;
                clusters[min_cluster].green_sum += c.green;
                clusters[min_cluster].blue_sum += c.blue;
            }
        }

        if (!modified) {
            break;
        }

        for (int i = 0; i < clusters_count; i++) {
            if (clusters[i].count > 0)  {
                clusters[i].center.red = clusters[i].red_sum;
                clusters[i].center.red /= clusters[i].count;
                clusters[i].center.green = clusters[i].green_sum / clusters[i].count;
                clusters[i].center.blue = clusters[i].blue_sum / clusters[i].count;
            }
            clusters[i].count = 0;
            clusters[i].red_sum = 0;
            clusters[i].green_sum = 0;
            clusters[i].blue_sum = 0;
        }
    }

    ppm_t* dst = ppm_create_empty(src->cols, src->rows, src->maxval);
    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            int cluster = pixel_cluster[i * src->cols + j];
            PPM_AT(dst, i, j) = clusters[cluster].center;
        }
    }

    ppm_write(dst, true);

    return 0;
}
