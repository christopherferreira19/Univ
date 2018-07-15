#include <stdio.h>
#include "mpi.h"

int main(int argc, char* argv[]) {
  int rank;
  int size;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);

  for (int i = 0; i < size; i++) {
    if (i == rank) {
      printf("I am %d of %d\n", rank, size);
    }

    MPI_Barrier(MPI_COMM_WORLD);
  }

  MPI_Finalize();

  return 0;
}
