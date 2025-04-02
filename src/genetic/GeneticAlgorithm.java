package genetic;

import cvrp.CVRP;
import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm {
    private CVRP problem;
    private int popSize;
    private int generations;
    private double Px;
    private double Pm;
    private int tournamentSize;
    private int elitismCount;

    private SelectionType selectionType;
    private CrossoverType crossoverType;
    private MutationType mutationType;

    private List<Individual> population;

    public GeneticAlgorithm(CVRP problem, int popSize, int generations,
                            double Px, double Pm, int tournamentSize,
                            SelectionType selectionType, CrossoverType crossoverType,
                            MutationType mutationType, int elitismCount) {
        this.problem = problem;
        this.popSize = popSize;
        this.generations = generations;
        this.Px = Px;
        this.Pm = Pm;
        this.tournamentSize = tournamentSize;
        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.mutationType = mutationType;
        this.elitismCount = elitismCount;
        this.population = new ArrayList<>();
    }

    public Individual run() {
        initializePopulation();
        Individual globalBest = Collections.min(population, Comparator.comparingDouble(i -> i.fitness));

        for (int gen = 0; gen < generations; gen++) {
            List<Individual> newPopulation = new ArrayList<>();

            // Always apply elitism
            List<Individual> elites = population.stream()
                    .sorted(Comparator.comparingDouble(i -> i.fitness))
                    .limit(elitismCount)
                    .map(Individual::new)
                    .collect(Collectors.toList());

            while (newPopulation.size() < popSize - elites.size()) {
                Individual parent1 = Selection.select(population, selectionType, tournamentSize);
                Individual parent2 = Selection.select(population, selectionType, tournamentSize);

                List<Integer> childRoute;
                if (Math.random() < Px) {
                    childRoute = Crossover.apply(crossoverType, parent1.route, parent2.route);
                } else {
                    childRoute = new ArrayList<>(parent1.route);
                }

                Individual child = new Individual(problem);
                child.route = childRoute;

                if (Math.random() < Pm) {
                    Mutation.apply(mutationType, child.route);
                }

                if (Math.random() < 0.05) {
                    child = LocalSearch.optimize(child); // 5% chance
                }                
                                
                child.evaluateFitness();
                newPopulation.add(child);
            }

            // Add elites
            newPopulation.addAll(elites);
            population = newPopulation;

            Individual currentBest = Collections.min(population, Comparator.comparingDouble(i -> i.fitness));
            if (currentBest.fitness < globalBest.fitness) {
                globalBest = new Individual(currentBest);
            }
        }

        return globalBest;
    }

    private void initializePopulation() {
        for (int i = 0; i < popSize; i++) {
            Individual ind = new Individual(problem);
            ind.randomizeRoute();
            ind.evaluateFitness();
            population.add(ind);
        }
    }
}