package Communication_V1;

import java.io.*;
import java.net.*;


public class FirmwareClient {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 1010); // Connect to the server

            // Set up input stream to receive messages from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up socket for sending to Python server
            DatagramSocket sendSocket = new DatagramSocket();

            // Loop to continuously receive messages until the server socket is closed
            while (!socket.isClosed()) {
                String receivedMessage = in.readLine();
                if (receivedMessage != null && !receivedMessage.trim().isEmpty()) {
                    System.out.println("Received by FirmwareClient: " + receivedMessage);
                    forwardMessage(receivedMessage, sendSocket);
                }
                // Add an exit condition if needed
            }

            // Close resources if necessary
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void forwardMessage(String message, DatagramSocket sendSocket) {
        try {
            InetAddress pythonServerAddress = InetAddress.getByName("localhost");
            int pythonServerPort = 2222;

            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                pythonServerAddress,
                pythonServerPort
            );

            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}