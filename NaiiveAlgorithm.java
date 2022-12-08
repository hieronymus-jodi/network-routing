// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Contains all of the necessary features to sort using Naiive approach.

import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import java.util.ArrayList;

public class NaiiveAlgorithm {
    Router parentRouter;
    List<Router> knownRouters = new ArrayList<Router>(); // List of all routers the router running the algorithm knows

    public NaiiveAlgorithm(Router parentRouter) {
        this.parentRouter = parentRouter;
    }

    public void setKnownRouters(List<Router> knownRouters) {
        this.knownRouters = knownRouters;
    }

    // Utility function that returns whether this router has a path to the destination router
    //          `stepLimit` limits how many routers away from current router to go
    public List<Router> knowsDestination(int destMAC, int stepLimit, List<Integer> checkedMACAddresses, List<Boolean> checkResult) {
        List<Router> knowsDest = new ArrayList<Router>();
        // Only continue if we're not at limit
        if (stepLimit > 0) {
            for (Router router : knownRouters) {
                // If the router hasn't already been checked, check it
                if (checkedMACAddresses.contains(router.getMACAddress())) {
                    // if result was pos, add router
                    //else if next check
                }
                if (!checkedMACAddresses.contains(router.getMACAddress())) {
                    Router knows = router.knowsRouter(destMAC, stepLimit-1, checkedMACAddresses, checkResult);
                    if (knows != null) {
                        System.out.println("Router " + router.getMACAddress() + " knows " + destMAC);
                        knowsDest.add(router);
                    }
                }
            }
        }
        return knowsDest;
    }

    public void naiive(int destMAC, List<Integer> path) {
        // Create list of routers that are on the path to the destination
        List<Router> routersToDest = new ArrayList<Router>();
        

    }
}
