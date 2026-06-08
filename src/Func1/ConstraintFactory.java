package Func1;

import GeoDL.Objectivity;
import java.util.List;

/**
 * Constraint 인스턴스 생성을 전담하는 팩토리 클래스 (SRP: 단일 책임 원칙 적용).
 *
 * 역할:
 *   - 제약조건의 생성 로직을 GeoManager나 호출자로부터 분리하여 단일 책임 원칙을 실현.
 *   - private 생성자로 인스턴스화 방지 — 모든 메서드가 정적(static).
 *   - 현재 지원하는 제약조건 유형:
 *     1. 거리 제약(ConstraintTypeDistance): 두 객체 중심 간 거리를 일정하게 유지
 *     2. 평행 제약(ConstraintTypeParallel): N개의 선 객체를 서로 평행하게 유지
 */
public class ConstraintFactory {

    /** 유틸리티 클래스 — 인스턴스 생성 불가 */
    private ConstraintFactory() {}

    /**
     * 두 객체 사이의 거리를 targetDistance로 유지하는 제약조건을 생성한다.
     * @param obj1           첫 번째 기하 객체
     * @param obj2           두 번째 기하 객체
     * @param targetDistance 유지할 목표 거리 (중심점 간 거리)
     */
    public static Constraint createDistanceConstraint(Objectivity obj1, Objectivity obj2,
                                                      double targetDistance) {
        return new ConstraintTypeDistance(obj1, obj2, targetDistance);
    }

    /**
     * N개의 선 객체를 서로 평행하게 유지하는 제약조건을 생성한다.
     * @param lines 평행하게 유지할 선 객체 목록 (최소 2개 이상)
     */
    public static Constraint createParallelConstraint(List<Objectivity> lines) {
        return new ConstraintTypeParallel(lines);
    }
}
