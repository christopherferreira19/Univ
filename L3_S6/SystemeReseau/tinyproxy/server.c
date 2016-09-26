#include "server.h"
#include "utils/csapp.h"
#include "utils/strutils.h"

/*
 * clienterror - returns an error message to the client
 */
void clienterror(int fd, char *cause, char *errnum,
                 char *shortmsg, char *longmsg) {
    char buf1[MAXLINE];
    char body1[MAXBUF], body2[MAXBUF];

    /* Build the HTTP response body */
    snprintf(body1, sizeof(body1), "<html><title>Tiny Error</title>");
    snprintf(body2, sizeof(body2), "%s<body bgcolor=""ffffff"">\r\n", body1);
    snprintf(body1, sizeof(body1), "%s%s: %s\r\n", body2, errnum, shortmsg);
    snprintf(body2, sizeof(body2), "%s<p>%s: %s\r\n", body1, longmsg, cause);
    snprintf(body1, sizeof(body1), "%s<hr><em>The Tiny Web server</em>\r\n", body2);

    /* Build and send the HTTP response header */
    snprintf(buf1, sizeof(buf1), "HTTP/1.0 %s %s\r\n", errnum, shortmsg);
    Rio_writen(fd, buf1, strlen(buf1));
    snprintf(buf1, sizeof(buf1), "Content-type: text/html\r\n");
    Rio_writen(fd, buf1, strlen(buf1));
    snprintf(buf1, sizeof(buf1), "Content-length: %d\r\n\r\n", (int)strlen(body1));
    Rio_writen(fd, buf1, strlen(buf1));
    /* Send the HTTP response body */
    Rio_writen(fd, body1, strlen(body1));
}

bool parse_manager_args(char *uri, char *filename, char *cgiargs) {
    char *ptr;
    char *cc;

    if (!strstr(uri, "cgi-bin")) {  /* Static content */
        strcpy(cgiargs, "");
        cc = concat(localDirPath, "/config/config.html");
        strcpy(filename, cc);
        return true;
    }
    else {  /* Dynamic content */
        ptr = index(uri, '?');
        if (ptr) {
            strcpy(cgiargs, ptr+1);
            *ptr = '\0';
        } else {
            strcpy(cgiargs, "");
        }

        if ((ptr = strstr(uri, "cgi-bin/")) != NULL) {
            ptr = index(ptr, '/');
            cc = concat(localDirPath, "/cgi-bin/", ptr + 1);
            strcpy(filename, cc);
        } else {
            cc = concat(localDirPath, "/cgi-bin/default.cgi");
            strcpy(filename, cc);
        }
        return false;
    }
}

/*
 * get_filetype - derive file type from file name
 */
void get_filetype(char *filename, size_t bufsize, char *filetype)
{
    if (strstr(filename, ".html"))
        snprintf(filetype, bufsize, "text/html");
    else if (strstr(filename, ".gif"))
        snprintf(filetype, bufsize, "image/gif");
    else if (strstr(filename, ".jpg"))
        snprintf(filetype, bufsize, "image/jpeg");
    else
        snprintf(filetype, bufsize, "text/plain");
}

void serve_static(int fd, char *filename, int filesize)
{
    int srcfd;
    char *srcp, filetype[MAXLINE], buf1[MAXBUF], buf2[MAXBUF];

    /* Send response headers to client */
    get_filetype(filename, sizeof(filetype), filetype);
    snprintf(buf1, sizeof(buf1), "HTTP/1.0 200 OK\r\n");
    snprintf(buf2, sizeof(buf2), "%sServer: Tiny Web Server\r\n", buf1);
    snprintf(buf1, sizeof(buf1), "%sContent-length: %d\r\n", buf2, filesize);
    snprintf(buf2, sizeof(buf2), "%sContent-type: %s\r\n\r\n", buf1, filetype);
    Rio_writen(fd, buf2, strlen(buf2));

    /* Send response body to client */
    srcfd = Open(filename, O_RDONLY, 0);
    srcp = Mmap(0, (size_t) filesize, PROT_READ, MAP_PRIVATE, srcfd, 0);
    Close(srcfd);
    Rio_writen(fd, srcp, (size_t) filesize);
    Munmap(srcp, (size_t) filesize);
}

void serve_dynamic(int fd, char *filename, char *cgiargs)
{
    char buf[MAXLINE], *emptylist[] = { NULL };

    /*
     * Return first part of HTTP response.
     * We assume that:
     * - Fork will not fail
     * - Execve will not fail (the existence and the permissions of the file
     *    have already been checked)
     */
    snprintf(buf, sizeof(buf), "HTTP/1.0 200 OK\r\n");
    Rio_writen(fd, buf, strlen(buf));
    snprintf(buf, sizeof(buf), "Server: Tinyproxy Web Server\r\n");
    Rio_writen(fd, buf, strlen(buf));

    if (Fork() == 0) { /* child */
        /* Real server would set all CGI vars here */
        setenv("QUERY_STRING", cgiargs, 1);
        Dup2(fd, STDOUT_FILENO);         /* Redirect stdout to client */
        Execve(filename, emptylist, environ); /* Run CGI program */
    }
    Wait(NULL); /* Parent waits for and reaps child */
}