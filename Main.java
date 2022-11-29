// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// File that sets up and runs tests

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(" *** *** *** *** *** *** *** *** *** *** *** *** ");
        System.out.println(" *** *** *** *** Network Routing *** *** *** *** ");
        System.out.println(" *** *** *** * By: Jodi Hieronymus * *** *** *** ");
        System.out.println(" *** *** *** *** *** *** *** *** *** *** *** *** \n");

        // Create routers
        int[] networkIDs = { 1, 40, 23, 119, 103, 33, 104, 10, 126 };
        int[][] knownRouters = { {1,2}, {0,3,4,5}, {0,3,4,5}, {1,2,6,7}, {1,2,6,7}, {1,2,6,7}, {3,4,5,8}, {3,4,5,8}, {6,7} };
        int[][] linkCosts = { {1,3}, {1,6,5,7}, {3,4,5,2}, {6,4,8,3}, {5,5,4,7}, {7,2,5,4}, {8,4,5,2}, {3,7,4,1}, {2,1} };

        List<Router> network = createNetwork(networkIDs, knownRouters, linkCosts);
        int[][] graphNetwork = createGraph(knownRouters, linkCosts);
        printNetwork(network);
        printGraph(graphNetwork);

        // Create packet
        IPv4Address sourceAddress = new IPv4Address(1, 0, 1, 2);
        IPv4Address destAddress = new IPv4Address(126, 10, 1, 8);
        String appData = "~* Yay! I made it to my destination! *~";
        Packet packet = new Packet(sourceAddress, destAddress, appData);

        // Route packet using Dijkstra's
        dijkstraRouting(network, graphNetwork, packet);
    }

    // Instantiates network
    private static List<Router> createNetwork(int[] networkIDs, int[][]knownRouters, int[][] linkCosts) {
        System.out.println(" ---- ---- ---- ---- ---- ");
        System.out.println(" --- Creating Network --- ");
        System.out.println(" ---- ---- ---- ---- ---- \n");

        List<Router> routerList = new ArrayList<Router>();

        // Need to make all routers first
        for (int id : networkIDs) {
            routerList.add( new Router(id, null, null) );
        }

        // Connect routers
        for(int i = 0; i < knownRouters.length; i++) { // For each router i
            for(int j = 0; j < knownRouters[i].length; j++) { // For each router j that i knows
                Router knownRouter = routerList.get(knownRouters[i][j]);
                routerList.get(i).addKnownRouter(knownRouter, linkCosts[i][j]);
            }
        }

        return routerList;
    }

    // Creates graph representation of network for Dijkstra's
    private static int[][] createGraph(int[][]knownRouters, int[][] linkCosts) {
        // Start with empty graph
        int graphNetwork[][]
            = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 7
                            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }  // 8
                        };

        // Populate graph with costs
        for(int i = 0; i < knownRouters.length; i++) { // For each router i
            for(int j = 0; j < knownRouters[i].length; j++) { // For each router j that i knows
                int known = knownRouters[i][j];
                graphNetwork[i][known] = linkCosts[i][j]; // Save the cost
            }
        }

        return graphNetwork;
    }

    // Prints network info
    private static void printNetwork(List<Router> routerList) {
        System.out.println(" +++ +++ +++ NETWORK +++ +++ +++ ");
        for (Router router : routerList) {
            router.printInfo();
            router.printKnownRouters();
        }
        System.out.println(" +++ +++ +++ +++ +++ +++ +++ +++ \n");
    }

    // Prints graph
    private static void printGraph(int[][] graph) {
        System.out.println(" +++ +++ +++ GRAPH +++ +++ +++ ");
        for(int[] row : graph) { // For each row
            System.out.print("      ");
            for(int item : row) { // For each item in the row
                System.out.print(item + " ");
            }
            System.out.println();
        }
        System.out.println(" +++ +++ +++ +++ +++ +++ +++ +++ \n");
    }

    // Finds route for packet using Dijkstra's algorithm
    // Reference: https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
    private static boolean dijkstraRouting(List<Router> routers, int[][] graph, Packet packet) {
        // Get IPS from packet, convert to networks
        int srcNet = packet.getSourceAddress().getNetwork();
        int destNet = packet.getDestinationAddress().getNetwork();

        // Holds ID of source and destination routers based on their ID in the list
        int srcID = -1, destID = -1;
        for (int i = 0; i < routers.size(); i++) {
            if (srcNet == routers.get(i).networkID) {
                srcID = i;
            }
            else if (destNet == routers.get(i).networkID) {
                destID = i;
            }
        }

        System.out.println("SRC: " + srcID + " - DEST: " + destID);
        // If failed to find routers
        if (srcID == -1 | destID == -1) {
            return false;
        }

        return true;
    }
}
