package GeoDL;

/**
 * 두 Objectivity 객체가 만나는지(교차)와, 만난다면 어디서 만나는지를 기록하는 관계 클래스.
 * (레거시 결과 운반자 — IntersectionResult와 함께 존재하는 기존 구조)
 *
 * 필드 의미:
 *   - intersectionInfo: 실제 교차 여부를 담는 메타데이터 객체 (true/false).
 *   - contactPoint:     교차점 좌표 (교차할 때만 유효).
 *   - optimalPoint1:    obj1의 최근접 점 (교차하지 않을 때 유효).
 *   - optimalPoint2:    obj2의 최근접 점 (교차하지 않을 때 유효).
 *
 * 캡슐화:
 *   - 내부 필드의 setter는 모두 package-private — IntersectionLogic(동일 패키지)만 직접 설정 가능.
 *   - 외부 계층은 getter를 통해서만 결과를 읽을 수 있다.
 *
 * 기존 한글 패키지의 '관계.만남'에서 이관 및 영문화.
 */
public class Intersection extends Relation {

    private IntersectionInfo intersectionInfo; // 교차 여부 메타데이터
    private Position contactPoint;             // 교차점 좌표
    private Position optimalPoint1;            // obj1의 최근접 점
    private Position optimalPoint2;            // obj2의 최근접 점
    private final GeoList relationList;        // 이 교차 관계에 참여한 객체들의 역참조 목록

    /** 빈 Intersection 객체 생성 (모든 필드 null). */
    public Intersection() {
        this.intersectionInfo = null;
        this.contactPoint     = null;
        this.optimalPoint1    = null;
        this.optimalPoint2    = null;
        this.relationList     = new GeoList();
    }

    /**
     * 두 객체 간의 교차 관계를 즉시 계산하여 결과를 채운다.
     * IntersectionLogic.compute()가 이 인스턴스의 setter들을 호출하여 결과를 기록한다.
     *
     * @throws IllegalArgumentException obj1 또는 obj2가 null인 경우
     */
    public Intersection(Objectivity obj1, Objectivity obj2) {
        this();
        if (obj1 == null || obj2 == null) {
            throw new IllegalArgumentException("Objectivity objects cannot be null.");
        }
        IntersectionLogic.compute(this, obj1, obj2); // 정적 로직 메서드에게 계산 위임
    }

    // ── Getter 공개 API ──────────────────────────────────────────────────────

    /** 교차 여부 메타데이터를 반환한다. */
    public IntersectionInfo getIntersectionInfo() { return intersectionInfo; }

    /** 교차점 좌표를 반환한다. 교차하지 않으면 null. */
    public Position         getContactPoint()     { return contactPoint; }

    /** obj1의 최근접 점을 반환한다. 교차하면 null. */
    public Position         getOptimalPoint1()    { return optimalPoint1; }

    /** obj2의 최근접 점을 반환한다. 교차하면 null. */
    public Position         getOptimalPoint2()    { return optimalPoint2; }

    /** 이 교차 관계에 참여한 객체들의 역참조 목록을 반환한다. */
    public GeoList          getRelationList()     { return relationList; }

    // ── Package-private setter (IntersectionLogic 전용) ───────────────────────

    /** 교차 여부 메타데이터를 설정한다. package-private — IntersectionLogic만 호출 가능. */
    void setIntersectionInfo(IntersectionInfo info)  { this.intersectionInfo = info; }

    /** 교차점 좌표를 설정한다. package-private — IntersectionLogic만 호출 가능. */
    void setContactPoint(Position point)             { this.contactPoint = point; }

    /** obj1의 최근접 점을 설정한다. package-private — IntersectionLogic만 호출 가능. */
    void setOptimalPoint1(Position point)            { this.optimalPoint1 = point; }

    /** obj2의 최근접 점을 설정한다. package-private — IntersectionLogic만 호출 가능. */
    void setOptimalPoint2(Position point)            { this.optimalPoint2 = point; }
}
