package experiments;

import cvrp.*;
import genetic.*;
import utils.*;

import java.io.File;

public class TaguchiTuner {

    public static void main(String[] args) {
        // Input folders for instances
        String[] folders = {
            "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/instances/basic",
            "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/instances/hard"
        };

        String outputFolder = "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/ga_taguchi";

        // Taguchi orthogonal array L16: [PopSize, Generations, Px, Pm, TournamentSize]
        int[][] L16 = {
            {0, 0, 0, 0, 0}, {0, 1, 1, 1, 1}, {0, 2, 2, 2, 2}, {0, 3, 3, 3, 3},
            {1, 0, 1, 2, 3}, {1, 1, 2, 3, 0}, {1, 2, 3, 0, 1}, {1, 3, 0, 1, 2},
            {2, 0, 2, 3, 1}, {2, 1, 3, 0, 2}, {2, 2, 0, 1, 3}, {2, 3, 1, 2, 0},
            {3, 0, 3, 2, 2}, {3, 1, 0, 3, 3}, {3, 2, 1, 0, 0}, {3, 3, 2, 1, 1}
        };

        // Parameter levels
        int[] popSizes = {100, 400, 800, 1000};
        int[] generations = {50, 200, 400, 1000};
        double[] pxRates = {0.7, 0.8, 0.9, 1.0};
        double[] pmRates = {0.01, 0.03, 0.05, 0.1};
        int[] tournamentSizes = {3, 5, 7, 9};
        int[] elitismCounts = {1, 3, 5};

        for (String folderPath : folders) {
            File folder = new File(folderPath);
            File[] files = folder.listFiles((_, name) -> name.endsWith(".vrp"));

            if (files == null || files.length == 0) {
                System.out.println("No .vrp files found in folder: " + folderPath);
                continue;
            }

            for (File file : files) {
                String instanceName = file.getName().replace(".vrp", "");
                String inputPath = file.getAbsolutePath();
                String outputCSV = outputFolder + "/taguchi_" + instanceName + ".csv";

                CVRP problem = new CVRP();
                problem.loadFromVRPFile(inputPath);

                Logger.writeHeader(outputCSV, "Config,Best,Worst,Average,StdDev,Time(s),Evaluations");

                for (int i = 0; i < L16.length; i++) {
                    int[] row = L16[i];

                    int popSize = popSizes[row[0]];
                    int gen = generations[row[1]];
                    double px = pxRates[row[2]];
                    double pm = pmRates[row[3]];
                    int tournamentSize = tournamentSizes[row[4]];

                    for (int elitismCount : elitismCounts) {
                        final int configIndex = i + 1;
                        final String configName = String.format("[%s] Config %d, EC: %d", instanceName, configIndex, elitismCount);

                        GAConfig config = new GAConfig(
                            popSize, gen, px, pm, tournamentSize,
                            SelectionType.TOURNAMENT,
                            CrossoverType.OX,
                            MutationType.SWAP,
                            elitismCount
                        );

                        try {
                            System.out.println("Running " + configName);
                            GAResult result = GARunner.run(problem, config, 10);
                            Logger.writeToCSV(outputCSV, config, result);
                            System.out.println("Done: " + configName);
                        } catch (Exception e) {
                            System.err.println("Error in " + configName);
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Completed all configs for: " + instanceName);
            }
        }

        System.out.println("All Taguchi experiments completed!");
    }
}