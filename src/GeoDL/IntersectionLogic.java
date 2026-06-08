package GeoDL;

/**
 * 두 Objectivity 객체 사이의 교차(Intersection) 관계를 계산하는 정적 유틸리티 클래스.
 *
 * 핵심 설계 원칙 (instructions.md §0 준수):
 *   더블 디스패치(Visitor 패턴) 완전 금지.
 *   런타임 타입 판별은 단일 진입점인 compute() 내부의 명시적 instanceof 체인으로만 수행.
 *   이는 팀이 합의한 "직관적 조건문 판별 구조"에 해당하며, Visitor 우회 도입을 방지한다.
 *
 * 지원하는 객체 조합:
 *   - Point × Point     → 두 점의 일치 여부 판별
 *   - Point × Segment   → 점이 선분 위에 있는지 판별 (t 매개변수 기반)
 *   - Point × StraightLine → 점이 무한 직선 위에 있는지 판별 (외적 판별)
 *
 * 기존 한글 패키지의 'Func2.만남로직'에서 이관 및 영문화.
 */
public class IntersectionLogic {

    /** 부동소수점 동등 비교 허용 오차 (IEEE 754 double 연산 오류 흡수) */
    private static final double EPSILON = 1e-9;

    /** 유틸리티 클래스 — 인스턴스 생성 불가 */
    private IntersectionLogic() {}

    /**
     * 두 Objectivity 객체 간의 교차 관계를 계산하여 result에 기록한다.
     * instanceof 체인으로 런타임 타입을 판별하고 적합한 알고리즘 메서드에 위임한다.
     *
     * @param result 결과를 기록할 Intersection 인스턴스 (setter로 채움)
     * @param obj1   첫 번째 기하 객체
     * @param obj2   두 번째 기하 객체
     * @throws IllegalArgumentException 지원하지 않는 타입 조합인 경우
     */
    public static void compute(Intersection result, Objectivity obj1, Objectivity obj2) {
        if (obj1 instanceof Point && obj2 instanceof Point) {
            computePointPoint(result, (Point) obj1, (Point) obj2);
        } else if (obj1 instanceof Point && obj2 instanceof Segment) {
            computePointSegment(result, (Point) obj1, (Segment) obj2);
        } else if (obj1 instanceof Point && obj2 instanceof StraightLine) {
            computePointStraightLine(result, (Point) obj1, (StraightLine) obj2);
        } else {
            throw new IllegalArgumentException("Unsupported Objectivity type combination: "
                + obj1.getClass().getSimpleName() + " and " + obj2.getClass().getSimpleName());
        }
    }

    // ── 알고리즘: Point ∩ Point ────────────────────────────────────────────────

    /**
     * 두 점 사이의 거리가 EPSILON 이하이면 교차(일치)로 판정.
     * 거리 제곱 비교로 sqrt 호출을 생략하여 성능 최적화.
     */
    private static void computePointPoint(Intersection result, Point p1, Point p2) {
        double x1 = p1.getPosition().getX(), y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX(), y2 = p2.getPosition().getY();
        double dx = x1 - x2, dy = y1 - y2;
        if ((dx * dx + dy * dy) < EPSILON * EPSILON) {
            // 두 점이 사실상 동일한 위치 → 교차(일치)
            result.setContactPoint(p1.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(true));
        } else {
            // 두 점이 서로 다른 위치 → 비교차, 각자의 위치가 최근접 점
            result.setOptimalPoint1(p1.getPosition());
            result.setOptimalPoint2(p2.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(false));
        }
    }

    // ── 알고리즘: Point ∩ Segment ──────────────────────────────────────────────

    /**
     * 점 p가 선분 seg 위에 있는지 판별한다.
     *
     * 알고리즘:
     *   1. 선분 방향벡터 (dX, dY)에 대한 점 p의 t 매개변수 계산:
     *      t = dot(p - p1, dir) / |dir|²
     *   2. 외적 ||(p-p1) × dir|| < EPSILON → p가 선분의 연장선 위에 있음 (collinear 판별)
     *   3. 0 ≤ t ≤ 1이면 선분 구간 내부에 있음
     *
     * 비교차 시 최근접 점 산정:
     *   - t < 0 → 선분 시작점 p1이 최근접
     *   - t > 1 → 선분 끝점 p2가 최근접
     *   - 0 ≤ t ≤ 1 → 연장선 위에 있지만 범위 외 → 투영점이 최근접
     */
    private static void computePointSegment(Intersection result, Point p, Segment seg) {
        Point p1 = seg.getPoint1();
        Point p2 = seg.getPoint2();

        double x  = p.getPosition().getX(),  y  = p.getPosition().getY();
        double x1 = p1.getPosition().getX(), y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX(), y2 = p2.getPosition().getY();

        double dX = x2 - x1, dY = y2 - y1; // 선분 방향벡터
        double lenSq = dX * dX + dY * dY;   // 방향벡터 크기의 제곱
        // t: 점 p를 선분 방향으로 투영한 매개변수 (0~1이면 선분 구간 내)
        double t = (lenSq < EPSILON) ? 0 : ((x - x1) * dX + (y - y1) * dY) / lenSq;

        // 외적으로 collinear(선 위) 여부 판별
        boolean onLine = Math.abs((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1)) < EPSILON;
        if (onLine && t >= 0 && t <= 1) {
            result.setContactPoint(p.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(true));
        } else {
            result.setOptimalPoint1(p.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(false));
            if (t < 0) {
                result.setOptimalPoint2(p1.getPosition()); // 시작점 방향으로 벗어남
            } else if (t > 1) {
                result.setOptimalPoint2(p2.getPosition()); // 끝점 방향으로 벗어남
            } else {
                result.setOptimalPoint2(new Position(x1 + t * dX, y1 + t * dY)); // 투영점
            }
        }
    }

    // ── 알고리즘: Point ∩ StraightLine ────────────────────────────────────────

    /**
     * 점 p가 무한 직선 line 위에 있는지 판별한다.
     *
     * 알고리즘:
     *   StraightLine은 원점 P와 방향벡터 D(= point2 - point1 방향)로 정의.
     *   점 p가 직선 위에 있으면: (p - P) × D = 0 (외적 = 0)
     *   비교차 시 직선에 대한 p의 수선의 발(foot of perpendicular)을 optimalPoint2로 설정.
     *
     * 주의:
     *   이 메서드는 D(= directionPoint - originPoint 벡터)가 단위 벡터라고 가정한다.
     *   호출자(StraightLine 생성자)가 단위화를 보장해야 한다.
     */
    private static void computePointStraightLine(Intersection result, Point p, StraightLine line) {
        Point P = line.getPoint1();  // 직선의 원점 P
        Point D = line.getPoint2();  // 방향벡터 끝점 D

        double x  = p.getPosition().getX(), y  = p.getPosition().getY();
        double xp = P.getPosition().getX(), yp = P.getPosition().getY();
        double xd = D.getPosition().getX(), yd = D.getPosition().getY();

        // 외적 판별: (p - P) × D = 0이면 직선 위의 점
        if (Math.abs((x - xp) * yd - (y - yp) * xd) < EPSILON) {
            result.setContactPoint(p.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(true));
        } else {
            result.setOptimalPoint1(p.getPosition());
            result.setIntersectionInfo(new IntersectionInfo(false));
            // 수선의 발 계산: t = dot(p - P, D), foot = P + t * D
            double t = (x - xp) * xd + (y - yp) * yd;
            result.setOptimalPoint2(new Position(xp + t * xd, yp + t * yd));
        }
    }
}
