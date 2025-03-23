package src.genetic;

import src.cvrp.*;
import java.util.*;

public class Individual {
    public List<Integer> route;  // Order of city visits (city IDs)
    public double fitness;       // Total distance (lower is better)
    private CVRP problem;        // Reference to the problem instance

    // Constructor: generate random route and evaluate fitness
    public Individual(CVRP problem) {
        this.problem = problem;
        this.route = generateRandomRoute();
        evaluateFitness();
    }

    // Deep copy constructor (used in crossover/mutation)
    public Individual(Individual other) {
        this.problem = other.problem;
        this.route = new ArrayList<>(other.route);
        this.fitness = other.fitness;
    }

    // Generate a random permutation of cities
    private List<Integer> generateRandomRoute() {
        List<Integer> cities = new ArrayList<>();
        for (Location city : problem.cities) {
            cities.add(city.id);
        }
        Collections.shuffle(cities);
        return cities;
    }

    // Evaluate fitness (total distance with capacity and depot returns)
    public void evaluateFitness() {
        double totalDistance = 0.0;
        double load = 0.0;
        int depotId = 0;

        int prev = depotId;
        for (int cityId : route) {
            int demand = problem.demands.get(cityId);

            if (load + demand > problem.vehicleCapacity) {
                // Return to depot
                totalDistance += problem.distanceMatrix[prev][depotId];
                prev = depotId;
                load = 0.0;
            }

            // Travel to city
            totalDistance += problem.distanceMatrix[prev][cityId];
            load += demand;
            prev = cityId;
        }

        // Final return to depot
        totalDistance += problem.distanceMatrix[prev][depotId];
        this.fitness = totalDistance;
    }

    // Simple route print
    public void printRoute() {
        System.out.println("Route: " + route.toString() + " | Fitness: " + fitness);
    }

    // Enhanced route print with depot visits shown
    public void printFullRouteWithDepot() {
        int depotId = 0;
        double load = 0.0;
        double totalDistance = 0.0;

        System.out.print("Route with depot: Depot → ");
        int prev = depotId;

        for (int cityId : route) {
            int demand = problem.demands.get(cityId);

            if (load + demand > problem.vehicleCapacity) {
                totalDistance += problem.distanceMatrix[prev][depotId];
                System.out.print("Depot → ");
                load = 0.0;
                prev = depotId;
            }

            totalDistance += problem.distanceMatrix[prev][cityId];
            load += demand;
            System.out.print(cityId + " → ");
            prev = cityId;
        }

        totalDistance += problem.distanceMatrix[prev][depotId];
        System.out.print("Depot\n");

        System.out.printf("Total Distance (Fitness): %.2f\n", totalDistance);
    }
}