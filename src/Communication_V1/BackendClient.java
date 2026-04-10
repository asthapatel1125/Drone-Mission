package Communication_V1;

import java.io.*;
import java.net.*;

public class BackendClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1010); // Connect to the backend server

            // Set up input stream to receive messages from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up output stream to send a message to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // Loop to continuously receive messages until the server socket is closed
            while (!socket.isClosed()) {
                String receivedMessage = in.readLine();
                if (receivedMessage != null && !receivedMessage.trim().isEmpty()) {
                    System.out.println("Received from client's side:" + receivedMessage);
                    
                    //parse the packet to send data to the appropriate methods/classes
                    /*if any message is received back from these methods/classes then send the data back
                      to the server to handle the given data
                      NOTE: convert to packet format before sending the packet back to the server */
                    String[] packetData = PacketParser.parseMessage(receivedMessage);
                    if(packetData != null)
                    {
                    switch(packetData[0])
                    {
                    	case "Drone":
                    		System.out.println("packet sent to Drone class");
                    		break;
                    	default:
                    		System.out.println("packet not recognized");
                    	
                    }}
                    else 
                    	System.out.println("packet array is null");
                    //Sending a response back to the server
                   //String responseMessage = "frontend_received_packet!\n";
                   //out.println(responseMessage);
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