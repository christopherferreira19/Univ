### TINYPROXY ###
#################

## Compilation ##
#################
# A la racine du projet

cmake .
make

##  Lancement  ##
#################

./tinyproxy <listen port> <configuration directory path> [server_host server_port]


# Pour le lancer en connection direct
./tinyproxy 2222 /home/b/blevind/system_reseau/tinyproxy

## Proxy to proxy
./tinyproxy 2222 /home/b/blevind/system_reseau/tinyproxy www-cache.ujf-grenoble.fr 3128

#Example
http://info.cern.ch/hypertext/WWW/TheProject.html
http://www.lagado.com/proxy-test

##  Accès à l'interface de gestion  ##
######################################

http://localhost:2222/tinyproxy:manager/cgi-bin/filter.py
http://localhost:2222/tinyproxy:manager/cgi-bin/filter.py