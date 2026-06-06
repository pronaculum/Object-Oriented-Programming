package Func1;

import GeoDL.Objectivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 방향을 가진 객체들이 서로 평행하도록 관리하는 제약조건입니다.
 */
public class ConstraintTypeParallel implements Constraint {
    private ArrayList<Objectivity> lines;

    private static final double EPSILON = 0.000001;

    public ConstraintTypeParallel(List<Objectivity> lines) {
        this.lines = new ArrayList<Objectivity>();

        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                this.lines.add(lines.get(i));
            }
        }
    }

    public boolean isSatisfied() {
        if (lines.size() < 2) {
            return false;
        }

        Objectivity base = lines.get(0);

        if (!base.hasDirection()) {
            return false;
        }

        double baseAngle = normalizeAngle(base.getAngle());

        for (int i = 1; i < lines.size(); i++) {
            Objectivity current = lines.get(i);

            if (!current.hasDirection()) {
                return false;
            }

            double currentAngle = normalizeAngle(current.getAngle());
            double difference = Math.abs(baseAngle - currentAngle) % 180.0;

            if (difference > EPSILON && Math.abs(difference - 180.0) > EPSILON) {
                return false;
            }
        }

        return true;
    }

    public void resolve() {
        if (lines.size() < 2) {
            return;
        }

        Objectivity base = lines.get(0);

        if (!base.hasDirection()) {
            return;
        }

        double baseAngle = base.getAngle();

        for (int i = 1; i < lines.size(); i++) {
            Objectivity current = lines.get(i);

            if (current.hasDirection()) {
                current.rotateToAngle(baseAngle);
            }
        }
    }

    public List<Objectivity> getConstrainedObjects() {
        return new ArrayList<Objectivity>(lines);
    }

    public String getConstraintType() {
        return "Parallel";
    }

    private double normalizeAngle(double angle) {
        double result = angle % 180.0;

        if (result < 0) {
            result += 180.0;
        }

        return result;
    }
}
