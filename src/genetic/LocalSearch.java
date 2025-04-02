package genetic;

import java.util.*;

public class LocalSearch {

    public static Individual optimize(Individual ind) {
        List<Integer> route = new ArrayList<>(ind.route);
        boolean improved = true;

        while (improved) {
            improved = false;
            for (int i = 1; i < route.size() - 2; i++) {
                for (int j = i + 1; j < route.size() - 1; j++) {
                    if (j - i == 1) continue; // Skip adjacent nodes

                    List<Integer> newRoute = new ArrayList<>(route);
                    Collections.reverse(newRoute.subList(i, j));

                    double oldDistance = ind.problem.calculateRouteDistance(route);
                    double newDistance = ind.problem.calculateRouteDistance(newRoute);

                    if (newDistance < oldDistance) {
                        route = newRoute;
                        improved = true;
                    }
                }
            }
        }

        Individual optimized = new Individual(ind.problem);
        optimized.route = route;
        optimized.evaluateFitness();
        return optimized;
    }
}

