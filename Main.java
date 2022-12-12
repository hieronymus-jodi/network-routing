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
        int[][] knownRouters = { {1,2}, {3,4,5}, {3,4,5}, {6,7}, {6,7}, {6,7}, {8}, {8}, {} };
        // Costs are in ms
        int[][] linkCosts = { {1,3}, {6,5,7}, {4,5,2}, {8,3}, {4,7}, {5,4}, {2}, {1}, {} };

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

        // Route naively
        routeNaiveAllActive(network.get(0), packet);
        routeNaiveRouterInactive(network.get(0), packet, network, 4);

        // Route packet using Dijkstra's
        routeDijkstrasAllActive(network.get(0), packet);
        routeDijkstrasRouterInactive(network.get(0), packet, network, 5);

    }

    // Instantiates network
    private static List<Router> createNetwork(int[] networkIDs, int[][]knownRouters, int[][] linkCosts) {
        System.out.println(" ---- ---- ---- ---- ---- ");
        System.out.println(" --- Creating Network --- ");
        System.out.println(" ---- ---- ---- ---- ---- \n");

        List<Router> routerList = new ArrayList<Router>();

        // Need to make all routers first
        int MACAddress = 0;
        for (int id : networkIDs) {
            routerList.add( new Router(id, MACAddress, networkIDs.length) );
            MACAddress++;
        }

        // Connect routers
        for(int i = 0; i < knownRouters.length; i++) { // For each router i
            for(int j = 0; j < knownRouters[i].length; j++) { // For each router j that i knows
                Router knownRouter = routerList.get(knownRouters[i][j]);
                System.out.println("Router " + routerList.get(i).getMACAddress() + " knows " + knownRouter.getMACAddress());
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

    // Routes packet using Naive algorithm NUMTEST times. Return time results
    //      All routers are active
    private static List<Long> routeNaiveAllActive(Router sourceRouter, Packet packet) {
        List<Long> times = new ArrayList<Long>();
        packet.setNaive();

        for (int testNum = 1; testNum < NUMTESTS + 1; testNum++) {
            System.out.println("-- -- NAIVE TEST (ALL ACTIVE) #" + testNum + "/" + NUMTESTS + " -- --");

            long time = sourceRouter.routePacket(packet);
            times.add(time);

            System.out.println("-- NAIVE (ALL ACTIVE) #" + testNum + "/" + NUMTESTS + " RESULT: " + time + "ns");
        }

        printTimeResults("NAIVE'S (ALL ACTIVE)", times);
        return times;

    }

    // Routes packet using Naive Algorithm NUMTESTS times. Returns time results
    //      Sets specified router as inactive
    private static List<Long> routeNaiveRouterInactive(Router sourceRouter, Packet packet, List<Router> network, int inactiveRouterID) {
        List<Long> times = new ArrayList<Long>();
        packet.setNaive();

        // Start with all routers active
        System.out.println("-- -- NAIVE TEST (FAILED ROUTER) # 1/" + NUMTESTS + " -- --");
        long time = sourceRouter.routePacket(packet);
        times.add(time);
        System.out.println("-- NAIVE (FAILED ROUTER) #1/" + NUMTESTS + " RESULT: " + time + "ns");

        // Set router as inactive
        network.get(inactiveRouterID).setIsActive(false);
        // Continue at 2
        for (int testNum = 2; testNum < NUMTESTS + 1; testNum++) {
            System.out.println("-- -- NAIVE TEST (FAILED ROUTER) #" + testNum + "/" + NUMTESTS + " -- --");

            time = sourceRouter.routePacket(packet);
            times.add(time);

            System.out.println("-- NAIVE (FAILED ROUTER) #" + testNum + "/" + NUMTESTS + " RESULT: " + time + "ns");
        }

        network.get(inactiveRouterID).setIsActive(true); // Turn back on for future tests

        printTimeResults("NAIVE'S (FAILED ROUTER)", times);
        return times;
    }

    // Routes packet using Dijkstra's Algorithm NUMTESTS times. Returns time results
    //      All routers are active
    private static List<Long> routeDijkstrasAllActive(Router sourceRouter, Packet packet) {
        List<Long> times = new ArrayList<Long>();
        packet.setDijkstras();

        for (int testNum = 1; testNum < NUMTESTS + 1; testNum++) {
            System.out.println("-- -- DIJKSTRAS TEST (ALL ACTIVE) #" + testNum + "/" + NUMTESTS + " -- --");

            long time = sourceRouter.routePacket(packet);
            times.add(time);

            System.out.println("-- DIJSKTRAS (ALL ACTIVE) #" + testNum + "/" + NUMTESTS + " RESULT: " + time + "ns");
        }

        printTimeResults("DIJKSTRA'S (ALL ACTIVE)", times);
        return times;
    }

    // Routes packet using Dijkstra's Algorithm NUMTESTS times. Returns time results
    //      Sets specified router as inactive
    private static List<Long> routeDijkstrasRouterInactive(Router sourceRouter, Packet packet, List<Router> network, int inactiveRouterID) {
        List<Long> times = new ArrayList<Long>();
        packet.setDijkstras();

        // Start with all routers active
        System.out.println("-- -- DIJKSTRAS TEST (FAILED ROUTER) # 1/" + NUMTESTS + " -- --");
        long time = sourceRouter.routePacket(packet);
        times.add(time);
        System.out.println("-- DIJSKTRAS (FAILED ROUTER) #1/" + NUMTESTS + " RESULT: " + time + "ns");

        // Set router as inactive
        network.get(inactiveRouterID).setIsActive(false);
        // Continue at 2
        for (int testNum = 2; testNum < NUMTESTS + 1; testNum++) {
            System.out.println("-- -- DIJKSTRAS TEST (FAILED ROUTER) #" + testNum + "/" + NUMTESTS + " -- --");

            time = sourceRouter.routePacket(packet);
            times.add(time);

            System.out.println("-- DIJSKTRAS (FAILED ROUTER) #" + testNum + "/" + NUMTESTS + " RESULT: " + time + "ns");
        }

        network.get(inactiveRouterID).setIsActive(true); // Turn back on for future tests

        printTimeResults("DIJKSTRA'S (FAILED ROUTER)", times);
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
        System.out.println("---- ---- ---- ---- ---- ---- ---- ----");
    }

}
