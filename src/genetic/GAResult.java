package genetic;

public class GAResult {
    public double best;
    public double worst;
    public double average;
    public double stdDev;
    public long timeMillis;
    public int evaluations;

    public GAResult(double best, double worst, double average, double stdDev, long timeMillis, int evaluations) {
        this.best = best;
        this.worst = worst;
        this.average = average;
        this.stdDev = stdDev;
        this.timeMillis = timeMillis;
        this.evaluations = evaluations;
    }

    public String toCSV() {
        return String.format("%.2f,%.2f,%.2f,%.2f,%.3f,%d",
                best, worst, average, stdDev, timeMillis / 1000.0, evaluations);
    }

    public static String csvHeader() {
        return "Best,Worst,Average,StdDev,Time(s),Evaluations";
    }
}
