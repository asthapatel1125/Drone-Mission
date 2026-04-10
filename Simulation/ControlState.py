from enum import Enum

class State(Enum):
    KEYBOARD = 0
    CONTROLLER = 1
    PACKET = 2

class StateColor:
    C1 = '\033[92m'
    C2 = '\033[0m'
