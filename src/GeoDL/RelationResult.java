package GeoDL;

/**
 * 기하학 관계 연산 결과의 최상위 추상 기반 클래스.
 *
 * 역할:
 *   - GeoManager.relate()가 반환하는 List에 담기는 모든 결과 객체의 공통 부모.
 *   - 4대 관계 유형(ANGLE, INTERSECTION, PARALLEL, COINCIDENCE)을 RelationType 열거형으로 정의.
 *   - private final relationType으로 한번 생성된 결과의 유형을 불변으로 보장.
 *
 * 캡슐화:
 *   - relationType 필드는 private final — 외부에서 변경 불가.
 *   - 서브클래스는 protected 생성자를 통해서만 relationType을 설정할 수 있다.
 */
public abstract class RelationResult {

    /** 4대 기하학적 관계 유형 */
    public enum RelationType {
        ANGLE,        // 두 객체 간의 사잇각
        INTERSECTION, // 두 객체의 교차(만남) 여부 및 교점
        PARALLEL,     // 두 선 방향의 평행 여부
        COINCIDENCE   // 두 객체의 위상적 일치(완전 겹침) 여부
    }

    private final RelationType relationType; // 이 결과가 나타내는 관계 유형

    /** 서브클래스 생성자에서 호출 — 관계 유형을 한번 지정하고 불변으로 고정 */
    protected RelationResult(RelationType relationType) {
        this.relationType = relationType;
    }

    /** 이 결과가 나타내는 관계 유형을 반환한다. */
    public RelationType getRelationType() { return relationType; }

    @Override
    public String toString() {
        return "[" + relationType.name() + "]";
    }
}
