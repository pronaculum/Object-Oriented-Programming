package GeoDL;

/**
 * 원점 P(point1)와 방향벡터 끝점 D(point2)로 정의되는 무한 직선(StraightLine).
 *
 * 표현 방식:
 *   - 원점 P에서 방향벡터 (D - P)를 따라 양방향으로 무한히 뻗어 있는 직선.
 *   - GeoManager.relate()에서 Line×Line 관계 연산 시, StraightLine은 경계 조건 없이
 *     모든 매개변수 t에 대해 교차점 계산이 허용된다.
 *   - 도메인 비전: "원점과 방향벡터를 다룰 때조차 이를 점들의 관계로 환원하여 표현한다."
 *
 * 주의:
 *   - 방향벡터(D - P)가 단위 벡터일 필요는 없으나,
 *     GeoManager.relatePointStraightLine()은 D를 단위 방향벡터 끝점으로 간주한다.
 *     호출자가 단위화를 보장해야 한다.
 *
 * 기존 한글 패키지의 '관계.직선'에서 이관 및 영문화.
 */
public class StraightLine extends Line {

    /**
     * 무한 직선을 생성한다.
     * @param originPoint    직선의 원점 P (getPoint1()로 접근)
     * @param directionPoint 방향벡터 끝점 D (getPoint2()로 접근)
     */
    public StraightLine(Point originPoint, Point directionPoint) {
        super(new LineInfo(LineInfo.LineType.STRAIGHT_LINE), originPoint, directionPoint);
    }
}
