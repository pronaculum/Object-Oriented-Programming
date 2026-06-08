package Func1;

import GeoDL.Objectivity;
import GeoDL.Position;
import java.util.Arrays;
import java.util.List;

/**
 * 두 Objectivity 객체의 중심점 사이 거리를 targetDistance로 유지하는 제약조건.
 *
 * isSatisfied():
 *   현재 두 객체의 중심점 거리가 targetDistance와 EPSILON 이하 차이면 충족 판정.
 *
 * resolve():
 *   두 객체의 중간점(midpoint)을 고정하고,
 *   각 객체를 중간점으로부터 targetDistance/2 거리로 대칭 이동한다.
 *   → 대칭 이동 방식으로 두 객체가 동등하게 조정되므로 한쪽 객체만 일방적으로 이동하는 편향 없음.
 *
 * 캡슐화:
 *   - obj1, obj2는 private final — 생성 후 변경 불가.
 *   - targetDistance는 private final — 외부에서 목표 거리를 바꾸려면 새 제약조건 생성 필요.
 */
public class ConstraintTypeDistance implements Constraint {

    private final Objectivity obj1;
    private final Objectivity obj2;
    private final double targetDistance; // 유지할 목표 거리 (중심점 간)
    private static final double EPSILON = 1e-6; // 부동소수점 비교 허용 오차

    public ConstraintTypeDistance(Objectivity obj1, Objectivity obj2, double targetDistance) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.targetDistance = targetDistance;
    }

    /**
     * 현재 두 객체의 중심점 거리가 targetDistance에 EPSILON 이내이면 충족.
     */
    @Override
    public boolean isSatisfied() {
        return Math.abs(calculateDistance(obj1, obj2) - targetDistance) <= EPSILON;
    }

    /**
     * 두 객체를 중간점을 기준으로 대칭 이동하여 목표 거리를 달성한다.
     * 이미 충족된 경우 조기 종료하여 불필요한 이동을 방지한다.
     */
    @Override
    public void resolve() {
        if (isSatisfied()) return;
        adjustDistanceToTarget(obj1, obj2, targetDistance);
    }

    @Override
    public List<Objectivity> getConstrainedObjects() {
        return Arrays.asList(obj1, obj2);
    }

    // ── 내부 수학 로직 ────────────────────────────────────────────────────────

    /** 두 객체의 중심점 사이의 유클리드 거리를 계산한다. */
    private double calculateDistance(Objectivity a, Objectivity b) {
        double dx = a.getPosition().getX() - b.getPosition().getX();
        double dy = a.getPosition().getY() - b.getPosition().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 두 객체를 중간점 기준으로 대칭 이동하여 목표 거리를 달성한다.
     *
     * 알고리즘:
     *   1. 현재 두 점의 중간점(cx, cy) 계산
     *   2. 현재 방향벡터를 단위벡터(nx, ny)로 정규화
     *   3. 각 점을 중간점으로부터 target/2 만큼 단위벡터 방향으로 대칭 배치
     */
    private void adjustDistanceToTarget(Objectivity a, Objectivity b, double target) {
        double cx = (a.getPosition().getX() + b.getPosition().getX()) / 2.0; // 중간점 x
        double cy = (a.getPosition().getY() + b.getPosition().getY()) / 2.0; // 중간점 y
        double dx = a.getPosition().getX() - b.getPosition().getX();
        double dy = a.getPosition().getY() - b.getPosition().getY();
        double current = Math.sqrt(dx * dx + dy * dy);
        if (current < EPSILON) return; // 두 점이 사실상 같은 위치 → 이동 방향 불명확, 건너뜀
        double half = target / 2.0;
        double nx = dx / current, ny = dy / current; // 단위 방향벡터
        a.moveTo(new Position(cx + nx * half, cy + ny * half));
        b.moveTo(new Position(cx - nx * half, cy - ny * half));
    }
}
