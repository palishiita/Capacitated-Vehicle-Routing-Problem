
package greedy;

import cvrp.*;
import genetic.*;
import java.util.*;

public class GreedySearch {
    private CVRP problem;

    public GreedySearch(CVRP problem) {
        this.problem = problem;
    }

    public Individual run() {
        Set<Integer> unvisited = new HashSet<>();
        for (Location city : problem.cities) {
            unvisited.add(city.id);
        }

        List<Integer> route = new ArrayList<>();
        int depotId = problem.depot.id;
        int current = depotId;
        double load = 0.0;

        while (!unvisited.isEmpty()) {
            int nextCity = -1;
            double minDistance = Double.MAX_VALUE;

            for (int cityId : unvisited) {
                int demand = problem.demands.get(cityId);
                if (load + demand <= problem.vehicleCapacity) {
                    int currIndex = problem.getMatrixIndex(current);
                    int nextIndex = problem.getMatrixIndex(cityId);
                    double distance = problem.distanceMatrix[currIndex][nextIndex];
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextCity = cityId;
                    }
                }
            }

            if (nextCity == -1) {
                // No valid city, return to depot
                current = depotId;
                load = 0.0;
            } else {
                route.add(nextCity);
                load += problem.demands.get(nextCity);
                current = nextCity;
                unvisited.remove(nextCity);
            }
        }

        // Build Individual and evaluate fitness
        Individual greedyIndividual = new Individual(problem);
        greedyIndividual.route = route;
        greedyIndividual.evaluateFitness();
        return greedyIndividual;
    }
}
