package mycode;

import java.util.ArrayList;
import java.util.List;

public class PointPredictObject {
    private int id;
    private List<PointData> points;

    public PointPredictObject(int id) {
        this.id = id;
        this.points = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<PointData> getPoints() {
        return points;
    }

    public void addPoint(double x, double y, double time) {
        points.add(new PointData(x, y, time));
    }
}