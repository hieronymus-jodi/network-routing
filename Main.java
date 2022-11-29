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
        int[] networkIDs = { 191, 200, 197, 219, 213, 193, 204, 196, 222 };
        int[][] knownRouters = { {1,2}, {0,3,4,5}, {0,3,4,5}, {1,2,6,7}, {1,2,6,7}, {1,2,6,7}, {3,4,5,8}, {3,4,5,8}, {6,7} };
        int[][] linkCosts = { {1,3}, {1,6,5,7}, {3,4,5,2}, {6,4,8,3}, {5,5,4,7}, {7,2,5,4}, {8,4,5,2}, {3,7,4,1}, {2,1} };

        List<Router> network = createNetwork(networkIDs, knownRouters, linkCosts);
        int[][] graphNetwork = createGraph(knownRouters, linkCosts);
        printNetwork(network);
        printGraph(graphNetwork);
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
}
