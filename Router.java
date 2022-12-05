// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the router that a packet is sent through.
import java.util.ArrayList;
import java.util.List;

public class Router {
    int networkID; // Using Class A IPv4 addresses - This should match first byte of address
    boolean isActive;
    FailMethod failMethod;
    
    // Dijkstras
    DijkstrasAlgorithm dijkstras;
    int[][] graphNetwork;

    // Belman Ford
    List<Router> knownRouters = new ArrayList<Router>();
    List<Integer> linkCosts = new ArrayList<Integer>();

    public Router(int networkID, int numRouters) {
        this.networkID = networkID;
        this.dijkstras = new DijkstrasAlgorithm(numRouters);

        isActive = true;
    }

    // Prints router information
    public void printInfo() {
        System.out.print("* ---- Router -- ");
        System.out.print("ID: " + networkID + " -- ");
        System.out.print("Active: " + isActive + " -- ");
        System.out.println("---- *");
    }

    // Belman Ford - Prints all known routers
    public void printKnownRouters() {

        for (int i = 0; i < knownRouters.size(); i++) {
            System.out.print("      -> (COST: " + linkCosts.get(i) + ") ");
            knownRouters.get(i).printInfo();
        }
    }

    // Belman Ford - Adds a known router
    public void addKnownRouter(Router newRouter, Integer cost) {
        this.knownRouters.add(newRouter);
        this.linkCosts.add(cost);
    }

    // Dijkstras - Sets the entire network of routers in graph form
    public void setNetworkGraph(int[][] networkGraph) {
        this.graphNetwork = networkGraph;
    }

    // Calculates route and routes packet to next router. Returns true if successful.
    public boolean routePacket (Packet packet) {
        // If the destination is in this router's network, don't need to go to other routers
        if (isDestinationNetwork(packet.getDestinationAddress())) {
            deliverPacketToHost(packet);
            return true;
        }
        else { // Calculate route and keep going
            char method = packet.getRouteMethod();
            int sourceMAC = packet.getSourceMAC();
            int destMAC = packet.getDestinationMAC();

            switch (method) {
                case 'D':
                    // Only run algorithm if haven't already calculated
                    if(!dijkstras.getRanDijkstras()) {
                        dijkstras.dijkstra(graphNetwork, sourceMAC);
                    }
                    List<Integer> path = new ArrayList<Integer>();
                    dijkstras.getPath(destMAC, path);
                    dijkstras.printPath(path);
            }
            // sortingAlgorithm.calculateRoute(null);
            // return routePacket(packet);
            return true;
        }
    }

    // Passes packet on predetermined route
    public boolean passPacket (Packet packet, List<Integer> networkIDs) {
        // If the destination is in this router's network, don't need to go to other routers
        if (isDestinationNetwork(packet.getDestinationAddress())) {
            deliverPacketToHost(packet);
            return true;
        }
        else { // Pass it on
            // Find next router in sequence, pass packet on
            boolean successfulPass = false;
            for (Router router : this.knownRouters) {
                if (router.networkID == networkIDs.get(0)) {
                    networkIDs.remove(0); // Remove router from sequence
                    successfulPass = router.passPacket(packet, networkIDs);
                    break;
                }
            }
            
            return successfulPass;
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
