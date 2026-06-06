package Func1;

import GeoDL.Objectivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 제약조건 객체 생성을 전담하는 클래스입니다.
 */
public class ConstraintFactory {
    private ConstraintFactory() {
    }

    public static Constraint createDistanceConstraint(Objectivity object1, Objectivity object2, double targetDistance) {
        if (object1 == null || object2 == null || targetDistance < 0) {
            return null;
        }

        return new ConstraintTypeDistance(object1, object2, targetDistance);
    }

    public static Constraint createParallelConstraint(List<Objectivity> lines) {
        if (lines == null || lines.size() < 2) {
            return null;
        }

        for (int i = 0; i < lines.size(); i++) {
            Objectivity object = lines.get(i);

            if (object == null || !object.hasDirection()) {
                return null;
            }
        }

        return new ConstraintTypeParallel(lines);
    }

    public static Constraint createParallelConstraint(Objectivity object1, Objectivity object2) {
        if (object1 == null || object2 == null) {
            return null;
        }

        ArrayList<Objectivity> lines = new ArrayList<Objectivity>();
        lines.add(object1);
        lines.add(object2);

        return createParallelConstraint(lines);
    }

    public static Constraint createPerpendicularConstraint(Objectivity object1, Objectivity object2) {
        if (object1 == null || object2 == null) {
            return null;
        }

        if (!object1.hasDirection() || !object2.hasDirection()) {
            return null;
        }

        return new ConstraintTypePerpendicular(object1, object2);
    }
}
