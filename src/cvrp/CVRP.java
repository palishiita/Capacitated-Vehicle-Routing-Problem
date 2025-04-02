package cvrp;

import java.io.*;
import java.util.*;

public class CVRP {
    public Location depot;
    public List<Location> cities;
    public Map<Integer, Integer> demands;
    public double[][] distanceMatrix;
    public int vehicleCapacity;

    public CVRP() {
        this.cities = new ArrayList<>();
        this.demands = new HashMap<>();
    }

    public void loadFromVRPFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean readingNodes = false, readingDemands = false;
            Map<Integer, Location> tempLocations = new HashMap<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("CAPACITY")) {
                    vehicleCapacity = Integer.parseInt(line.split(":")[1].trim());

                } else if (line.startsWith("NODE_COORD_SECTION")) {
                    readingNodes = true;

                } else if (line.startsWith("DEMAND_SECTION")) {
                    readingNodes = false;
                    readingDemands = true;

                } else if (line.startsWith("DEPOT_SECTION")) {
                    readingDemands = false;

                } else if (line.startsWith("EOF")) {
                    break;

                } else if (readingNodes) {
                    String[] parts = line.split("\\s+");
                    int id = Integer.parseInt(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    tempLocations.put(id, new Location(id, x, y));

                } else if (readingDemands) {
                    String[] parts = line.split("\\s+");
                    int id = Integer.parseInt(parts[0]);
                    int demand = Integer.parseInt(parts[1]);
                    demands.put(id, demand);
                }
            }

            // Assume depot is node 1
            this.depot = tempLocations.get(1);
            this.demands.put(1, 0); // depot demand = 0

            // Add all other cities except depot
            for (Map.Entry<Integer, Location> entry : tempLocations.entrySet()) {
                if (entry.getKey() != 1) {
                    this.cities.add(entry.getValue());
                }
            }

            computeDistanceMatrix();

            System.out.println("Loaded instance: " + filename);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    private void computeDistanceMatrix() {
        int size = cities.size() + 1; // depot + cities
        distanceMatrix = new double[size][size];

        List<Location> all = new ArrayList<>();
        all.add(depot); // depot is index 0
        all.addAll(cities);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distanceMatrix[i][j] = all.get(i).distanceTo(all.get(j));
            }
        }
    }

    public int getMatrixIndex(int cityId) {
        if (cityId == depot.id) return 0;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).id == cityId) return i + 1;
        }
        throw new IllegalArgumentException("City ID not found: " + cityId);
    }

    public double calculateRouteDistance(List<Integer> route) {
        double total = 0.0;
        int prev = depot.id;
    
        for (int cityId : route) {
            total += distanceMatrix[getMatrixIndex(prev)][getMatrixIndex(cityId)];
            prev = cityId;
        }
    
        // Return to depot
        total += distanceMatrix[getMatrixIndex(prev)][getMatrixIndex(depot.id)];
        return total;
    }
    

    public void printSummary() {
        System.out.printf("Depot: x=%.2f y=%.2f\n", depot.x, depot.y);
        for (Location city : cities) {
            int demand = demands.get(city.id);
            System.out.printf("City %d: x=%.2f y=%.2f demand=%d\n", city.id, city.x, city.y, demand);
        }
        System.out.println("Vehicle Capacity: " + vehicleCapacity);
    }
}