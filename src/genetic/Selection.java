package genetic;

import java.util.*;

public class Selection {
    
    public static Individual select(List<Individual> population, SelectionType type, int tournamentSize) {
        if (type == SelectionType.TOURNAMENT) {
            return tournamentSelection(population, tournamentSize);
        } else {
            return rouletteSelection(population);
        }
    }

    private static Individual tournamentSelection(List<Individual> population, int k) {
        Random rand = new Random();
        List<Individual> tournament = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            tournament.add(population.get(rand.nextInt(population.size())));
        }
        return Collections.min(tournament, Comparator.comparingDouble(i -> i.fitness));
    }

    private static Individual rouletteSelection(List<Individual> population) {
        double totalFitness = 0.0;
        for (Individual ind : population) {
            totalFitness += 1.0 / ind.fitness; // invert for minimization
        }

        double rand = Math.random() * totalFitness;
        double cumulative = 0.0;

        for (Individual ind : population) {
            cumulative += 1.0 / ind.fitness;
            if (cumulative >= rand) {
                return ind;
            }
        }
        return population.get(population.size() - 1); // fallback
    }
}
