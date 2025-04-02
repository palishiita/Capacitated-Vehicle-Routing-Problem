package genetic;

import cvrp.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GARunner {

    public static GAResult run(CVRP problem, GAConfig config, int runs) {
        List<Double> fitnesses = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(
                    problem,
                    config.popSize,
                    config.generations,
                    config.Px,
                    config.Pm,
                    config.tournamentSize,
                    config.selectionType,
                    config.crossoverType,
                    config.mutationType,
                    config.elitismCount
            );

            Individual best = ga.run();
            fitnesses.add(best.fitness);
        }

        long end = System.currentTimeMillis();

        double best = Collections.min(fitnesses);
        double worst = Collections.max(fitnesses);
        double avg = fitnesses.stream().mapToDouble(f -> f).average().orElse(0.0);
        double std = Math.sqrt(fitnesses.stream()
                .mapToDouble(f -> (f - avg) * (f - avg))
                .average().orElse(0.0));

        int evaluations = config.popSize * config.generations * runs;

        return new GAResult(best, worst, avg, std, end - start, evaluations);
    }
}
