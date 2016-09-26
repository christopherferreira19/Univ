#!/usr/bin/env python
import os
import sys
import subprocess

cwd = os.path.dirname(sys.argv[0])
os.chdir(os.path.abspath(cwd))

logFile = '../config/logs.txt'

print("Content-type: text/html")
print("\r\n")
print("<HTML><HEAD><TITLE>Script Cgi</TITLE></HEAD>")
print("<BODY>")
print("<CENTER>")
print("<H1>Contenu du journal</H1>")

print("<H2>Logs</H2>")

subprocess.call("./rm_logs.cgi")
print("<p>")
print("Logs as been deleted")
print("</p>")

print("</BODY>")
print("</HTML>")