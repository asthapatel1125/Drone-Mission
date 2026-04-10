package Communication_V1;

import java.net.*;
import java.io.*;


public class SimulationClientCall {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(1111); // Listening on port 9999

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Receive the message

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from SimulationClient: " + receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
