package mycode;

public class PointData {

    private double x;
    private double y;
    private double time;

    public PointData(double x, double y, double time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "PointData{x=" + x + ", y=" + y + ", time=" + time + "}";
    }
}