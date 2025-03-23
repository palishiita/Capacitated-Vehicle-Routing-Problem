package src.genetic;

import src.cvrp.*;
import java.util.*;

public class GeneticAlgorithm {
    private int popSize;
    private int generations;
    private double crossoverRate; // Px
    private double mutationRate;  // Pm
    private int tournamentSize;
    private CVRP problem;
    private List<Individual> population;
    private Random rand = new Random();

    public GeneticAlgorithm(CVRP problem, int popSize, int generations, double crossoverRate, double mutationRate, int tournamentSize) {
        this.problem = problem;
        this.popSize = popSize;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.population = new ArrayList<>();
    }

    public Individual run() {
        initializePopulation();

        Individual bestOverall = getBestIndividual();

        for (int gen = 0; gen < generations; gen++) {
            List<Individual> newPopulation = new ArrayList<>();

            while (newPopulation.size() < popSize) {
                Individual parent1 = tournamentSelection();
                Individual parent2 = tournamentSelection();

                Individual child;
                if (rand.nextDouble() < crossoverRate) {
                    List<Integer> childRoute = Crossover.orderedCrossover(parent1.route, parent2.route);
                    child = new Individual(problem);
                    child.route = childRoute;
                    child.evaluateFitness();
                } else {
                    child = new Individual(parent1); // Copy
                }

                if (rand.nextDouble() < mutationRate) {
                    if (rand.nextBoolean()) {
                        Mutation.swap(child.route);
                    } else {
                        Mutation.inversion(child.route);
                    }
                    child.evaluateFitness();
                }

                newPopulation.add(child);

                // Update best if needed
                if (child.fitness < bestOverall.fitness) {
                    bestOverall = new Individual(child);
                }
            }

            population = newPopulation;

            // Logging (console)
            System.out.printf("Gen %d: Best Fitness = %.2f\n", gen + 1, bestOverall.fitness);
        }

        return bestOverall;
    }

    private void initializePopulation() {
        for (int i = 0; i < popSize; i++) {
            population.add(new Individual(problem));
        }
    }

    private Individual tournamentSelection() {
        List<Individual> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int index = rand.nextInt(popSize);
            tournament.add(population.get(index));
        }
        return Collections.min(tournament, Comparator.comparingDouble(ind -> ind.fitness));
    }

    private Individual getBestIndividual() {
        return Collections.min(population, Comparator.comparingDouble(ind -> ind.fitness));
    }
}
