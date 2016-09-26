#!/usr/bin/env python
import os
import sys

cwd = os.path.dirname(sys.argv[0])
os.chdir(os.path.abspath(cwd))
filterFile = '../config/watch/filterConfig.txt'

host = os.environ['SERVER_PORT']

print("Content-type: text/html")
print("\r\n")
print("<HTML><HEAD><TITLE>Script Cgi</TITLE></HEAD>")
print("<BODY>")
print("<CENTER>")
print("<H1>Contenu des regles de filtrage</H1>")

print("""<form action="http://localhost:""" + host + """/tinyproxy:manager/cgi-bin/filterUpdate.py" name="updateFiltrage">""")
print("""<textarea name="filtrageArea" cols="50" rows="25">""")
with open(filterFile) as fp:
    for line in fp:
        print (line[:-1])
print("</textarea>")
print("<br />")
print("""<input type="submit" value="Modifier"/>""")
print("""</form>""")

print("</CENTER>")

print("</BODY>")
print("</HTML>")