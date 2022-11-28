// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the packet to be sent.
public class Packet {
    // Network Layer
    IPv4Address sourceAddress;
    IPv4Address destinationAddress;

    // Application Layer
    String applicationData;

    public Packet(IPv4Address sourceAddress, IPv4Address destinationAddress, String applicationData) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.applicationData = applicationData;
    }

    public IPv4Address getDestinationAddress() {
        return destinationAddress;
    }

    public IPv4Address getSourceAddress() {
        return sourceAddress;
    }

    public String getApplicationData() {
        return applicationData;
    }
}
