// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the router that a packet is sent through.
import java.util.ArrayList;
import java.util.List;

public class Router {
    List<Router> knownRouters = new ArrayList<Router>();
    List<Integer> linkCosts = new ArrayList<Integer>();
    int networkID; // Using Class A IPv4 addresses - This should match first byte of address
    boolean isActive;
    FailMethod failMethod;
    Algorithm sortingAlgorithm;

    public Router(int networkID, FailMethod failMethod, Algorithm sortingAlgorithm) {
        this.networkID = networkID;
        this.failMethod = failMethod;
        this.sortingAlgorithm = sortingAlgorithm;

        isActive = true;
    }

    // Prints router information
    public void printInfo() {
        System.out.print("* ---- Router -- ");
        System.out.print("ID: " + networkID + " -- ");
        System.out.print("Active: " + isActive + " -- ");
        System.out.println("---- *");
    }

    // Prints all known routers
    public void printKnownRouters() {

        for (int i = 0; i < knownRouters.size(); i++) {
            System.out.print("      -> (COST: " + linkCosts.get(i) + ") ");
            knownRouters.get(i).printInfo();
        }
    }

    public void addKnownRouter(Router newRouter, Integer cost) {
        this.knownRouters.add(newRouter);
        this.linkCosts.add(cost);
    }

    // Routes packet to next router. Returns true if successful.
    public boolean routePacket (Packet packet) {
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
