package src;

import src.cvrp.CVRP;
import src.genetic.GeneticAlgorithm;
import src.genetic.Individual;
import src.greedy.GreedySearch;
import src.random.RandomSearch;
import src.metaheuristics.SimulatedAnnealing;
import src.utils.Logger;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Generate and save problem instance (optional)
        // generateAndSaveCVRP(10, 100, 10, 50, "cvrp_instance.txt");

        int numCities = 10, gridSize = 100, maxDemand = 10, vehicleCapacity = 50;
        int popSize = 100, generations = 100, tournamentSize = 5, runs = 10;
        double Px = 0.7, Pm = 0.1;

        // Run and log GA
        batchRunGA(runs, numCities, gridSize, maxDemand, vehicleCapacity,
                popSize, generations, Px, Pm, tournamentSize, "ga_results.csv");

        // Run and log Random Search
        batchRunRandom(runs, numCities, gridSize, maxDemand, vehicleCapacity,
                10000, "random_results.csv");

        // Run and log Greedy Search
        batchRunGreedy(runs, numCities, gridSize, maxDemand, vehicleCapacity,
                "greedy_results.csv");

        // Run and log Simulated Annealing
        batchRunSA(runs, numCities, gridSize, maxDemand, vehicleCapacity,
                1000.0, 0.01, 0.95, 100, "sa_results.csv");
    }

    public static void generateAndSaveCVRP(int numCities, int gridSize, int maxDemand, int vehicleCapacity, String filename) {
        CVRP instance = new CVRP(numCities, gridSize, maxDemand, vehicleCapacity);
        instance.printInstance();
        instance.saveToTXT(filename);
    }

    // Genetic Algorithm batch run
    public static void batchRunGA(int runs, int numCities, int gridSize, int maxDemand, int vehicleCapacity,
                                  int popSize, int generations, double Px, double Pm, int tournamentSize,
                                  String outputFilename) {

        List<Double> fitnessList = new ArrayList<>();
        double bestOverallFitness = Double.MAX_VALUE;
        Individual bestOverallIndividual = null;

        for (int i = 1; i <= runs; i++) {
            CVRP instance = new CVRP(numCities, gridSize, maxDemand, vehicleCapacity);
            GeneticAlgorithm ga = new GeneticAlgorithm(instance, popSize, generations, Px, Pm, tournamentSize);
            Individual best = ga.run();
            fitnessList.add(best.fitness);

            if (best.fitness < bestOverallFitness) {
                bestOverallFitness = best.fitness;
                bestOverallIndividual = best;
            }
            System.out.printf("GA Run %d: Fitness = %.2f\n", i, best.fitness);
        }

        logSummary("GA", fitnessList, bestOverallFitness, bestOverallIndividual, outputFilename);
    }

    // Random Search batch run
    public static void batchRunRandom(int runs, int numCities, int gridSize, int maxDemand, int vehicleCapacity,
                                      int evaluationsPerRun, String outputFilename) {

        List<Double> fitnessList = new ArrayList<>();
        double bestOverallFitness = Double.MAX_VALUE;
        Individual bestOverallIndividual = null;

        for (int i = 1; i <= runs; i++) {
            CVRP instance = new CVRP(numCities, gridSize, maxDemand, vehicleCapacity);
            RandomSearch randomSearch = new RandomSearch(instance, evaluationsPerRun);
            Individual best = randomSearch.run();
            fitnessList.add(best.fitness);

            if (best.fitness < bestOverallFitness) {
                bestOverallFitness = best.fitness;
                bestOverallIndividual = best;
            }
            System.out.printf("Random Run %d: Fitness = %.2f\n", i, best.fitness);
        }

        logSummary("Random Search", fitnessList, bestOverallFitness, bestOverallIndividual, outputFilename);
    }

    // Greedy Search batch run
    public static void batchRunGreedy(int runs, int numCities, int gridSize, int maxDemand, int vehicleCapacity,
                                      String outputFilename) {

        List<Double> fitnessList = new ArrayList<>();
        double bestOverallFitness = Double.MAX_VALUE;
        Individual bestOverallIndividual = null;

        for (int i = 1; i <= runs; i++) {
            CVRP instance = new CVRP(numCities, gridSize, maxDemand, vehicleCapacity);
            GreedySearch greedySearch = new GreedySearch(instance);
            Individual best = greedySearch.run();
            fitnessList.add(best.fitness);

            if (best.fitness < bestOverallFitness) {
                bestOverallFitness = best.fitness;
                bestOverallIndividual = best;
            }
            System.out.printf("Greedy Run %d: Fitness = %.2f\n", i, best.fitness);
        }

        logSummary("Greedy Search", fitnessList, bestOverallFitness, bestOverallIndividual, outputFilename);
    }

    // Simulated Annealing batch run
    public static void batchRunSA(int runs, int numCities, int gridSize, int maxDemand, int vehicleCapacity,
                                  double initialTemp, double finalTemp, double coolingRate, int iterationsPerTemp,
                                  String outputFilename) {

        List<Double> fitnessList = new ArrayList<>();
        double bestOverallFitness = Double.MAX_VALUE;
        Individual bestOverallIndividual = null;

        for (int i = 1; i <= runs; i++) {
            CVRP instance = new CVRP(numCities, gridSize, maxDemand, vehicleCapacity);
            SimulatedAnnealing sa = new SimulatedAnnealing(instance, initialTemp, finalTemp, coolingRate, iterationsPerTemp);
            Individual best = sa.run();
            fitnessList.add(best.fitness);

            if (best.fitness < bestOverallFitness) {
                bestOverallFitness = best.fitness;
                bestOverallIndividual = best;
            }
            System.out.printf("SA Run %d: Fitness = %.2f\n", i, best.fitness);
        }

        logSummary("Simulated Annealing", fitnessList, bestOverallFitness, bestOverallIndividual, outputFilename);
    }

    // Logging summary for all algorithms
    private static void logSummary(String algoName, List<Double> fitnessList, double bestFitness,
                                   Individual bestIndividual, String filename) {

        double sum = 0.0;
        for (double f : fitnessList) sum += f;
        double avg = sum / fitnessList.size();

        double variance = 0.0;
        for (double f : fitnessList) variance += Math.pow(f - avg, 2);
        double stdDev = Math.sqrt(variance / fitnessList.size());
        double worst = Collections.max(fitnessList);

        System.out.println("\n===== " + algoName + " Summary =====");
        System.out.printf("Best Fitness: %.2f\n", bestFitness);
        System.out.printf("Worst Fitness: %.2f\n", worst);
        System.out.printf("Average Fitness: %.2f\n", avg);
        System.out.printf("Std Deviation: %.2f\n", stdDev);
        System.out.println("Best Route:");
        bestIndividual.printRoute();
        bestIndividual.printFullRouteWithDepot();

        Logger.saveRunResultsToCSV(filename, fitnessList, avg, bestFitness, worst, stdDev);
    }
}