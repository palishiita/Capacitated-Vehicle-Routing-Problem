package genetic;

import cvrp.*;
import java.util.*;

public class Individual {
    public List<Integer> route;  // Order of city visits (city IDs)
    public double fitness;       // Total distance (lower is better)
    CVRP problem;        // Reference to the problem instance

    // Constructor: generate random route and evaluate fitness
    public Individual(CVRP problem) {
        this.problem = problem;
        this.route = new ArrayList<>();
        this.fitness = Double.MAX_VALUE;
    }

    // Deep copy constructor – useful for crossover/mutation to clone a parent before modifying.
    public Individual(Individual other) {
        this.problem = other.problem;
        this.route = new ArrayList<>(other.route);
        this.fitness = other.fitness;
    }

    // Creates a random permutation of all city IDs (excluding the depot) — used for initial population.
    public void randomizeRoute() {
        this.route = new ArrayList<>();
        for (Location city : problem.cities) {
            this.route.add(city.id);
        }
        Collections.shuffle(this.route);
    }

    // Evaluate fitness (total distance with capacity and depot returns)
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
    }

    public void printRoute() {
        System.out.println("Route: " + route + " | Fitness: " + fitness);
    }

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