import re
import user as us

def processUserInput(user, match):
    if user == None:
        print("unknown user")
        return False
                
    if match.group(1) == "call":
        m = re.search(r'\s*(\w+)\s*', match.group(2))
        if m:
            user.Call(m.group(1))
            return True

    if match.group(1) == "display":
        user.Display()
        return True

    if match.group(1) == "say":
        m = re.search(r'\s*(.*)\s*', match.group(2))
        if m:
            user.Say(m.group(1))
            return True

    if match.group(1) == "disconnect":
        user.Disconnect()
        return True

    if match.group(1) == "endCall":
        user.EndCall()
        return True

    if match.group(1) == "connect":
        m = re.search(r'([0-9]+)\s*,\s*([0-9]+)\s*', match.group(2))
        if m:
            pos = [m.group(1), m.group(2)]
            user.ConnectTo(pos)
        return True

    if match.group(1) == "move":
        m = re.search(r'([0-9]+)\s*,\s*([0-9]+)\s*', match.group(2))
        if m:
            newPos = [int(m.group(1)), int(m.group(2))]
            user.Move(newPos)
        return True
    return False
