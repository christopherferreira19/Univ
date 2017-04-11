#include "syscall.h"

#define MAX_VALUES 30
#define PRODUCER_COUNT 4
#define CONSUMER_COUNT 4

void readValues(int* total_count, int values[]) {
    int count;
    GetInt(&count);
    if (count > MAX_VALUES) {
        PutString("Too many values in input file");
        Halt();
    }

    for (int i = 0; i < count; i++) {
        GetInt(&values[i * 2]);
        GetInt(&values[i * 2 + 1]);
    }

    *total_count = count * 2;
}

mutex_t mutex;
sem_t empty;
sem_t full;

#define BUF_SIZE 32
int buf[32];
int in = 0;
int out = 0;
int count = 0;

typedef struct {
    int index;
    const int* from;
    const int* to;
} ProducerData;

int producers[PRODUCER_COUNT];
ProducerData producersData[PRODUCER_COUNT];
int consumers[CONSUMER_COUNT];

void producer(void* arg) {
    const int* it = producersData[(int) arg].from;
    const int* to = producersData[(int) arg].to;
    int dummy = 0;

    while (it < to) {
        int waitFor = *(it++);
        for (int i = 0; i < waitFor; i++) {
            // Increment some dummy variable to make sure
            // this loop does not get optimized out
            dummy++;
        }
        int value = *(it++);
        SemaphoreWait(&full);
        LockAcquire(&mutex);

        buf[in] = value;
        in = (in + 1) % BUF_SIZE;
        count++;
        
        LockRelease(&mutex);
        SemaphoreSignal(&empty);
    }

    producersData[(int) arg].index = dummy;
}

void consumer(void* arg) {
    for (;;) {
        SemaphoreWait(&empty);
        LockAcquire(&mutex);

        // If the semaphore let the consumer get in
        // while the count is 0, this means the producers
        // are all done, thus we can just exit
        if (count == 0) {
            LockRelease(&mutex);
            break;
        }

        int value = buf[out];
        out = (out + 1) % BUF_SIZE;
        count--;

        PutInt(value);
        PutChar('\n');

        LockRelease(&mutex);
        SemaphoreSignal(&full);
    }
}

int main() {
    int total_producer_values_count;
    int producer_values[2 * MAX_VALUES];
    readValues(&total_producer_values_count, producer_values);

    LockInit(&mutex);
    SemaphoreInit(&empty, 0);
    SemaphoreInit(&full, BUF_SIZE);

    int producer_values_count = total_producer_values_count / PRODUCER_COUNT;
    for (int i; i < PRODUCER_COUNT; i++) {
        producersData[i].index = i + 1;
        producersData[i].from = producer_values + (i*producer_values_count);
        producersData[i].to = producer_values + ((i+1)*producer_values_count) - 1;
        producers[i] = UserThreadCreate(producer, (void*) i);
    }

    for (int i; i < CONSUMER_COUNT; i++) {
        consumers[i] = UserThreadCreate(consumer, (void*) (i+1));
    }

    for (int i; i < PRODUCER_COUNT; i++) {
        UserThreadJoin(producers[i]);
    }

    // Let all the consumer get a chance to exit
    for (int i; i < CONSUMER_COUNT; i++) {
        SemaphoreSignal(&empty);
    }
    for (int i; i < CONSUMER_COUNT; i++) {
        UserThreadJoin(consumers[i]);
    }

    LockDestroy(&mutex);
    SemaphoreDestroy(&empty);
    SemaphoreDestroy(&full);

    return 0;
}