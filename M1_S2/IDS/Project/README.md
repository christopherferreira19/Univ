Description
===========

This project is split in two part :

## Java

The first part, inside the java folder is the java code providing the "server" part of the project.
Maven can be used to build it. For convenience, the following command can be used to execute the server (from inside the /java folder and with maven installed):

```
mvn exec:java -Dexec.mainClass=ids.Main -Dexec.arguments=<size>
```
where `<size>` is an argument defining the width and height of the square grid (a power of two).

## Python

The second part, is the client part written in python (it should be compatible with both python2 and python3).
In order to run the python source code part, the libraries `pika` and `msgpack-python` should be installed on the system. This can be achieved with the following if pip is installed :
```
pip install pika msgpack-python
```

Two main interface are provided to interact with the server as a user. The most convenient is userconsole.py which provides a console with some simple commands.

It can be run with the following command (from the root of the project) :
```
python user/userconsole.py <user> <x> <y>
```
where `<user>` (name of the user), `<x>` and `<y>` (x and y coordinates on the 2D grid) should be replaced with relevant values.

Or it is possible to run a predefined scenario with (Note that the grid has to be >= 8x8):
```
python user/userconsole.py user1 1 2 < scenario/test_u1 &  python user/userconsole.py user2 3 4  < scenario/test_u2
```
