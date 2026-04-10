package Communication_V1;

import java.io.*;
import java.net.*;

public class CommunicationHandler {
    private ServerSocket serverSocket;
    private Socket backendSocket;
    private Socket frontendSocket;
    private Socket firmwareSocket;
    private Socket simulationSocket;

    public CommunicationHandler() {
        try {
            serverSocket = new ServerSocket(1010); // Backend server
            System.out.println("Server started. Waiting for clients to connect...");
            
            // Establish connections with clients
            backendSocket = serverSocket.accept(); // Backend client
            	System.out.println("Backend client connected");
            frontendSocket = serverSocket.accept(); // Frontend client
            	System.out.println("Frontend client connected");
            firmwareSocket = serverSocket.accept(); // Firmware client
            	System.out.println("Firmware client connected");
            simulationSocket = serverSocket.accept(); // Simulation client
            	System.out.println("Simulation client connected");
            	 new Thread(this::handleBackendResponse).start();
                 //new Thread(this::handleFrontendResponse).start();
            
            System.out.println("All clients connected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClients(String packet) {
        try {
        	//parse the message to find the client identifier: frontend, firmware, or simulation
        	//send these messages to the appropriate clients
        	//if they send a message back, send them to other clients/backend server
        	String[] parts = packet.split("_");
    		
			if (parts.length >= 2) //part1: server identifier, part 2: data
			{
	            // The first part (index 0) is the server identifier
	            String serverIdentifier = parts[0];
	            //convert the message to lowercase
	            
	            
	            switch(serverIdentifier)
	            {
	            	case "backend":
	            		// Sending message to backend
	                    PrintWriter backendOut = new PrintWriter(backendSocket.getOutputStream(), true);
	                    backendOut.println(packet); //send message to the client
	                    
	                    //read response back from the backendClient
	                    handleBackendResponse();     
	            		break;
	            	case "frontend":
	            		// Sending message to frontend
	                    PrintWriter frontendOut = new PrintWriter(frontendSocket.getOutputStream(),true);
	                    frontendOut.println(packet); //send message to the client
	                    
	                    //reponse from the frontend Client
	                    handleFrontendResponse();
	                    break;
	            	case "firmware":
	            		// Sending message to firmware
	                    PrintWriter firmwareOut = new PrintWriter(firmwareSocket.getOutputStream(), true);
	                    firmwareOut.println(packet); //send message to the client
	                    break;
	            	case "simulation":
	            		// Sending message to simulation
	                    PrintWriter simulationOut = new PrintWriter(simulationSocket.getOutputStream(), true);
	                    simulationOut.println(packet); //send message to the client
	                    break;
	            	default:
	            		System.out.println("invalid packet");
	            		break;
	            }}
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    private void handleBackendResponse() {
        try {
            BufferedReader backendIn = new BufferedReader(new InputStreamReader(backendSocket.getInputStream()));
           
            if (backendIn.ready()) {
                String backendResponse = backendIn.readLine();
                if (backendResponse != null) {
                    //System.out.println("Received from backend on the server side: " + backendResponse);
                    // Process backend response if needed
                    // Forward the response to other clients or handle as required
                    sendToClients(backendResponse);
                }}
            } catch (IOException e) {
            	e.printStackTrace();
            }
    }
    
    private void handleFrontendResponse() {
        try {
            BufferedReader frontendIn = new BufferedReader(new InputStreamReader(frontendSocket.getInputStream()));
           
            String frontendResponse;
            while ((frontendResponse = frontendIn.readLine()) != null) {
                if (!frontendResponse.trim().isEmpty()) {
                    //System.out.println("Received from backend on the server side: " + frontendResponse);
                    // Process frontend response if needed
                    // Forward the response to other clients or handle as required
                    sendToClients(frontendResponse);
                    
                }
                break;}
            } catch (IOException e) {
            	e.printStackTrace();
            }
    }


    public static void main(String[] args) {
        CommunicationHandler communicationHandler = new CommunicationHandler();

        // Example of taking user inputs and sending them to respective clients
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) 
        {
            try {
                System.out.print("Enter a packet to send: ");
                String userInput = userInputReader.readLine();
                communicationHandler.sendToClients(userInput);
            } catch (IOException e) {
                e.printStackTrace();
            }}}
}