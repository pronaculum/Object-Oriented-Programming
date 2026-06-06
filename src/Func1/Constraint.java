package Func1;

import GeoDL.Objectivity;
import java.util.List;

/**
 * 모든 제약조건이 반드시 구현해야 할 공통 계약 인터페이스.
 *
 * 역할:
 *   - Func1 패키지의 제약조건 시스템에서 다형성을 가능하게 하는 핵심 인터페이스.
 *   - ConstraintHandler는 이 인터페이스 타입으로만 제약조건을 저장·관리하므로
 *     구체 구현체(Distance, Parallel, Perpendicular)를 몰라도 일관되게 처리할 수 있다.
 *
 * 3대 계약 메서드:
 *   - isSatisfied(): 현재 상태에서 제약이 충족되는지 확인
 *   - resolve():     제약을 물리적으로 충족시키도록 객체를 이동/회전
 *   - getConstrainedObjects(): 이 제약에 참여하는 객체 목록 반환 (삭제 시 정리용)
 */
public interface Constraint {
    /** 현재 상태에서 이 제약조건이 충족되어 있으면 true를 반환한다. */
    boolean isSatisfied();

    /** 제약조건을 충족시키기 위해 관련 객체의 위치나 각도를 조정한다. */
    void resolve();

    /**
     * 이 제약조건에 참여하는 객체 목록을 반환한다.
     * ConstraintHandler가 객체 삭제 시 관련 제약조건을 정리하는 데 사용한다.
     */
    List<Objectivity> getConstrainedObjects();
}
