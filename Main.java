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

        List<Router> network = createNetwork();
        printNetwork(network);
    }

    // Instantiates network
    private static List<Router> createNetwork() {
        System.out.println(" ---- ---- ---- ---- ---- ");
        System.out.println(" --- Creating Network --- ");
        System.out.println(" ---- ---- ---- ---- ---- \n");

        List<Router> routerList = new ArrayList<Router>();

        // Create routers
        int[] networkIDs = { 191, 200, 197, 219, 213, 193, 204, 196, 222 };
        int[][] knownRouters = { {1,2}, {3,4,5}, {3,4,5}, {6,7}, {6,7}, {6,7}, {8}, {8}, {} };
        int[][] linkCosts = { {1,3}, {6,5,7}, {4,5,2}, {8,3}, {4,7}, {5,4}, {2}, {1}, {} };

        // Need to make all routers first
        for (int id : networkIDs) {
            routerList.add( new Router(id, null, null) );
        }

        // Connect routers
        for(int i = 0; i < knownRouters.length; i++) { // For each router x
            for(int j = 0; j < knownRouters[i].length; j++) { // For each router x knows
                Router knownRouter = routerList.get(knownRouters[i][j]);
                routerList.get(i).addKnownRouter(knownRouter, linkCosts[i][j]);
            }
        }

        return routerList;
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
}
