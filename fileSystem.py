# ALL FUNCTIONS WILL TAKE commands AS FIRST PARAMETER
import os

def main():
    print 'Welcome to the IT308 filesystem'
    query = raw_input("$ ")
    commands = query.split()
    while(commands[0] != 'quit'):
        #switch to different inputs
        switcher = {
            "help": help
        }
        func = switcher.get(commands[0], invalid)
        func(commands)
        query = raw_input("$ ")
        commands = query.split()

def invalid(commands):
    print "Invalid command: "+commands[0]

def help(commands):
    print """Welcome to the IT308 filesystem. Some of the useful commands are as follows:\n
    1. help\n
    """

if __name__ == '__main__':
    main();
