#!/usr/bin/env python
import os
import sys

cwd = os.path.dirname(sys.argv[0])
os.chdir(os.path.abspath(cwd))

logFile = '../config/logs.txt'

host = os.environ['SERVER_PORT']

print("Content-type: text/html")
print("\r\n")
print("<HTML><HEAD><TITLE>Script Cgi</TITLE></HEAD>")
print("<BODY>")
print("<CENTER>")
print("<H1>Contenu du journal</H1>")

print("<p>")
print("<H2>Logs</H2>")

print("""<form action="http://localhost:""" + host + """/tinyproxy:manager/cgi-bin/logsRm.py" name="resetLogs">""")
print("""<textarea readonly cols="125" rows="40">""")
with open(logFile) as fp:
    for line in fp:
        print (line[:-1])
print("</textarea>")
print("</p>")
print("""<input type="submit" value="Clear"/>""")
print("""</form>""")

print("</BODY>")
print("</HTML>")