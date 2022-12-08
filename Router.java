// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing the router that a packet is sent through.
// The Router class is named Router because this simulation primarily focuses on routing algorithms.
//      However, some functions of the Router class may be more similar to a link layer switch.
import java.util.ArrayList;
import java.util.List;

public class Router {
    int networkID; // Using Class A IPv4 addresses - This should match first byte of address
    int MACAddress; // In this case, using the id in the list/array
    boolean isActive;
    FailMethod failMethod;
    
    // Dijkstras
    DijkstrasAlgorithm dijkstras;
    int[][] graphNetwork;
    List<Router> allRouters = new ArrayList<Router>();

    // Naiive Approach
    NaiiveAlgorithm naiive;
    List<Router> knownRouters = new ArrayList<Router>();
    List<Integer> linkCosts = new ArrayList<Integer>();

    public Router(int networkID, int MACAddress, int numRouters) {
        this.networkID = networkID;
        this.MACAddress = MACAddress;
        this.dijkstras = new DijkstrasAlgorithm(numRouters);
        this.naiive = new NaiiveAlgorithm(this);

        isActive = true;
    }

    // Used to set isActive externally
    public void setIsActive(boolean isActive) {
        System.out.println("Router " + this.getMACAddress() + " isActive: " + isActive);
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    // Used to set MAC Address
    public void setMACAddress(int MACAddress) {
        this.MACAddress = MACAddress;
    }

    // Used to check MAC Address - In reality, this wouldn't be requested from router in this way
    public int getMACAddress() {
        return MACAddress;
    }

    public List<Router> getKnownRouters() {
        return knownRouters;
    }

    // Prints router information
    public void printInfo() {
        System.out.print("* ---- Router -- ");
        System.out.print("ID: " + networkID + " -- ");
        System.out.print("Active: " + isActive + " -- ");
        System.out.println("---- *");
    }

    // Naiive Approach - Prints all known routers
    public void printKnownRouters() {

        for (int i = 0; i < knownRouters.size(); i++) {
            System.out.print("      -> (COST: " + linkCosts.get(i) + ") ");
            knownRouters.get(i).printInfo();
        }
    }

    // Naiive Approach - Adds a known router
    public void addKnownRouter(Router newRouter, Integer cost) {
        this.knownRouters.add(newRouter);
        this.linkCosts.add(cost);
        this.naiive.setKnownRouters(knownRouters);
    }

    // Dijkstras - Sets the entire network of routers in graph form
    public void setNetworkGraph(int[][] networkGraph, List<Router> allRouters) {
        this.graphNetwork = networkGraph;
        this.allRouters = allRouters;
    }

    // Naiive - Sees if knows the destination router directly or indirectly
    public Router knowsRouter(int destinationMAC, List<Integer> knowsDestination, List<Integer> doesntKnowDestination, List<Integer> querySources, List<Integer> pathSoFar) {
        // 1 - Check self
        if (this.getMACAddress() == destinationMAC) {
            knowsDestination.add(this.getMACAddress());
            return this;
        }

        for (Router neighbor: knownRouters) {
            // 2 - Check if neighbors are destination
            if (neighbor.getMACAddress() == destinationMAC) {
                knowsDestination.add(this.getMACAddress());
                return this;
            }
            // 3 - Check if neighbors have already been checked, know destination, and haven't been sent packet yet
            if (knowsDestination.contains(neighbor.getMACAddress()) && !pathSoFar.contains(neighbor.getMACAddress())) {
                knowsDestination.add(this.getMACAddress());
                return this;
            }
        }

        // 4 - Ask neighbor, as long as they haven't checked already, aren't the ones asking, and haven't been set packet yet
        for (Router neighbor: knownRouters) {
            if (!doesntKnowDestination.contains(neighbor.getMACAddress()) && !querySources.contains(neighbor.getMACAddress()) && !pathSoFar.contains(neighbor.getMACAddress())) {
                querySources.add(this.getMACAddress()); // Add self to query sources so neighbor router knows not to query this router back
                Router result = neighbor.knowsRouter(destinationMAC, knowsDestination, doesntKnowDestination, querySources, pathSoFar); // Recursive call
                if (result != null) {
                    knowsDestination.add(this.getMACAddress());
                    return this;
                }
            }
        }

        // If we made it this far, then all checks have failed
        querySources.clear();
        doesntKnowDestination.add(MACAddress);
        return null;

    }

    // Naiive - Returns the cost of the link to a desired router
    public int getCost (Router router) {
        int id = knownRouters.indexOf(router);
        return linkCosts.get(id);
    }

    // Calculates route and routes packet to next router. Returns time taken.
    public long routePacket (Packet packet) {
        if (isActive) {
            long startTime = System.nanoTime(); // Used to calculate time
            long endTime;

            // If the destination is in this router's network, don't need to go to other routers
            if (isDestinationNetwork(packet.getDestinationAddress())) {
                deliverPacketToHost(packet);
                endTime = System.nanoTime();
                return endTime - startTime;
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
                        printPath(path);

                        // Pass packet to next router
                        boolean successfulPass = passPacket(packet, path);
                        endTime = System.nanoTime();
                        if (successfulPass) {
                            return endTime - startTime;
                        }
                        else {
                            return Long.MAX_VALUE; // Failed to pass; Took infinite time
                        }
                    case 'N':
                    System.out.print("At router " + this.getMACAddress());
                        List<Integer> knowsDestination = new ArrayList<Integer>(); // Holds MACAddresses that are known to have links to dest
                        List<Integer> doesntKnowDestination = new ArrayList<Integer>(); // Holds MACAddresses that have been checked but don't have links to dest
                        List<Integer> querySources = new ArrayList<Integer>(); // Holds MACAddresses of querying routers so no endless loop
                        doesntKnowDestination.add(this.getMACAddress()); // We don't know if this router has a link; assume false

                        packet.addAddressToPath(this.getMACAddress()); // We're on the path if the packet was sent here
                        List<Integer> pathSoFar = packet.getPathSoFar(); // Routers the packets have been to already; Don't want to go backwards

                        Router nextRouter = naiive.naiive(destMAC, knowsDestination, doesntKnowDestination, querySources, pathSoFar);

                        nextRouter.routePacket(packet);
                        endTime = System.nanoTime();
                        return endTime - startTime;
                    default:
                        return Long.MAX_VALUE; // Failed to route; Took infinite time
                }
            }
        }
        else {
            return Long.MAX_VALUE; // Inactive - Failed to route; Took infinite time
        }
    }

    // Dijkstra's - Passes packet on predetermined route
    public boolean passPacket (Packet packet, List<Integer> path) {
        // Only accept packet if active
        if (isActive) {
            // If the destination is in this router's network, don't need to go to other routers
            if (isDestinationNetwork(packet.getDestinationAddress())) {
                deliverPacketToHost(packet);
                return true;
            }
            else { // Pass it on
                // Find next router in sequence, pass packet on
                path.remove(0); // Removing this ID
                int nextRouter = path.get(0);
                
                return allRouters.get(nextRouter).passPacket(packet, path);
            }
        }
        else { // Router is inactive - failed to pass
            return false;
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

    public void printPath(List<Integer> path) {
        System.out.print("\nPATH : ");

        for (Integer ID : path) {
            System.out.print(ID + " ");
        }

        System.out.println('\n');
    }
}
