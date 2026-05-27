package mycode;

import java.util.List;

public class PredictionConstraint {
    private int minimumPoints;

    public PredictionConstraint(int minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    public boolean validate(List<PointData> points) {

        if (points == null || points.size() < minimumPoints) {
            return false;
        }

        for (int i = 1; i < points.size(); i++) {

            if (points.get(i).getTime() <= points.get(i - 1).getTime()) {
                return false;
            }
        }

        return true;
    }
}