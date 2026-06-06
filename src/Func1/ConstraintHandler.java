package Func1;

import GeoDL.Objectivity;
import java.util.HashMap;
import java.util.Map;

/**
 * 활성 제약조건을 ID 기반으로 등록·해제·일괄 처리하는 관리 클래스.
 *
 * 역할:
 *   - 모든 Constraint 구현체를 HashMap으로 저장하고 정수 ID로 식별한다.
 *   - resolveAll(): 불충족 제약조건만 선별하여 resolve()를 호출 (충족된 것은 건너뜀).
 *   - cleanUpForDeletedObject(): 삭제된 기하 객체가 포함된 제약조건을 자동으로 정리.
 *
 * 다형성 활용:
 *   - Constraint 인터페이스 타입으로만 저장하므로 Distance, Parallel, Perpendicular 등
 *     모든 구체 구현체를 동일하게 처리할 수 있다 (OCP: 개방-폐쇄 원칙 준수).
 */
public class ConstraintHandler {

    /** ID → Constraint 매핑. LinkedHashMap 대신 HashMap으로 삽입 순서 미보장 (순서 불필요) */
    private final Map<Integer, Constraint> constraintMap = new HashMap<>();
    private int nextId = 1; // 다음 할당할 제약조건 ID (1부터 증가)

    /**
     * 새 제약조건을 등록하고 부여된 ID를 반환한다.
     * 반환된 ID로 이후 removeConstraint()를 호출하여 제약조건을 해제할 수 있다.
     */
    public int addConstraint(Constraint constraint) {
        int id = nextId++;
        constraintMap.put(id, constraint);
        return id;
    }

    /**
     * 지정된 ID의 제약조건을 해제(등록 해제)한다.
     * 해당 ID가 없으면 아무 작업도 수행하지 않는다.
     */
    public void removeConstraint(int id) {
        constraintMap.remove(id);
    }

    /**
     * 현재 등록된 모든 제약조건을 점검하고, 불충족 상태인 것들을 resolve()로 강제 충족시킨다.
     * isSatisfied()가 true인 제약조건은 건너뛰어 불필요한 연산을 방지한다.
     */
    public void resolveAll() {
        for (Constraint constraint : constraintMap.values()) {
            if (!constraint.isSatisfied()) {
                constraint.resolve();
            }
        }
    }

    /**
     * 삭제된 기하 객체(deletedObj)가 포함된 모든 제약조건을 자동으로 정리한다.
     * 기하 객체를 삭제할 때 관련 제약조건이 고립(dangling)되지 않도록 호출한다.
     */
    public void cleanUpForDeletedObject(Objectivity deletedObj) {
        // removeIf: 조건을 만족하는 엔트리를 순회하며 즉시 제거 (ConcurrentModificationException 방지)
        constraintMap.values().removeIf(c -> c.getConstrainedObjects().contains(deletedObj));
    }
}
