#include "filterWatch.h"
#include "utils/csapp.h"
#include <sys/inotify.h>

#define MAX_EVENTS 1024 /*Max. number of events to process at one go*/
#define LEN_NAME 512
#define EVENT_SIZE  ( sizeof (struct inotify_event) ) /*size of one event*/
#define BUF_LEN     ( MAX_EVENTS * ( EVENT_SIZE + LEN_NAME )) /*buffer to store the data of events*/

int watch(char* fileToWatch) {
    int length, i = 0, wd;
    int fd;
    char buffer[BUF_LEN];

    /* Initialize Inotify*/
    fd = inotify_init();
    if ( fd < 0 ) {
        unix_error( "Couldn't initialize inotify");
    }

    /* add watch to starting directory */
    wd = inotify_add_watch(fd, fileToWatch, IN_CLOSE_WRITE);

    if (wd == -1)
    {
        printf("Couldn't add watch to %s\n",fileToWatch);
        unix_error("Couldn't add watch");
    }
    else
    {
        printf("Watching %s\n",fileToWatch);
    }

    while(1) {
        i = 0;
        length = read( fd, buffer, BUF_LEN );

        if ( length < 0 ) {
            perror( "read" );
        }

        while ( i < length ) {
            struct inotify_event *event = ( struct inotify_event * ) &buffer[ i ];
            if ( event->len ) {

                if ( event->mask & IN_CLOSE_WRITE) {
                    printf( "The file %s was writen with WD %d\n", event->name, event->wd );
                    kill(getpgid(getpid()), SIGUSR1);
                }

                i += EVENT_SIZE + event->len;
            }
        }
    }

    /* Clean up*/
    inotify_rm_watch(fd, wd);
    close( fd );

    return 0;
}