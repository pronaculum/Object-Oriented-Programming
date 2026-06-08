package GeoDL;

/**
 * 구체적인 기하학 점(Point) — 독립적으로 존재하는 [정보(Information)] 단위.
 *
 * 도메인 비전 구현:
 *   - "점은 그 자체로 독립적인 의미를 가지는 고유한 정보 단위다."
 *   - Point는 어떤 관계에도 종속되지 않으며, 자신의 위치(Position)를 독자적으로 보유한다.
 *   - 반면 Line(관계)은 두 Point의 참조를 빌려서만 존재한다 — 즉, 점이 먼저 존재해야 선이 생긴다.
 *
 * 역참조 리스트(relationList):
 *   - 이 점이 참여하고 있는 모든 관계(Line, Intersection 등)의 역참조를 GeoList로 보관한다.
 *   - 자료구조 학파의 요구: 런타임에 역방향 참조가 필요한 경우 탐색 효율을 보장.
 *
 * 기존 한글 패키지의 '정보.점' + '대상성.대상성점'을 단일 클래스로 통합·영문화.
 */
public class Point extends Information implements Objectivity {

    private Position position;           // 현재 좌표 (불변 Position 객체를 교체하는 방식으로 이동)
    private final GeoList relationList;  // 이 점이 참여하는 관계 객체들의 역참조 목록

    /** 원점 (0, 0)에 위치하는 점 생성 */
    public Point() {
        this.position = new Position();
        this.relationList = new GeoList();
    }

    /** 지정된 Position으로 점 생성. null이면 원점으로 대체. */
    public Point(Position position) {
        this.position = (position != null) ? position : new Position();
        this.relationList = new GeoList();
    }

    /** 지정된 (x, y) 좌표로 점 생성 */
    public Point(double x, double y) {
        this.position = new Position(x, y);
        this.relationList = new GeoList();
    }

    // ── Objectivity 계약 구현 ──────────────────────────────────────────────────

    /** 현재 위치를 반환한다. (불변 Position 객체) */
    @Override
    public Position getPosition() { return position; }

    /**
     * 점은 방향이 없으므로 관례상 0.0을 반환.
     * GeoManager가 Point x Line 관계를 계산할 때 이 값은 사용되지 않는다.
     */
    @Override
    public double getAngle() { return 0.0; }

    /**
     * 이 점을 주어진 Position으로 이동한다.
     * null 전달 시 원점으로 이동 (방어적 처리).
     * 이 점을 참조하는 Line은 자동으로 업데이트된다 (참조 공유 구조).
     */
    @Override
    public void moveTo(Position position) {
        this.position = (position != null) ? position : new Position();
    }

    // ── 관계 역참조 접근자 ─────────────────────────────────────────────────────

    /** 이 점이 참여하는 관계 객체(Line 등) 목록을 반환한다. */
    public GeoList getRelationList() { return relationList; }
}
