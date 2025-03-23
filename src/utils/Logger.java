package src.utils;

import java.io.*;
import java.util.List;

public class Logger {

    public static void saveRunResultsToCSV(String filename, List<Double> fitnessList, double avg, double best, double worst, double stdDev) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Run,BestFitness\n");
            for (int i = 0; i < fitnessList.size(); i++) {
                writer.write((i + 1) + "," + String.format("%.2f", fitnessList.get(i)) + "\n");
            }

            writer.newLine();
            writer.write("Summary\n");
            writer.write("AverageFitness," + String.format("%.2f", avg) + "\n");
            writer.write("BestOverall," + String.format("%.2f", best) + "\n");
            writer.write("WorstOverall," + String.format("%.2f", worst) + "\n");
            writer.write("StdDeviation," + String.format("%.2f", stdDev) + "\n");

            writer.flush();
            System.out.println("Saved CSV results to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}
