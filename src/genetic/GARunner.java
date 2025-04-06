package genetic;

import cvrp.CVRP;
import utils.Logger;

public class GARunner {

    public static GAResult run(CVRP problem, GAConfig config, int runs) {
        double best = Double.MAX_VALUE;
        double worst = Double.MIN_VALUE;
        double total = 0;
        double totalSquared = 0;

        long start = System.currentTimeMillis();

        for (int run = 0; run < runs; run++) {
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

            // Run GA and get result with generation fitness curves
            GeneticAlgorithm.RunResult result = ga.run();
            double fitness = result.best.fitness;

            best = Math.min(best, fitness);
            worst = Math.max(worst, fitness);
            total += fitness;
            totalSquared += fitness * fitness;

            // Save generation-by-generation fitness curve
            String fitnessLogPath = String.format(
                "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/ga_fitness_logs/%s_%dgen_%dpop_EC%d_Run%d.csv",
                config.crossoverType.toString(),
                config.generations,
                config.popSize,
                config.elitismCount,
                run
            );

            Logger.writeFitnessLog(fitnessLogPath, result.bests, result.avgs, result.worsts);
        }

        long end = System.currentTimeMillis();

        double average = total / runs;
        double stdDev = Math.sqrt((totalSquared / runs) - (average * average));

        // ✅ Deterministic evaluation count
        int evaluations = config.popSize * config.generations * runs;

        return new GAResult(best, worst, average, stdDev, end - start, evaluations);
    }
}