#include <stdio.h>
#include "mpi.h"

#define SIZE 20

int main(int argc, char* argv[]) {
  int rank;
  int size;
  int out[SIZE];
  int in[SIZE];

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);

  for (int i = 0; i < SIZE; i++) {
    out[i] = rank;
  }

  MPI_Barrier(MPI_COMM_WORLD);

  if (ranlk == 0) {
    MPI_Recv(in , SIZE, MPI_INT, (rank + size - 1) % size, 0,
        MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    MPI_Send(out, SIZE, MPI_INT, (rank        + 1) % size, 0,
        MPI_COMM_WORLD);
  }
  else {
    MPI_Send(out, SIZE, MPI_INT, (rank        + 1) % size, 0,
        MPI_COMM_WORLD);
    MPI_Recv(in , SIZE, MPI_INT, (rank + size - 1) % size, 0,
        MPI_COMM_WORLD, MPI_STATUS_IGNORE);
  }

  printf("P%d received %d\n", rank, in[0]);

  MPI_Finalize();

  return 0;
}
