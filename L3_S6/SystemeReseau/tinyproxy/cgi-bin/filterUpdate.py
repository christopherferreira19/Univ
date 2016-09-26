#!/usr/bin/env python
import os, cgi, sys, cgitb

cwd = os.path.dirname(sys.argv[0])
os.chdir(os.path.abspath(cwd))
filterFile = '../config/watch/filterConfig.txt'
filterFileTEMP = '../config/filterConfigTEMP.txt'

# Create instance of FieldStorage
form = cgi.FieldStorage()

# Get data from fields
if form.getvalue('filtrageArea'):
    data = form.getvalue('filtrageArea')
else:
    data = "No data"
dataFormat = data.split('\r\n')

# write it to a temporary file
targetTEMP = open(filterFileTEMP, "w");
for line in dataFormat:
    targetTEMP.write(line + "\n")
targetTEMP.close()

# Check Validity of format
import subprocess
p = subprocess.Popen("../config/valid_config_update", stdout=subprocess.PIPE, shell=True)
(output, err) = p.communicate()
p_status = p.wait()

# if valid format
if (p_status == 0):
    target = open(filterFile, "w");
    for line in dataFormat:
        target.write(line + "\n")
    target.close()

print("Content-type: text/html")
print("\r\n")
print("<HTML><HEAD><TITLE>Script Cgi</TITLE></HEAD>")
print("<BODY>")
print("<CENTER>")
print("<H1>Contenu des regles de filtrage</H1>")

print("""<textarea readonly cols="125" rows="40">""");
with open(filterFile) as fp:
    for line in fp:
        print (line[:-1])
print("</textarea>")
print("<br />")

# if invalid format
if (p_status != 0):
    print("<p>")
    print("Unable to update.")
    print("</p>")
    print("<p>")
    print("Invalid field : " + str(output)[2:-3] + "\n")
    print("<p>")

print("</CENTER>")
print("</BODY>")
print("</HTML>")