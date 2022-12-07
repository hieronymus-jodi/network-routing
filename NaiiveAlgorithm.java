// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Contains all of the necessary features to sort using Naiive approach.

import java.util.List;
import java.util.ArrayList;

public class NaiiveAlgorithm {
    List<Router> knownRouters = new ArrayList<Router>(); // List of all routers the router running the algorithm knows

    public NaiiveAlgorithm() {

    }

    public void setKnownRouters(List<Router> knownRouters) {
        this.knownRouters = knownRouters;
    }

    // Utility function that returns whether this router has a path to the destination router
    //          `stepLimit` limits how many routers away from current router to go
    public boolean knowsDestination(int destMAC, int stepLimit) {
        // Only continue if we're not at limit
        if (stepLimit > 0) {
            // Check all routers we know
            for (Router router : knownRouters) {
                if (router.getMACAddress() == destMAC) {
                    return true;
                }
            }

            // If we get this far, then it may be other steps away
            for (Router router : knownRouters) {
                if (router.checkIfLinkToDestination(destMAC, stepLimit) == true) {
                    return true;
                }
            }
        }

        return false; // If we get this far, this router isn't on the path
    }

    public void naiive(int destMAC, List<Integer> path) {
        // Create list of routers that are on the path to the destination
        List<Router> routersToDest = new ArrayList<Router>();
        

    }
}
