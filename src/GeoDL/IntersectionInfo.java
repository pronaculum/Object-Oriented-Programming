package GeoDL;

/**
 * 두 객체가 실제로 만나는지(교차 여부)를 기록하는 메타데이터 운반 클래스.
 *
 * 역할:
 *   - Intersection(레거시 관계 결과) 내부에서 intersects 플래그를 캡슐화하여 전달하는 역할.
 *   - private final boolean으로 불변성 보장.
 *
 * 기존 한글 패키지의 '만남정보' 클래스에서 이관 및 영문화.
 */
public class IntersectionInfo extends Information {
    private final boolean intersects; // 두 객체가 실제로 교차하는지 여부

    /** 기본값: 교차하지 않음 (false) */
    public IntersectionInfo() {
        this.intersects = false;
    }

    /** 교차 여부를 명시적으로 지정하여 생성 */
    public IntersectionInfo(boolean intersects) {
        this.intersects = intersects;
    }

    /** 두 객체가 실제로 교차하면 true를 반환한다. */
    public boolean isIntersecting() { return intersects; }
}
