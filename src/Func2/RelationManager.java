package Func2;

import GeoDL.Objectivity;
import GeoDL.RelationResult;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 기하 객체 쌍을 등록하고, 4대 기하 관계(각도·교차·평행·일치)를 실시간으로 조회·갱신하는 관리 클래스.
 *
 * Epic 5 이관:
 *   기존 GeoDL 패키지에 있던 관계 관리 책임을 Func2로 분리.
 *   GeoDL 도메인은 객체 정의에, Func2는 관계 연산·관리에 집중하도록 패키지 역할을 명확히 구분.
 *
 * 구성 전략:
 *   - pairs  : ID → ObjectPair — 등록된 기하 객체 쌍 (관계 대상 원본 참조 보관)
 *   - cache  : ID → List<RelationResult> — GeoManager 계산 결과를 캐시 (중복 연산 방지)
 *   - nextId : 1부터 단순 증가하는 정수 ID (삭제 후 재사용 없음 — 혼동 방지)
 *
 * 삽입 순서 보장:
 *   pairs와 cache 모두 LinkedHashMap 사용.
 *   listAll() 결과를 예측 가능한 순서로 반환하기 위해 등록 순서를 보존한다.
 *
 * 불변 보호:
 *   getRelations() / listAll() 반환 리스트는 모두 unmodifiableList/unmodifiableMap으로 래핑.
 *   호출자가 캐시 데이터를 직접 변조하지 못하도록 보호한다.
 */
public class RelationManager {

    /**
     * 두 기하 객체의 쌍을 묶어 캡슐화하는 정적 내부 클래스.
     * 불변 상태(final 필드)로 등록 이후 쌍의 구성이 바뀌지 않음을 보장한다.
     */
    private static class ObjectPair {
        final Objectivity first;  // 첫 번째 기하 객체 (등록 시 지정, 이후 변경 불가)
        final Objectivity second; // 두 번째 기하 객체 (등록 시 지정, 이후 변경 불가)

        ObjectPair(Objectivity first, Objectivity second) {
            this.first  = first;
            this.second = second;
        }
    }

    /** 등록된 기하 객체 쌍 저장소 — 삽입 순서 보존을 위해 LinkedHashMap 사용 */
    private final Map<Integer, ObjectPair> pairs          = new LinkedHashMap<>();
    /** 관계 연산 결과 캐시 — ID에 대응하는 최신 RelationResult 목록 */
    private final Map<Integer, List<RelationResult>> cache = new LinkedHashMap<>();
    /** 다음 할당할 관계 쌍 ID (1부터 시작하는 단순 증가 카운터) */
    private int nextId = 1;

    /**
     * 두 기하 객체를 관계 쌍으로 등록하고 즉시 초기 관계를 계산하여 캐시에 저장한다.
     *
     * @param obj1 첫 번째 기하 객체 (null 불가)
     * @param obj2 두 번째 기하 객체 (null 불가)
     * @return 할당된 관계 쌍 ID — 이후 getRelations/update/unregister 호출 시 사용
     * @throws IllegalArgumentException null 입력 시
     */
    public int register(Objectivity obj1, Objectivity obj2) {
        if (obj1 == null || obj2 == null) {
            throw new IllegalArgumentException("Objects must not be null.");
        }
        int id = nextId++;
        pairs.put(id, new ObjectPair(obj1, obj2)); // 원본 참조 보관
        cache.put(id, GeoManager.relate(obj1, obj2)); // 초기 관계 즉시 계산
        return id;
    }

    /**
     * 등록된 관계 쌍과 캐시된 연산 결과를 모두 제거한다.
     * 기하 객체 자체는 삭제하지 않는다 — 쌍 등록 해제만 수행.
     *
     * @param id unregister할 관계 쌍 ID (존재하지 않으면 아무 작업도 수행 안 함)
     */
    public void unregister(int id) {
        pairs.remove(id);
        cache.remove(id);
    }

    /**
     * 지정된 ID의 관계 쌍에 대한 최근 계산 결과를 반환한다.
     * 객체가 이동한 경우 update(id)를 먼저 호출해야 최신 상태가 반영된다.
     *
     * @param id 조회할 관계 쌍 ID
     * @return 읽기 전용 RelationResult 리스트 (unmodifiableList)
     * @throws IllegalArgumentException 등록되지 않은 ID인 경우
     */
    public List<RelationResult> getRelations(int id) {
        List<RelationResult> results = cache.get(id);
        if (results == null) {
            throw new IllegalArgumentException("No pair registered with id " + id);
        }
        return Collections.unmodifiableList(results); // 외부 변조 차단
    }

    /**
     * 지정된 ID의 관계 쌍에 대해 GeoManager.relate()를 다시 호출하여 캐시를 최신화한다.
     * 기하 객체의 위치·방향이 변경된 후 호출하여 실시간 관계를 반영한다.
     *
     * @param id 갱신할 관계 쌍 ID
     * @throws IllegalArgumentException 등록되지 않은 ID인 경우
     */
    public void update(int id) {
        ObjectPair pair = pairs.get(id);
        if (pair == null) {
            throw new IllegalArgumentException("No pair registered with id " + id);
        }
        cache.put(id, GeoManager.relate(pair.first, pair.second)); // 관계 재계산 후 캐시 갱신
    }

    /**
     * 등록된 모든 관계 쌍의 캐시를 일괄 갱신한다.
     * 여러 객체가 동시에 이동·회전한 뒤 전체 관계 상태를 동기화할 때 사용한다.
     */
    public void updateAll() {
        for (Integer id : pairs.keySet()) {
            cache.put(id, GeoManager.relate(pairs.get(id).first, pairs.get(id).second));
        }
    }

    /**
     * 현재 캐시된 모든 관계 쌍의 결과를 스냅샷으로 반환한다.
     * 최신 상태가 필요하면 이 메서드 호출 전에 updateAll()을 먼저 실행해야 한다.
     *
     * @return 관계 쌍 ID를 키로 하는 읽기 전용 맵 (unmodifiableMap).
     *         각 값 리스트도 unmodifiableList로 래핑됨.
     *         등록 순서(LinkedHashMap)에 따라 순서가 보존됨.
     */
    public Map<Integer, List<RelationResult>> listAll() {
        Map<Integer, List<RelationResult>> snapshot = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<RelationResult>> entry : cache.entrySet()) {
            // 개별 리스트도 unmodifiableList로 래핑 — 중첩된 읽기 전용 보장
            snapshot.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(snapshot); // 최외곽 맵도 읽기 전용
    }
}
