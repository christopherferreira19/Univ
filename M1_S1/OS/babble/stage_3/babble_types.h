#ifndef __BABBLE_TYPES_H__
#define __BABBLE_TYPES_H__

#include <stdint.h>
#include <time.h>
#include <pthread.h>

#include "babble_config.h"
#include "babble_publication_set.h"

typedef enum{
    LOGIN =0,
    PUBLISH,
    FOLLOW,
    TIMELINE,
    FOLLOW_COUNT,
    RDV,
    UNREGISTER
} command_id;


typedef struct answer{
    char msg[BABBLE_BUFFER_SIZE];
    struct answer *next;
} answer_t;


typedef struct answer_set{
    answer_t *aset;
    int size;    /* -2 means no msg to send
                    -1 means single msg to send imediately
                    >=0 means msgs to send (with send of the number of
                    messages first) */
} answer_set_t;

typedef struct command{
    command_id cid;
    uint64_t order;
    int sock;    /* only needed by the LOGIN command, other commands
                  * will use the key */
    unsigned long key;
    char msg[BABBLE_SIZE];
    answer_set_t answer; /* once the cmd has been processed, answer
                           * to client is stored there */
    int answer_exp;   /* answer sent only if set */
} command_t;


typedef struct client_data{
    unsigned long key;     /* hash of the name */
    char client_name[BABBLE_ID_SIZE];    /* name as provided by the
                                          * client */
    int sock;              /* socket to communicate with this client
                            * */

    uint64_t commands_received;
    uint64_t commands_finished;
    pthread_mutex_t commands_finished_mutex;
    pthread_cond_t commands_finished_cond;
    
    publication_set_t *pub_set; /* set of messages published by the
                                * client */
    pthread_rwlock_t pub_lock;

    
    struct client_data *followed[MAX_CLIENT];  /* key of the followed
                                                * clients */
    pthread_rwlock_t followed_lock;

    int nb_followed;
    uint64_t last_timeline;   /* stored to display only *new* messages
                               * */
    int nb_follower;

} client_data_t;


#endif
