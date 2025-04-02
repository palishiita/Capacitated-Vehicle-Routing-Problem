package cvrp;

public class Location {
    public int id;
    public double x;
    public double y;

    public Location(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Location other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}