package utils;

import genetic.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Logger {

    // Writes header for GA-style CSV
    public static void writeHeader(String path, String additionalHeaders) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("Config," + additionalHeaders + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes GA config + result to CSV
    public static void writeToCSV(String path, GAConfig config, GAResult result) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(config.toShortString() + "," + result.toCSV() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fitness per generation log
    public static void writeFitnessLog(String path, List<Double> best, List<Double> avg, List<Double> worst) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("Generation,Best,Average,Worst\n");
            for (int i = 0; i < best.size(); i++) {
                fw.write(String.format(Locale.US, "%d,%.2f,%.2f,%.2f\n",
                        i, best.get(i), avg.get(i), worst.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New: Write header for simple algorithm results
    public static void writeSimpleHeader(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("Best,Worst,Average,StdDev,Time(s),Evaluations\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write result row for greedy/random/SA
    public static void writeSimpleResult(String path, double best, double worst, double avg, double std, long timeMillis, int evaluations) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(String.format(Locale.US, "%.2f,%.2f,%.2f,%.2f,%.3f,%d\n",
                    best, worst, avg, std, timeMillis / 1000.0, evaluations));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
