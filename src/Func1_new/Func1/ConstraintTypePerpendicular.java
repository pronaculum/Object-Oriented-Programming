package Func1;

import GeoDL.Objectivity;
import java.util.Arrays;
import java.util.List;

/**
 * 두 객체가 서로 수직이 되도록 관리하는 제약조건입니다.
 */
public class ConstraintTypePerpendicular implements Constraint {
    private final Objectivity line1;
    private final Objectivity line2;

    private static final double EPSILON = 0.000001;

    public ConstraintTypePerpendicular(Objectivity line1, Objectivity line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public boolean isSatisfied() {
        if (!line1.hasDirection() || !line2.hasDirection()) {
            return false;
        }

        double angle1 = line1.getAngle();
        double angle2 = line2.getAngle();
        double angleDifference = Math.abs(angle1 - angle2) % 180.0;

        return Math.abs(angleDifference - 90.0) <= EPSILON;
    }

    public void resolve() {
        if (isSatisfied()) {
            return;
        }

        if (!line1.hasDirection() || !line2.hasDirection()) {
            return;
        }

        double targetAngleForLine2 = line1.getAngle() + 90.0;
        line2.rotateToAngle(targetAngleForLine2);
    }

    public List<Objectivity> getConstrainedObjects() {
        return Arrays.asList(line1, line2);
    }

    public String getConstraintType() {
        return "Perpendicular";
    }

    public Objectivity getLine1() {
        return line1;
    }

    public Objectivity getLine2() {
        return line2;
    }
}
