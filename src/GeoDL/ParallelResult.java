package GeoDL;

/**
 * 두 선 객체의 평행(Parallel) 관계 결과를 담는 불변 데이터 클래스.
 *
 * parallel의 의미:
 *   - true:  두 선의 방향 벡터가 선형적으로 동일하고(외적 = 0),
 *            그러면서도 같은 직선 위에 있지 않은(오프셋 존재) 평행 상태.
 *   - false: 교차하거나, 일치하거나, 방향이 다른 상태.
 *
 * 일치(Coincidence)와의 구분:
 *   - 방향 벡터가 같고 오프셋도 0인 경우 → CoincidenceResult(true), ParallelResult(false)
 *   - 방향 벡터가 같고 오프셋이 있는 경우 → CoincidenceResult(false), ParallelResult(true)
 */
public class ParallelResult extends RelationResult {

    private final boolean parallel; // 두 선이 평행 상태인지 여부

    public ParallelResult(boolean parallel) {
        super(RelationType.PARALLEL);
        this.parallel = parallel;
    }

    /** 두 선이 평행이면 true를 반환한다. (일치 상태는 false) */
    public boolean isParallel() { return parallel; }

    @Override
    public String toString() {
        return super.toString() + " parallel=" + parallel;
    }
}
