// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Contains all of the necessary features to sort using Dijkstra's Algorithm.
// Reference: https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/ 
// Reference: https://www.geeksforgeeks.org/printing-paths-dijkstras-shortest-path-algorithm/

import java.util.List;

public class DijkstrasAlgorithm {
    int numRouters;
    boolean ranDijkstras; // Tells if Dijkstra's has already been run

    int cost[]; // Holds smallest cost src -> i
    int[] parents; // Holds parents from source to each vertex

    public DijkstrasAlgorithm(int numRouters) {
        this.numRouters = numRouters;
        ranDijkstras = false;

        this.cost = new int[numRouters];
        this.parents = new int[numRouters];
    }

    boolean getRanDijkstras() {
        return this.ranDijkstras;
    }

    // Utility function for Dijkstra's algorithm that finds the router
    //      with the minimum cost link
    int findMinCost(int cost[], Boolean sptRouters[])
	{
		// Initialize minimum cost and ID
		int min = Integer.MAX_VALUE, minID = -1;

		for (int r = 0; r < numRouters; r++)
			if (sptRouters[r] == false && cost[r] <= min) {
				min = cost[r];
				minID = r;
			}

		return minID;
	}

    public void printPath(List<Integer> path) {
        System.out.print("\nPATH : ");

        for (Integer ID : path) {
            System.out.print(ID + " ");
        }

        System.out.println('\n');
    }

    // Finds shortest path based on prev calculations
    public void getPath(int destMAC, List<Integer> path) {
        getSubPath(destMAC, path);
        path.add(destMAC);
    }

    // Utility function for finding path
    private void getSubPath(int currRouterID, List<Integer> path) {
        // If no parent
        if (parents[currRouterID] == -1)
        {
            return;
        }
        else {
            getSubPath(parents[currRouterID], path);
            path.add(parents[currRouterID]);
        }
    }

	// Finds route for packet using Dijkstra's algorithm
	void dijkstra(int graph[][], int src)
	{
        ranDijkstras = true;

		Boolean sptRouters[] = new Boolean[numRouters]; // True if i is included or finalized

        // No parent for source
        parents[src] = -1;

		// Initialize all costs as infinite, all routers as false
		for (int i = 0; i < numRouters; i++) {
			cost[i] = Integer.MAX_VALUE;
			sptRouters[i] = false;
		}

		// No cost for router to stay at itself - initialize to 0
		cost[src] = 0;

		// Find shortest path for all routers
		for (int count = 0; count < numRouters - 1; count++) {
			// Pick minimum cost router from unprocessed routers
			int nextMinID = findMinCost(cost, sptRouters);

			// Mark as processed
			sptRouters[nextMinID] = true;

			// Update costs of adjacent links
			for (int r = 0; r < numRouters; r++)

				// Only update if:
                //      1 - Unprocessed
                //      2 - Not 0 (Link exists)
                //      3 - Cost src -> j would be cheaper if routed through minCostID
				if (!sptRouters[r] && graph[nextMinID][r] != 0
					&& cost[nextMinID] != Integer.MAX_VALUE
					&& cost[nextMinID] + graph[nextMinID][r] < cost[r]) {
                    parents[r] = nextMinID;
					cost[r] = cost[nextMinID] + graph[nextMinID][r];
                }
		}

	}

    // Testing to make sure Dijkstras works.
    // Graph copied from GeeksForGeeks as linked above.
    public static void main(String[] args)
	{
		int graph[][]
			= new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 },
							{ 4, 0, 8, 0, 0, 0, 0, 11, 0 },
							{ 0, 8, 0, 7, 0, 4, 0, 0, 2 },
							{ 0, 0, 7, 0, 9, 14, 0, 0, 0 },
							{ 0, 0, 0, 9, 0, 10, 0, 0, 0 },
							{ 0, 0, 4, 14, 10, 0, 2, 0, 0 },
							{ 0, 0, 0, 0, 0, 2, 0, 1, 6 },
							{ 8, 11, 0, 0, 0, 0, 1, 0, 7 },
							{ 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
		DijkstrasAlgorithm t = new DijkstrasAlgorithm(9);

		t.dijkstra(graph, 0);
	}
}
