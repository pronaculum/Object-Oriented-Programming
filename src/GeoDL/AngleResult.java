package GeoDL;

/**
 * 두 기하 객체 간의 사잇각(Angle) 관계 결과를 담는 불변 데이터 클래스.
 *
 * angleDegrees:
 *   - 두 방향 벡터 사이의 가장 작은 양의 각도 (범위: 0°~180°).
 *   - 예: 수직 두 선분 → 90.0°, 평행 두 선분 → 0.0°, 반평행 두 선분 → 0.0°.
 *   - GeoManager에서 Math.acos(|dot|)로 계산하므로 항상 예각 또는 직각 범위를 반환.
 *
 * 캡슐화: angleDegrees는 private final — 생성 후 변경 불가.
 */
public class AngleResult extends RelationResult {

    private final double angleDegrees; // 사잇각 (단위: 도, 범위: 0~180)

    public AngleResult(double angleDegrees) {
        super(RelationType.ANGLE);
        this.angleDegrees = angleDegrees;
    }

    /** 두 객체 사이의 사잇각을 도(degree) 단위로 반환한다. */
    public double getAngleDegrees() { return angleDegrees; }

    @Override
    public String toString() {
        return super.toString() + " angleDegrees=" + angleDegrees;
    }
}
