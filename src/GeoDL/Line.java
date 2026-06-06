package GeoDL;

/**
 * 모든 선 유형 기하학 관계의 추상 기반 클래스.
 *
 * 도메인 비전 구현:
 *   - "선(Line)은 결코 홀로 존재할 수 없으며, 반드시 점과 점의 유기적인 관계를 맺음으로써
 *     그 의미가 발현된다." — Line은 두 Point의 참조를 GeoList로 보관한다.
 *   - 원점과 방향벡터를 다루는 StraightLine도 "두 점의 관계"로 환원하여 표현한다.
 *
 * pointList 내 인덱스 의미:
 *   - 인덱스 0 → point1: Segment에서는 끝점 A, StraightLine에서는 원점 P
 *   - 인덱스 1 → point2: Segment에서는 끝점 B, StraightLine에서는 방향벡터 끝점 D
 *
 * GeoList 접근 제어:
 *   - getPointList()는 package-private — 동일 패키지의 IntersectionLogic만 직접 접근 가능.
 *   - 외부 계층은 getPoint1(), getPoint2() 공개 메서드를 통해서만 점에 접근.
 *
 * 기존 한글 패키지의 '관계.선'에서 이관 및 영문화.
 */
public abstract class Line extends Relation implements Objectivity {

    private final LineInfo lineInfo;  // 선의 유형 메타데이터 (SEGMENT, STRAIGHT_LINE 등)
    private final GeoList pointList;  // 이 선을 구성하는 두 Point의 참조 목록

    /**
     * 서브클래스(Segment, StraightLine)가 생성자에서 호출한다.
     * 두 Point를 GeoList에 순서대로 등록하여 선의 기하학적 의미를 확립한다.
     */
    protected Line(LineInfo lineInfo, Point point1, Point point2) {
        this.lineInfo  = lineInfo;
        this.pointList = new GeoList();
        GeoList.add(this.pointList, 0, point1); // 인덱스 0: 시작점 또는 원점
        GeoList.add(this.pointList, 1, point2); // 인덱스 1: 끝점 또는 방향벡터 끝점
    }

    // ── Objectivity 계약 구현 ──────────────────────────────────────────────────

    /**
     * 선의 대표 위치로 point1의 좌표를 반환한다.
     * Segment: 끝점 A의 위치 / StraightLine: 원점 P의 위치.
     */
    @Override
    public Position getPosition() {
        Point p1 = getPoint1();
        return (p1 != null) ? p1.getPosition() : new Position();
    }

    /**
     * point1에서 point2 방향으로의 각도를 도(degree) 단위로 반환한다.
     * atan2(dy, dx)를 사용하므로 반환 범위는 [-180°, 180°].
     */
    @Override
    public double getAngle() {
        Point p1 = getPoint1();
        Point p2 = getPoint2();
        if (p1 == null || p2 == null) return 0.0;
        double dx = p2.getPosition().getX() - p1.getPosition().getX();
        double dy = p2.getPosition().getY() - p1.getPosition().getY();
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * 선 전체를 평행 이동한다.
     * point1이 주어진 위치로 이동하고, point2는 같은 델타(dx, dy)만큼 동시에 이동하여
     * 선의 방향과 길이를 보존한다.
     */
    @Override
    public void moveTo(Position position) {
        Point p1 = getPoint1();
        Point p2 = getPoint2();
        if (p1 == null) return;
        // 이동 델타 계산: 목표 위치 - 현재 point1 위치
        double dx = position.getX() - p1.getPosition().getX();
        double dy = position.getY() - p1.getPosition().getY();
        if (p2 != null) {
            // point2도 같은 델타만큼 이동하여 방향·길이 보존
            p2.moveTo(new Position(
                p2.getPosition().getX() + dx,
                p2.getPosition().getY() + dy
            ));
        }
        p1.moveTo(position);
    }

    // ── 접근자 ────────────────────────────────────────────────────────────────

    /** 선 유형 메타데이터(SEGMENT, STRAIGHT_LINE 등)를 반환한다. */
    public LineInfo getLineInfo() { return lineInfo; }

    /**
     * 두 Point가 담긴 GeoList를 반환한다.
     * package-private: 외부 패키지 접근 차단. IntersectionLogic 전용.
     */
    GeoList getPointList() { return pointList; }

    /** Segment에서는 끝점 A, StraightLine에서는 원점 P를 반환한다. */
    public Point getPoint1() { return (Point) GeoList.getEntry(pointList, 0); }

    /** Segment에서는 끝점 B, StraightLine에서는 방향벡터 끝점 D를 반환한다. */
    public Point getPoint2() { return (Point) GeoList.getEntry(pointList, 1); }
}
