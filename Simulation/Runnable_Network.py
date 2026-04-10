import socket
from Consts import Consts
import os


class Network:
    def __init__(self):
        self.net = None

    def connect(self):
        IP = Consts.IP
        PORT = Consts.PORT
        if self.net is None:
            print("Connecting...")
            print(IP)
            print(PORT)
            self.net = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.net.bind((IP, PORT))
            print("Connection done")
    
    def killConnection(self):
        self.net.close()
        self.net = None

    def getPacket(self) -> str:
        if self.net is not None:
            return str(self.net.recvfrom(1024))
        return ""
    
if __name__ == "__main__":
    net = Network()
    net.connect()
    try:
        os.remove("./Log/log.txt")
    except:
        pass 
    while net.net is not None:
        packet = net.getPacket()
        packet = packet.split("'")
        packet = packet[1]
        if packet != "":
            log = open("./Log/log.txt","a")
            log.write(packet + "\n")
            log.close()