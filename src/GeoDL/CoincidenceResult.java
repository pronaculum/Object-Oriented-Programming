package GeoDL;

/**
 * 두 기하 객체의 위상적 일치(Coincidence) 관계 결과를 담는 불변 데이터 클래스.
 *
 * coincident의 의미:
 *   - true:  두 객체가 완전히 겹치는 상태.
 *            선의 경우 → 방향 벡터가 동일하고 오프셋이 0인 경우.
 *            점의 경우 → 두 점이 동일한 좌표에 위치하는 경우.
 *   - false: 위상적으로 겹치지 않는 상태 (교차, 평행, 분리 등).
 *
 * ParallelResult와의 관계:
 *   - 방향 벡터가 같고 오프셋이 0 → CoincidenceResult(true), ParallelResult(false)
 *   - 방향 벡터가 같고 오프셋 있음 → CoincidenceResult(false), ParallelResult(true)
 */
public class CoincidenceResult extends RelationResult {

    private final boolean coincident; // 두 객체가 위상적으로 완전히 일치하는지 여부

    public CoincidenceResult(boolean coincident) {
        super(RelationType.COINCIDENCE);
        this.coincident = coincident;
    }

    /** 두 객체가 완전히 일치(겹침) 상태이면 true를 반환한다. */
    public boolean isCoincident() { return coincident; }

    @Override
    public String toString() {
        return super.toString() + " coincident=" + coincident;
    }
}
