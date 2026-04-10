package Communication_V1;

import java.io.*;
import java.net.*;

public class FrontendClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1010); // Connect to the server

            // Set up input stream to receive messages from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Set up output stream to send a message to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Loop to continuously receive messages until the server socket is closed
            while (!socket.isClosed()) {
                String receivedMessage = in.readLine();
                if (receivedMessage != null && !receivedMessage.trim().isEmpty()) {
                    System.out.println("Received by FrontendClient: " + receivedMessage);
                    
                 // Sending a response back to the server
                    String responseMessage = "backend_packet_sent_from_frontend\n";
                    out.println(responseMessage);
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
}