// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the router that a packet is sent through.
import java.util.List;

public class Router {
    List<Router> knownRouters;
    int networkID; // Using Class A IPv4 addresses - This should match first byte of address
    boolean isActive;
    FailMethod failMethod;
    Algorithm sortingAlgorithm;

    public Router(int networkID, FailMethod failMethod, Algorithm sortingAlgorithm){
        this.networkID = networkID;
        this.failMethod = failMethod;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    // Routes packet to next router. Returns true if successful.
    public boolean routePacket (Packet packet){
        // If the destination is in this router's network, don't need to go to other routers
        if (isDestinationNetwork(packet.getDestinationAddress())) {
            deliverPacketToHost(packet);
            return true;
        }
        else { // Keep going

            // TODO
            sortingAlgorithm.calculateRoute(null);
            return routePacket(packet);
        }
    }

    // Checks to see if the packet is for this router's network
    private boolean isDestinationNetwork(IPv4Address destinationAddress) {
        return destinationAddress.getNetwork() == this.networkID;
    }

    // *Note: Host machines aren't in this simulation, just network routers.
    // Instead, pretend we delivered it and print application data
    private void deliverPacketToHost(Packet packet) {
        System.out.println("-- * PACKET DELIVERED! * --");
        System.out.println("Router: " + networkID + ".0.0.1");
        System.out.println("Host: " + packet.getDestinationAddress().getAddress());
        System.out.println("Application Data: " + packet.getApplicationData() + "\n");
    }
}
