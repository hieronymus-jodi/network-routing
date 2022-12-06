// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// File that sets up and runs tests

import java.util.ArrayList;
import java.util.List;

public class Main {
    static final int NUMROUTERS = 9;
    static final int NUMTESTS = 15;
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
        setRoutersGraphs(network, graphNetwork);

        // Create packet
        int sourceMAC = 0;
        int destinationMAC = 8;
        IPv4Address sourceAddress = new IPv4Address(1, 0, 1, 2);
        IPv4Address destAddress = new IPv4Address(126, 10, 1, 8);
        String appData = "~* Yay! I made it to my destination! *~";
        Packet packet = new Packet(sourceMAC, destinationMAC, sourceAddress, destAddress, appData);

        // Route packet using Dijkstra's
        routeDijkstras(network.get(0), packet);
    }

    // Instantiates network
    private static List<Router> createNetwork(int[] networkIDs, int[][]knownRouters, int[][] linkCosts) {
        System.out.println(" ---- ---- ---- ---- ---- ");
        System.out.println(" --- Creating Network --- ");
        System.out.println(" ---- ---- ---- ---- ---- \n");

        List<Router> routerList = new ArrayList<Router>();

        // Need to make all routers first
        for (int id : networkIDs) {
            routerList.add( new Router(id, networkIDs.length) );
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

    // Add graph representation to each router
    private static void setRoutersGraphs(List<Router> network, int[][] graphNetwork) {
        for (Router router : network) {
            router.setNetworkGraph(graphNetwork, network);
        }
    }

    // Routes packet using Dijkstra's Algorithm NUMTESTS times. Returns time results
    private static List<Long> routeDijkstras(Router sourceRouter, Packet packet) {
        List<Long> times = new ArrayList<Long>();
        packet.setDijkstras();
        for (int i = 0; i < NUMTESTS; i++) {
            int testNum = i + 1;
            System.out.println("-- -- DIJKSTRAS TEST #" + testNum + "/" + NUMTESTS + " -- --");

            long time = sourceRouter.routePacket(packet);
            times.add(time);

            System.out.println("-- DIJSKTRAS #" + testNum + "/" + NUMTESTS + " RESULT: " + time + "ns");
        }

        printTimeResults("DIJKSTRA'S", times);
        return times;
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

    // Prints time results
    private static void printTimeResults(String testName, List<Long> rawTimes) {
        // long timeMS = rawTime[i] / 1000000; // Converts to milliseconds
        System.out.println("---- ---- * " + testName + " RESULTS * ---- ----");
        for (int testNum = 1; testNum < NUMTESTS + 1; testNum++) {
            String testTime = rawTimes.get(testNum - 1) + "ns";
            System.out.println("--- > " + testNum + " / " + NUMTESTS + ": " + testTime);
        }
        System.out.println("---- ---- ---- ---- ---- ---- ---- ----\n");
    }

}
