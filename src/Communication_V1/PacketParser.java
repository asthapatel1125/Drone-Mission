package Communication_V1;

public class PacketParser {

    // Method to parse a message and return an array of data without the server identifier
    public static String[] parseMessage(String message) {
        if (message != null) {
            String[] parts = message.split("_");
            String[] parsedData = new String[parts.length - 1];

            // Manually copy elements excluding the server identifier
            for (int i = 1; i < parts.length; i++) {
                parsedData[i - 1] = parts[i];
            }

            return parsedData;
        }
        return null; // Handle case when server identifier is not present
    }

    // Method to create a packet from an array of data with the server identifier
    public static String createPacket(String serverIdentifier, String[] data) {
        StringBuilder packet = new StringBuilder(serverIdentifier);

        for (String packetData : data) {
            packet.append("_").append(packetData);
        }

        return packet.toString();
    }

    // Example usage

}