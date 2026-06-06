package Func1;

import GeoDL.Objectivity;
import java.util.Arrays;
import java.util.List;

/**
 * 두 객체의 중심점 사이 거리를 targetDistance로 유지하는 제약조건입니다.
 */
public class ConstraintTypeDistance implements Constraint {
    private final Objectivity object1;
    private final Objectivity object2;
    private double targetDistance;

    private static final double EPSILON = 0.000001;

    public ConstraintTypeDistance(Objectivity object1, Objectivity object2, double targetDistance) {
        this.object1 = object1;
        this.object2 = object2;
        this.targetDistance = targetDistance;
    }

    public boolean isSatisfied() {
        double currentDistance = calculateDistance(object1, object2);
        return Math.abs(currentDistance - targetDistance) <= EPSILON;
    }

    public void resolve() {
        if (isSatisfied()) {
            return;
        }

        adjustDistanceToTarget();
    }

    public List<Objectivity> getConstrainedObjects() {
        return Arrays.asList(object1, object2);
    }

    public String getConstraintType() {
        return "Distance";
    }

    public Objectivity getObject1() {
        return object1;
    }

    public Objectivity getObject2() {
        return object2;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        if (targetDistance >= 0) {
            this.targetDistance = targetDistance;
        }
    }

    private double calculateDistance(Objectivity o1, Objectivity o2) {
        double dx = o2.getCenterX() - o1.getCenterX();
        double dy = o2.getCenterY() - o1.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void adjustDistanceToTarget() {
        double x1 = object1.getCenterX();
        double y1 = object1.getCenterY();
        double x2 = object2.getCenterX();
        double y2 = object2.getCenterY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        if (currentDistance <= EPSILON) {
            object2.moveBy(targetDistance, 0.0);
            return;
        }

        double unitX = dx / currentDistance;
        double unitY = dy / currentDistance;

        double targetX = x1 + unitX * targetDistance;
        double targetY = y1 + unitY * targetDistance;

        object2.moveBy(targetX - x2, targetY - y2);
    }
}
