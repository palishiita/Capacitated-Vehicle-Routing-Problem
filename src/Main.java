import cvrp.*;
import metaheuristics.*;
import greedy.*;
import random.*;
import genetic.*;

import utils.*;

import java.io.File;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Input folders for instances
        String[] folders = {
            "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/instances/basic",
            "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/instances/hard"
        };

        // Output folders per algorithm
        String greedyOutput = "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/greedy/";
        String randomOutput = "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/random/";
        String saOutput = "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/metaheuristics/";

        // Create output folders if they don't exist
        new File(greedyOutput).mkdirs();
        new File(randomOutput).mkdirs();
        new File(saOutput).mkdirs();

        // Loop over all instances
        for (String folderPath : folders) {
            File folder = new File(folderPath);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".vrp"));
            if (files == null) continue;

            for (File file : files) {
                String instanceName = file.getName().replace(".vrp", "");
                CVRP problem = new CVRP();
                problem.loadFromVRPFile(file.getAbsolutePath());

                // --- Greedy Search ---
                long start = System.currentTimeMillis();
                Individual greedy = new GreedySearch(problem).run();
                long end = System.currentTimeMillis();
                String greedyPath = greedyOutput + instanceName + ".csv";
                Logger.writeSimpleHeader(greedyPath);
                Logger.writeSimpleResult(greedyPath, greedy.fitness, greedy.fitness, greedy.fitness, 0.0, end - start, 1);

                // --- Random Search (10 runs) ---
                double[] rsFitness = new double[10];
                long rsStart = System.currentTimeMillis();
                for (int i = 0; i < 10; i++) {
                    Individual rand = new RandomSearch(problem, 10000).run();
                    rsFitness[i] = rand.fitness;
                }
                long rsEnd = System.currentTimeMillis();
                String randomPath = randomOutput + instanceName + ".csv";
                Logger.writeSimpleHeader(randomPath);
                Logger.writeSimpleResult(randomPath, min(rsFitness), max(rsFitness), avg(rsFitness), std(rsFitness), rsEnd - rsStart, 10 * 10000);

                // --- Simulated Annealing (10 runs) ---
                double[] saFitness = new double[10];
                long saStart = System.currentTimeMillis();
                for (int i = 0; i < 10; i++) {
                    Individual sa = new SimulatedAnnealing(problem, 1000, 0.01, 0.995, 50).run();
                    saFitness[i] = sa.fitness;
                }
                long saEnd = System.currentTimeMillis();
                String saPath = saOutput + instanceName + ".csv";
                Logger.writeSimpleHeader(saPath);
                Logger.writeSimpleResult(saPath, min(saFitness), max(saFitness), avg(saFitness), std(saFitness), saEnd - saStart, 10 * 1000);
            }
        }

        System.out.println("🎉 All metaheuristic comparisons completed!");
    }

    private static double min(double[] arr) {
        return Arrays.stream(arr).min().orElse(Double.MAX_VALUE);
    }

    private static double max(double[] arr) {
        return Arrays.stream(arr).max().orElse(Double.MIN_VALUE);
    }

    private static double avg(double[] arr) {
        return Arrays.stream(arr).average().orElse(0.0);
    }

    private static double std(double[] arr) {
        double mean = avg(arr);
        return Math.sqrt(Arrays.stream(arr).map(a -> (a - mean) * (a - mean)).average().orElse(0.0));
    }
}

