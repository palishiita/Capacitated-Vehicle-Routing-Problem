package src.random;

import src.cvrp.*;
import src.genetic.*;

import java.util.*;

public class RandomSearch {
    private CVRP problem;
    private int evaluations;

    public RandomSearch(CVRP problem, int evaluations) {
        this.problem = problem;
        this.evaluations = evaluations;
    }

    public Individual run() {
        Individual best = null;

        for (int i = 0; i < evaluations; i++) {
            Individual candidate = generateRandomIndividual();
            if (best == null || candidate.fitness < best.fitness) {
                best = candidate;
            }
        }

        return best;
    }

    private Individual generateRandomIndividual() {
        List<Integer> cities = new ArrayList<>();
        for (Location city : problem.cities) {
            cities.add(city.id);
        }
        Collections.shuffle(cities);

        Individual individual = new Individual(problem);
        individual.route = cities;
        individual.evaluateFitness();
        return individual;
    }
}

