package GeoDL;

import Func1.Constraint;
import Func1.ConstraintFactory;
import Func1.ConstraintHandler;

/**
 * GeoDL 라이브러리의 대표 관리체입니다.
 * 객체 생성/삭제와 제약조건 설정/해제를 외부에서 사용할 수 있게 제공합니다.
 */
public class GeoManager {
    private ObjectivityHandler objectHandler;
    private ConstraintHandler constraintHandler;

    public GeoManager() {
        objectHandler = new ObjectivityHandler();
        constraintHandler = new ConstraintHandler();
    }

    public GeoManager(int capacity) {
        objectHandler = new ObjectivityHandler(capacity);
        constraintHandler = new ConstraintHandler();
    }

    // ------------------------------------------------------------
    // 3-1. 점, 선분, 직선, 다각선, 다각형 생성
    // ------------------------------------------------------------

    public Dot createDot(double x, double y) {
        Dot dot = ObjectivityFactory.createDot(x, y);

        if (objectHandler.addObject(dot)) {
            return dot;
        }

        return null;
    }

    public Segment createSegment(Dot start, Dot end) {
        Segment segment = ObjectivityFactory.createSegment(start, end);

        if (objectHandler.addObject(segment)) {
            return segment;
        }

        return null;
    }

    public Line createLine(Dot point1, Dot point2) {
        Line line = ObjectivityFactory.createLine(point1, point2);

        if (objectHandler.addObject(line)) {
            return line;
        }

        return null;
    }

    public Polyline createPolyline(Dot[] dots, int dotCount) {
        Polyline polyline = ObjectivityFactory.createPolyline(dots, dotCount);

        if (objectHandler.addObject(polyline)) {
            return polyline;
        }

        return null;
    }

    public Polygon createPolygon(Dot[] dots, int dotCount) {
        Polygon polygon = ObjectivityFactory.createPolygon(dots, dotCount);

        if (objectHandler.addObject(polygon)) {
            return polygon;
        }

        return null;
    }

    // ------------------------------------------------------------
    // 3-1. 객체 제거
    // ------------------------------------------------------------

    public boolean removeObject(Objectivity object) {
        boolean result = objectHandler.removeObject(object);

        if (result) {
            constraintHandler.cleanUpForDeletedObject(object);
        }

        return result;
    }

    public int getObjectCount() {
        return objectHandler.getObjectCount();
    }

    public Objectivity getObject(int index) {
        return objectHandler.getObject(index);
    }

    public Objectivity findObjectById(int id) {
        return objectHandler.findObjectById(id);
    }

    public Objectivity[] getObjects() {
        return objectHandler.getObjects();
    }

    // ------------------------------------------------------------
    // 3-2. 두 객체 간 제약조건 설정
    // ------------------------------------------------------------

    public int setDistanceConstraint(Objectivity object1, Objectivity object2, double targetDistance) {
        if (!canSetConstraint(object1, object2)) {
            return -1;
        }

        Constraint constraint = ConstraintFactory.createDistanceConstraint(object1, object2, targetDistance);
        return constraintHandler.addConstraint(constraint);
    }

    public int setParallelConstraint(Objectivity object1, Objectivity object2) {
        if (!canSetConstraint(object1, object2)) {
            return -1;
        }

        Constraint constraint = ConstraintFactory.createParallelConstraint(object1, object2);
        return constraintHandler.addConstraint(constraint);
    }

    public int setPerpendicularConstraint(Objectivity object1, Objectivity object2) {
        if (!canSetConstraint(object1, object2)) {
            return -1;
        }

        Constraint constraint = ConstraintFactory.createPerpendicularConstraint(object1, object2);
        return constraintHandler.addConstraint(constraint);
    }

    private boolean canSetConstraint(Objectivity object1, Objectivity object2) {
        if (object1 == null || object2 == null) {
            return false;
        }

        if (!objectHandler.containsObject(object1)) {
            return false;
        }

        if (!objectHandler.containsObject(object2)) {
            return false;
        }

        return true;
    }

    // ------------------------------------------------------------
    // 3-2. 제약조건 해제 및 조회
    // ------------------------------------------------------------

    public boolean removeConstraint(int constraintId) {
        return constraintHandler.removeConstraint(constraintId);
    }

    public int removeConstraintsBetween(Objectivity object1, Objectivity object2) {
        return constraintHandler.removeConstraintsBetween(object1, object2);
    }

    public Constraint getConstraint(int constraintId) {
        return constraintHandler.getConstraint(constraintId);
    }

    public Constraint[] getConstraints() {
        return constraintHandler.getConstraints();
    }

    public int[] getConstraintIds() {
        return constraintHandler.getConstraintIds();
    }

    public int getConstraintCount() {
        return constraintHandler.getConstraintCount();
    }

    public void resolveConstraints() {
        constraintHandler.resolveAll();
    }
}
