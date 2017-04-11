import sys
import readline
import re
import user as us
import pika
import time
import processuserinput as pu
import argparse


connection = None
channel = None
myUser = None

command = [
    "call <usernameToCall>",
    "say <msg>",
    "display",
    "disconnect",
    "endCall",
    "connect <x,y>",
    "move <x,y>",
    "sleep",
    "exit"
]

if (sys.version_info > (3, 0)):
    def raw_input(param):
        return input(param)

def parse():
    parser = argparse.ArgumentParser()
    parser.add_argument("name", type=str, help="user name")
    parser.add_argument("pos", nargs=2, type=int, help="user position")
    parser.add_argument("--sleepTime", type=int, help="sleep time between commands",
                        default=0.1)
    parser.add_argument("--host", type=str, help="host to connect to",
                        default="localhost")
    args = parser.parse_args()
    return args

def Usage():
    print("Usage:")
    for el in command:
        print("  " + el)

def main(name, pos, sleepTime, host):
    connection = pika.BlockingConnection(pika.ConnectionParameters(
               host))
    channel = connection.channel()
    myUser = us.User(name, pos, channel)
    myUser.Connect()
    if(myUser == None):
        do_exit();

    prefix = [el.split()[0] for el in command]

    cont = True
    while(cont):
        time.sleep(sleepTime)
        line = raw_input(name + "#: ")

        if line == 'exit':
            cont = False
            continue
        if line == 'sleep':
            time.sleep(sleepTime)
            continue
        line = line.strip()
        if(not len(line)):
            continue

        m = re.search(r"(\w+)\s*(.*)?", line)
        if(m.group(1) not in prefix):
            Usage()
            continue

        if(not pu.processUserInput(myUser, m)):
            Usage()

def do_exit():
    exit();

if __name__ == "__main__":
    args = parse()
    main(**vars(args))
