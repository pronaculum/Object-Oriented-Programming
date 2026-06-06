package GeoDL;

/**
 * 기하 객체 생성을 담당하는 클래스입니다.
 * GeoManager가 객체 생성 로직을 직접 가지지 않도록 분리했습니다.
 */
public class ObjectivityFactory {
    private static final double EPSILON = 0.000001;

    private ObjectivityFactory() {
    }

    public static Dot createDot(double x, double y) {
        return new Dot(x, y);
    }

    public static Segment createSegment(Dot start, Dot end) {
        if (start == null || end == null) {
            return null;
        }

        if (isSamePosition(start, end)) {
            return null;
        }

        return new Segment(start, end);
    }

    public static Line createLine(Dot point1, Dot point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        if (isSamePosition(point1, point2)) {
            return null;
        }

        return new Line(point1, point2);
    }

    public static Polyline createPolyline(Dot[] dots, int dotCount) {
        if (!isValidDotArray(dots, dotCount, 2)) {
            return null;
        }

        return new Polyline(dots, dotCount);
    }

    public static Polygon createPolygon(Dot[] dots, int dotCount) {
        if (!isValidDotArray(dots, dotCount, 3)) {
            return null;
        }

        return new Polygon(dots, dotCount);
    }

    private static boolean isValidDotArray(Dot[] dots, int dotCount, int minCount) {
        if (dots == null) {
            return false;
        }

        if (dotCount < minCount || dotCount > dots.length) {
            return false;
        }

        for (int i = 0; i < dotCount; i++) {
            if (dots[i] == null) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSamePosition(Dot dot1, Dot dot2) {
        double dx = Math.abs(dot1.getX() - dot2.getX());
        double dy = Math.abs(dot1.getY() - dot2.getY());

        return dx <= EPSILON && dy <= EPSILON;
    }
}
