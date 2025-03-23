package src.cvrp;

import java.util.*;
import java.io.*;

public class CVRP {
    public Location depot;
    public List<Location> cities;
    public Map<Integer, Integer> demands;
    public double[][] distanceMatrix;
    public int vehicleCapacity;

    public CVRP(int numCities, int gridSize, int maxDemand, int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
        this.depot = new Location(0, Math.random() * gridSize, Math.random() * gridSize);
        this.cities = new ArrayList<>();
        this.demands = new HashMap<>();

        generateRandomCities(numCities, gridSize, maxDemand);
        computeDistanceMatrix();
    }

    private void generateRandomCities(int numCities, int gridSize, int maxDemand) {
        for (int i = 1; i <= numCities; i++) {
            double x = Math.random() * gridSize;
            double y = Math.random() * gridSize;
            Location city = new Location(i, x, y);
            cities.add(city);
            int demand = 1 + (int) (Math.random() * maxDemand);
            demands.put(i, demand);
        }
    }

    private void computeDistanceMatrix() {
        int total = cities.size() + 1; // Including depot
        distanceMatrix = new double[total][total];

        List<Location> allLocations = new ArrayList<>();
        allLocations.add(depot);
        allLocations.addAll(cities);

        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                distanceMatrix[i][j] = allLocations.get(i).distanceTo(allLocations.get(j));
            }
        }
    }

    public void printInstance() {
        System.out.printf("Depot: x=%.2f y=%.2f\n", depot.x, depot.y);
        for (Location city : cities) {
            int demand = demands.get(city.id);
            System.out.printf("City %d: x=%.2f y=%.2f demand=%d\n", city.id, city.x, city.y, demand);
        }
        System.out.println("Vehicle Capacity: " + vehicleCapacity);
    }

    public void saveToTXT(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.format("Depot: x=%.2f y=%.2f\n", depot.x, depot.y));
            writer.newLine();

            for (Location city : cities) {
                int demand = demands.get(city.id);
                writer.write(String.format("City %d: x=%.2f y=%.2f demand=%d\n", city.id, city.x, city.y, demand));
            }

            writer.newLine();
            writer.write("Vehicle Capacity: " + vehicleCapacity + "\n");

            writer.flush();
            System.out.println("Saved instance to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving instance: " + e.getMessage());
        }
    }
}