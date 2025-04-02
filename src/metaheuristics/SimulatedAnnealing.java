package metaheuristics;

import cvrp.*;
import genetic.*;

import java.util.*;

public class SimulatedAnnealing {
    private CVRP problem;
    private double initialTemp;
    private double finalTemp;
    private double coolingRate;
    private int iterationsPerTemp;

    public SimulatedAnnealing(CVRP problem, double initialTemp, double finalTemp, double coolingRate, int iterationsPerTemp) {
        this.problem = problem;
        this.initialTemp = initialTemp;
        this.finalTemp = finalTemp;
        this.coolingRate = coolingRate;
        this.iterationsPerTemp = iterationsPerTemp;
    }

    public Individual run() {
        Individual current = new Individual(problem);
        current.randomizeRoute();              // ✅ Fix: ensure route is initialized
        current.evaluateFitness();

        Individual best = new Individual(current);
        Random rand = new Random();
        double temp = initialTemp;

        while (temp > finalTemp) {
            for (int i = 0; i < iterationsPerTemp; i++) {
                Individual neighbor = generateNeighbor(current);
                double delta = neighbor.fitness - current.fitness;

                if (delta < 0 || Math.exp(-delta / temp) > rand.nextDouble()) {
                    current = neighbor;
                    if (current.fitness < best.fitness) {
                        best = new Individual(current);
                    }
                }
            }
            temp *= coolingRate;
        }

        return best;
    }

    // Simple neighbor: swap two cities
    private Individual generateNeighbor(Individual ind) {
        Individual neighbor = new Individual(ind);
        Random rand = new Random();

        if (neighbor.route.size() < 2) return neighbor; // 🔒 Prevent crash on short routes

        int i = rand.nextInt(neighbor.route.size());
        int j = rand.nextInt(neighbor.route.size());
        Collections.swap(neighbor.route, i, j);

        neighbor.evaluateFitness();
        return neighbor;
    }
}