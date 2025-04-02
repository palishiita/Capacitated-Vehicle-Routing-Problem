package experiments;

import utils.*;
import cvrp.*;
import genetic.*;

import java.util.ArrayList;
import java.util.List;

public class GAParameterTuner {

    public static void main(String[] args) {
        // Load the CVRP instance
        CVRP problem = new CVRP();
        problem.loadFromVRPFile("C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/instances/basic/A-n37-k6.vrp");

        // Output CSV path
        String outputCSV = "C:/Users/ishii/Desktop/Capacitated-Vehicle-Routing-Problem/src/results/parameter_tuning_A-n37-k6.csv";
        Logger.writeHeader(outputCSV, GAResult.csvHeader());

        // Parameter grids
        int[] popSizes = {800, 1000, 2000, 5000};
        int[] generations = {800};
        double[] crossoverRates = {0.7};
        double[] mutationRates = {0.05};
        int[] tournamentSizes = {7};
        int[] elitismCounts = {2}; // Elitism count(s) to test

        SelectionType[] selectionTypes = SelectionType.values();

        CrossoverType[] crossoverTypes = {
            CrossoverType.OX,
            // CrossoverType.CX,
            // CrossoverType.PMX 
        };

        MutationType[] mutationTypes = MutationType.values();
        boolean[] elitismOptions = {true};

        // Build configuration list
        List<GAConfig> configsToTest = new ArrayList<>();

        for (SelectionType sel : selectionTypes) {
            for (CrossoverType cross : crossoverTypes) {
                for (MutationType mut : mutationTypes) {
                    for (boolean elitism : elitismOptions) {
                        for (int elitismCount : elitismCounts) {
                            for (int pop : popSizes) {
                                for (int gen : generations) {
                                    for (double px : crossoverRates) {
                                        for (double pm : mutationRates) {
                                            for (int tour : tournamentSizes) {
                                                GAConfig config = new GAConfig(
                                                        pop, gen, px, pm, tour,
                                                        sel, cross, mut, elitismCount 
                                                );
                                                configsToTest.add(config);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Run all configurations (10 runs each)
        for (GAConfig config : configsToTest) {
            System.out.println("Running: " + config.toShortString());

            try {
                long start = System.currentTimeMillis();
                GAResult result = GARunner.run(problem, config, 10);
                long end = System.currentTimeMillis();

                Logger.writeToCSV(outputCSV, config, result);
                System.out.printf("Completed in %.2fs\n", (end - start) / 1000.0);

            } catch (Exception e) {
                System.out.println("Error during config: " + config.toShortString());
                e.printStackTrace();
            }
        }

        System.out.println("All parameter combinations tested!");
        System.out.println("Results saved to: " + outputCSV);
    }
}
