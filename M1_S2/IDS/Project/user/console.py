import sys
import readline
import re
import user as us
import pika
import time

connection = pika.BlockingConnection(pika.ConnectionParameters(
               'localhost'))
channel = connection.channel()

myUser = {}

command = [
    "user <userName> create <pos>",
    "user <userName> call <usernameToCall>",
    "user <userName> send <msg>",
    "user <userName> display",
    "user <userName> disconnect",
    "userList",
]

if (sys.version_info > (3, 0)):
    def raw_input(param):
        return input(param)

def getUser(name):
    if not name in myUser:
        return None
    return myUser[name]

def newUser(name, pos):
    print("newUser", name, pos)
    if name in myUser:
        return (False, "user already exist")
    user = us.User(name, pos, channel)
    user.Connect()
    myUser[name] = user
    return (True, user)

def displayUser():
    print(myUser)

def Usage():
    print("Usage:")
    for el in command:
        print(el)

def main():
    cont = True
    while(cont):
        time.sleep(1)
        user = None
        line = raw_input("phone-game#: ")

        if line == 'exit':
            cont = False
            continue
        line = line.strip()
        m = re.search(r"(\w+)\s*(.*)?", line)

        if not m:
            Usage()
            continue

        if m.group(1) == "?":
            Usage()
            continue

        if m.group(1) == "userList":
            displayUser()
            continue

        if m.group(1) == "user":
            m = re.search(r'(\w+)\s+(.*)', m.group(2))
            if not m:
                Usage()
                continue
            
            userName = m.group(1)
            m = re.search(r'(\w+)(.*)', m.group(2))

            if m.group(1) == "create":
                m = re.search(r'\s*([0-9]+)\s*,\s*([0-9]+)\s*', m.group(2))
                if m:
                    userPos = [m.group(1), m.group(2)]
                    res = newUser(userName, userPos)
                    if not res[0]:
                        print(res[1])
                    continue
                    
            user = getUser(userName)
            if user == None:
                print("unknown user", userName)
                continue
                
            if m.group(1) == "send":
                m = re.search(r'\s*(\w+)\s*', m.group(2))
                if m:
                    user.Send(m.group(1))
                    continue
            if m.group(1) == "call":
                m = re.search(r'\s*(\w+)\s*', m.group(2))
                if m:
                    user.Call(m.group(1))
                    continue

            if m.group(1) == "display":
                    user.Display()
                    continue

            # if m.group(2) == "disconnect":
            #         user.Disconnect()
            #         continue
            # if m.group(2) == "move":
            #     m = re.search(r'([0-9])\s*,\s*([0-9])\s*', m.group(3))
            #     if m:
            #         newPos = [m.group(1), m.group(2)]
            #         user.Move(newPos)

        Usage()
        continue

if __name__ == "__main__":
    main()
