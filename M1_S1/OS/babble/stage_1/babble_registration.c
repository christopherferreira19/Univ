#include <stdio.h>
#include <strings.h>
#include <pthread.h>

#include "babble_registration.h"

client_data_t *registration_table[MAX_CLIENT];
int nb_registered_clients;

pthread_rwlock_t lock = PTHREAD_RWLOCK_INITIALIZER;

void registration_init(void)
{
    nb_registered_clients=0;

    bzero(registration_table, MAX_CLIENT * sizeof(client_data_t*));
}

client_data_t* registration_lookup_p(unsigned long key)
{
    int i=0;
    
    for(i=0; i< nb_registered_clients; i++){
        if(registration_table[i]->key == key){
            return registration_table[i];
        }
    }


    return NULL;
}

client_data_t* registration_lookup(unsigned long key)
{
		pthread_rwlock_rdlock(&lock);
		client_data_t* res = registration_lookup_p(key);
 		pthread_rwlock_unlock(&lock);
 		return res;
}

int registration_insert(client_data_t* cl)
{    
		pthread_rwlock_wrlock(&lock);
    if(nb_registered_clients == MAX_CLIENT){
     		pthread_rwlock_unlock(&lock);
        return -1;
    }
    
    /* lookup to find if key already exists*/
    client_data_t* lp= registration_lookup_p(cl->key);
    if(lp != NULL){
        fprintf(stderr, "Error -- id % ld already in use\n", cl->key);
     		pthread_rwlock_unlock(&lock);
        return -1;
    }

    /* insert cl */
    registration_table[nb_registered_clients]=cl;
    nb_registered_clients++;

 		pthread_rwlock_unlock(&lock);
    return 0;
}


client_data_t* registration_remove(unsigned long key)
{
		pthread_rwlock_wrlock(&lock);
		int i=0;
    
    for(i=0; i<nb_registered_clients; i++){
        if(registration_table[i]->key == key){
            break;
        }
    }

    if(i == nb_registered_clients){
        fprintf(stderr, "Error -- no client found\n");
    		pthread_rwlock_unlock(&lock);
        return NULL;
    }
    
    
    client_data_t* cl= registration_table[i];

    nb_registered_clients--;
    registration_table[i] = registration_table[nb_registered_clients];

 		pthread_rwlock_unlock(&lock);
 		return cl;
}
