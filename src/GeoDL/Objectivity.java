package GeoDL;

/**
 * 기하 객체가 반드시 구현해야 할 범용 계약 인터페이스 (대상성 인터페이스).
 *
 * 역할:
 *   - 모든 사용자 접점 기하 객체(Point, Segment, StraightLine 등)는 이 인터페이스를 구현한다.
 *   - GeoManager, RelationManager, ConstraintHandler 등 상위 계층이
 *     구체 타입을 몰라도 이 인터페이스를 통해 위치·각도·이동을 조작할 수 있다.
 *
 * 3대 계약 메서드:
 *   - getPosition(): 객체의 대표 좌표(앵커 포인트)를 반환
 *   - getAngle():    객체의 방향각(도 단위)을 반환. 점은 관례상 0.0 반환.
 *   - moveTo():      객체를 주어진 Position으로 이동. 구현체마다 이동 의미가 다름.
 *
 * 기존 한글 패키지의 '대상성' 인터페이스에서 이관 및 영문화.
 */
public interface Objectivity {
    /** 이 기하 객체의 대표 위치(앵커) 좌표를 반환한다. */
    Position getPosition();

    /** 이 기하 객체의 방향각을 도(degree) 단위로 반환한다. 점(Point)은 0.0을 반환. */
    double getAngle();

    /**
     * 이 기하 객체를 주어진 Position으로 이동한다.
     * 점의 경우: 해당 좌표로 직접 이동.
     * 선의 경우: point1이 해당 좌표로 이동하고, point2는 같은 델타만큼 평행 이동.
     */
    void moveTo(Position position);
}
