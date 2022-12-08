// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the packet to be sent.
import java.util.List;
import java.util.ArrayList;

public class Packet {
    char routeMethod; // D = Dijkstras

    // Link Layer
    int sourceMAC; // Gives ID of router in graph/list
    int destinationMAC;
    List<Integer> pathSoFar = new ArrayList<Integer>(); // Would actually be IPs in network layer

    // Network Layer
    IPv4Address sourceAddress;
    IPv4Address destinationAddress;

    // Application Layer
    String applicationData;

    public Packet(int sourceMAC, int destinationMAC, IPv4Address sourceAddress, IPv4Address destinationAddress, String applicationData) {
        this.sourceMAC = sourceMAC;
        this.destinationMAC = destinationMAC;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.applicationData = applicationData;
    }

    public void setDijkstras() {
        this.routeMethod = 'D';
    }

    public void setNaiive() {
        this.routeMethod = 'N';
    }

    public int getSourceMAC() {
        return sourceMAC;
    }

    public int getDestinationMAC() {
        return destinationMAC;
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

    public char getRouteMethod () {
        return routeMethod;
    }

    public void addAddressToPath (int MACAddress) {
        pathSoFar.add(MACAddress);
    }

    public List<Integer> getPathSoFar () {
        return pathSoFar;
    }

}
