package genetic;

import cvrp.*;
import java.util.*;

public class Individual {
    public static int evaluationCount = 0; // Static counter for fitness evaluations

    public List<Integer> route;           // Order of city visits (city IDs)
    public double fitness;                // Total distance (lower is better)
    CVRP problem;                         // Reference to the problem instance

    // Constructor: generate empty individual linked to a problem
    public Individual(CVRP problem) {
        this.problem = problem;
        this.route = new ArrayList<>();
        this.fitness = Double.MAX_VALUE;
    }

    // Deep copy constructor – clone an individual
    public Individual(Individual other) {
        this.problem = other.problem;
        this.route = new ArrayList<>(other.route);
        this.fitness = other.fitness;
    }

    // Randomize the route: permutation of city IDs (excluding the depot)
    public void randomizeRoute() {
        this.route = new ArrayList<>();
        for (Location city : problem.cities) {
            this.route.add(city.id);
        }
        Collections.shuffle(this.route);
    }

    // Evaluate fitness (total distance) with capacity constraints
    public void evaluateFitness() {
        double totalDistance = 0.0;
        int depotId = problem.depot.id;
        int depotIndex = problem.getMatrixIndex(depotId);
        int currentLoad = 0;
        int previous = depotId;

        for (int cityId : route) {
            int demand = problem.demands.get(cityId);
            int prevIndex = problem.getMatrixIndex(previous);
            int cityIndex = problem.getMatrixIndex(cityId);

            if (currentLoad + demand > problem.vehicleCapacity) {
                // Return to depot and start new route
                totalDistance += problem.distanceMatrix[prevIndex][depotIndex];
                totalDistance += problem.distanceMatrix[depotIndex][cityIndex];
                currentLoad = demand;
            } else {
                totalDistance += problem.distanceMatrix[prevIndex][cityIndex];
                currentLoad += demand;
            }

            previous = cityId;
        }

        // Return to depot from last city
        int lastIndex = problem.getMatrixIndex(previous);
        totalDistance += problem.distanceMatrix[lastIndex][depotIndex];

        this.fitness = totalDistance;

        evaluationCount++;
    }

    // Reset the static evaluation counter
    public static void resetEvaluationCount() {
        evaluationCount = 0;
    }

    // Debug: print route and fitness
    public void printRoute() {
        System.out.println("Route: " + route + " | Fitness: " + fitness);
    }

    // Debug: print route with depot visits
    public void printFullRouteWithDepot() {
        int depot = problem.depot.id;
        int currentLoad = 0;
        int previous = depot;

        System.out.print("Depot → ");
        for (int cityId : route) {
            int demand = problem.demands.get(cityId);
            if (currentLoad + demand > problem.vehicleCapacity) {
                System.out.print("Depot → ");
                currentLoad = 0;
                previous = depot;
            }
            System.out.print(cityId + " → ");
            currentLoad += demand;
            previous = cityId;
        }
        System.out.println("Depot");
    }
}
