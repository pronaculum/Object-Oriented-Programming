package mycode;

import java.util.List;

public class LinearRegressionPredictor {

    public PointData predictNext(List<PointData> points) {

        PointData last = points.get(points.size() - 1);
        PointData prev = points.get(points.size() - 2);

        double dx = last.getX() - prev.getX();
        double dy = last.getY() - prev.getY();
        double dt = last.getTime() - prev.getTime();

        return new PointData(
                last.getX() + dx,
                last.getY() + dy,
                last.getTime() + dt
        );
    }

    public PointData predictPrevious(List<PointData> points) {

        PointData first = points.get(0);
        PointData second = points.get(1);

        double dx = second.getX() - first.getX();
        double dy = second.getY() - first.getY();
        double dt = second.getTime() - first.getTime();

        return new PointData(
                first.getX() - dx,
                first.getY() - dy,
                first.getTime() - dt
        );
    }
}