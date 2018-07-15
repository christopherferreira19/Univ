#include <stdio.h>
#include "mpi.h"

#define SIZE 10000

int main(int argc, char* argv[]) {
  int rank;
  int size;
  int out[SIZE];
  int in[SIZE];
  MPI_Request delivery;
  MPI_Request reception;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);

  for (int i = 0; i < SIZE; i++) {
    out[i] = rank;
  }

  MPI_Barrier(MPI_COMM_WORLD);

  MPI_Irecv( in , SIZE, MPI_INT, (rank + size - 1) % size, 0,
      MPI_COMM_WORLD, &reception);
  MPI_Isend(out, SIZE, MPI_INT, (rank        + 1) % size, 0,
      MPI_COMM_WORLD, &delivery);
  MPI_Wait(&reception, MPI_STATUS_IGNORE);

  printf("P%d received %d\n", rank, in[0]);

  MPI_Finalize();

  return 0;
}
