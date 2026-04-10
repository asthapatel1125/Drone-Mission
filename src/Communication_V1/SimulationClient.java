package Communication_V1;
import java.io.*;
import java.net.*;

public class SimulationClient {
    public static void main(String[] args) {
        try {
            // Set up server socket to connect to the server on port 8080
            Socket socket = new Socket("localhost", 1010);

            // Set up input stream to receive messages from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up socket for sending to Python server
            DatagramSocket sendSocket = new DatagramSocket();

            while (!socket.isClosed()) {
                String receivedMessage = in.readLine();
                if (receivedMessage != null && !receivedMessage.trim().isEmpty()) {
                    System.out.println("Received by SimulationClient: " + receivedMessage);
                    forwardMessage(receivedMessage, sendSocket);
                }
            }

            // Close resources
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void forwardMessage(String message, DatagramSocket sendSocket) {
        try {
            InetAddress pythonServerAddress = InetAddress.getByName("localhost");
            int pythonServerPort = 1111;

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