package Func1;

import GeoDL.Objectivity;
import java.util.Arrays;
import java.util.List;

/**
 * 두 도형(주로 선분·직선)이 서로 직교(90도)를 이루도록 유지하는 제약조건.
 *
 * isSatisfied():
 *   두 객체의 각도 차이가 90도(또는 270도 등)에 EPSILON 이내이면 충족.
 *   현재 getAngle() 스텁이 0.0을 반환하므로 실제 판별이 이루어지지 않는다.
 *
 * resolve():
 *   line1의 각도를 기준으로 line2를 (line1.angle + 90도) 방향으로 회전.
 *   rotateToAngle() 스텁이므로 실제 회전이 발생하지 않는다.
 *
 * ⚠️ 기술 부채:
 *   getAngle()과 rotateToAngle()이 private 스텁 메서드(반환값 0.0 고정)로만 구현됨.
 *   (완전 구현 참고: Func1_new의 ConstraintTypePerpendicular — 보호 자산)
 *
 * 캡슐화:
 *   - line1, line2는 private final — 생성 후 변경 불가.
 */
public class ConstraintTypePerpendicular implements Constraint {

    private final Objectivity line1; // 기준이 되는 첫 번째 선 객체
    private final Objectivity line2; // 직교시킬 두 번째 선 객체
    private static final double EPSILON = 1e-6; // 각도 비교 허용 오차

    public ConstraintTypePerpendicular(Objectivity line1, Objectivity line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    /**
     * 두 객체의 각도 차이가 90도에 EPSILON 이내이면 충족 판정.
     * 현재 getAngle() 스텁이 0.0을 반환하므로 항상 false 판정됨 (구현 한계).
     */
    @Override
    public boolean isSatisfied() {
        double angle1 = getAngle(line1);
        double angle2 = getAngle(line2);

        // |각도 차이| mod 180 이 90도에 가까우면 직교 상태
        double angleDifference = Math.abs(angle1 - angle2) % 180;
        return Math.abs(angleDifference - 90.0) <= EPSILON;
    }

    /**
     * line1의 각도를 기준으로 line2를 직교 방향으로 회전시킨다.
     * 이미 충족된 경우 조기 종료.
     */
    @Override
    public void resolve() {
        if (isSatisfied()) return;

        double angle1 = getAngle(line1);
        double targetAngleForLine2 = angle1 + 90.0; // line1에 수직인 각도

        rotateToAngle(line2, targetAngleForLine2); // line2를 직교 방향으로 회전
    }

    @Override
    public List<Objectivity> getConstrainedObjects() {
        return Arrays.asList(line1, line2);
    }

    // ── 내부 수학 로직 (현재 스텁 — 완전 구현 필요) ─────────────────────────

    /** 객체의 방향각을 반환한다. ⚠️ 스텁: 항상 0.0 반환. Objectivity.getAngle() 연동 필요. */
    private double getAngle(Objectivity obj) { return 0.0; }

    /** 객체를 targetAngle로 회전시킨다. ⚠️ 스텁: 아무 동작 없음. moveTo() 연동 필요. */
    private void rotateToAngle(Objectivity obj, double targetAngle) {}
}
