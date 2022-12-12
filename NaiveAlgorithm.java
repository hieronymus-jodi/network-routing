// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Contains all of the necessary features to sort using Naive approach.

import java.util.List;
import java.util.ArrayList;

public class NaiveAlgorithm {
    Router parentRouter;
    List<Router> knownRouters = new ArrayList<Router>(); // List of all routers the router running the algorithm knows

    public NaiveAlgorithm(Router parentRouter) {
        this.parentRouter = parentRouter;
    }

    public void setKnownRouters(List<Router> knownRouters) {
        this.knownRouters = knownRouters;
    }

    // Prints routers that are discovered as options
    private void printOptions (List<Router> options) {
        System.out.print("--> Routing options for " + parentRouter.getMACAddress() + " (" + options.size() + " available): ");
        for (Router router : options) {
            System.out.print(router.getMACAddress() + " ");
        }
        System.out.println();
    }

    // (Unused - for multidirectional links) Utility function that returns the routers the parent router has a direct link to that have path to destination
    public List<Router> findLinkRouters(int destMAC, List<Integer> knowsDestination, List<Integer> doesntKnowDestination, List<Integer> querySources, List<Integer> pathSoFar) {
        List<Router> knowsDest = new ArrayList<Router>();

        for (Router router : knownRouters) {
            // Only check if hasn't been checked, hasn't been to yet
            if (!knowsDestination.contains(router.getMACAddress()) && !doesntKnowDestination.contains(router.getMACAddress())) {
                Router knows = router.knowsRouter(destMAC, knowsDestination, doesntKnowDestination, querySources, pathSoFar);
                if (knows != null) {
                    System.out.println("Router " + router.getMACAddress() + " knows " + destMAC);
                    knowsDest.add(router);
                }
            }
        }

        return knowsDest;
    }

    public Router naive(int destMAC, List<Integer> knowsDestination, List<Integer> doesntKnowDestination, List<Integer> querySources, List<Integer> pathSoFar) {
        // Create list of routers that are on the path to the destination
        // List<Router> routersToDest = findLinkRouters(destMAC, knowsDestination, doesntKnowDestination, querySources, pathSoFar);
        List<Router> routersToDest = parentRouter.getKnownRouters();
        printOptions(routersToDest);

        // Find minimum cost link
        Integer minCost = Integer.MAX_VALUE;
        Router minRouter = null;

        for (Router option : routersToDest) {
            int cost = parentRouter.getCost(option);

            if ((cost < minCost) && (option.getIsActive())) {
                minCost = cost;
                minRouter = option;
            }
        }

        System.out.println("... Cost to " + minRouter.getMACAddress() + ": " + minCost);

        return minRouter;
    }
}
