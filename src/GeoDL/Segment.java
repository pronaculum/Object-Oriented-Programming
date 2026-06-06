package GeoDL;

/**
 * 두 끝점 A(point1)와 B(point2)로 정의되는 유한 선분(Segment).
 *
 * 선분의 기하학적 의미:
 *   - 시작점 A와 끝점 B 사이의 유한한 구간만 존재하는 선.
 *   - getAngle()은 A→B 방향의 각도를 반환한다.
 *   - Line.moveTo()는 A가 목표 위치로 이동하고 B는 같은 델타만큼 이동하여 방향과 길이를 보존한다.
 *
 * 기존 한글 패키지의 '관계.선분'에서 이관 및 영문화.
 */
public class Segment extends Line {

    /**
     * 두 끝점으로 선분을 생성한다.
     * @param pointA 시작점 A (getPoint1()로 접근)
     * @param pointB 끝점 B (getPoint2()로 접근)
     */
    public Segment(Point pointA, Point pointB) {
        super(new LineInfo(LineInfo.LineType.SEGMENT), pointA, pointB);
    }
}
