package Func3;

import GeoDL.Point;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 다수의 TrackedObject를 통합 관리하고 궤적 예측 연산을 조율하는 파사드(Facade) 클래스.
 *
 * 구성요소 (생성자 주입):
 *   - predictor  : 실제 예측 알고리즘 구현체 (Predictor 인터페이스 — 현재 LinearExtrapolationPredictor)
 *   - constraint : 예측 가능 여부 판별 게이트 (PredictionConstraint — 최소 점 수 검사)
 *   - objects    : TrackedObject를 ID로 관리하는 HashMap
 *
 * 설계 원칙:
 *   PredictionManager는 알고리즘 구현을 직접 갖지 않는다.
 *   predictor와 constraint에 위임(Delegate)하여 변경 없이 알고리즘 교체가 가능하다.
 *
 * 예외 전략:
 *   - 단일 ID 예측 (predictNextObject/predictPreviousObject): 존재하지 않는 ID나 데이터 부족 → 예외 발생
 *   - 전체 예측 (predictAllNext/predictAllPrevious): 조건 불충족 객체는 결과 맵에서 제외 (조용히 건너뜀)
 *
 * LinkedHashMap 사용 (predictAll*):
 *   predictAll 결과 맵은 LinkedHashMap으로 등록 순서를 보존하여
 *   동일 호출 시 예측 가능한 순서로 결과를 반환한다.
 */
public class PredictionManager {

    /** 추적 대상 ID → TrackedObject 저장소 */
    private final Map<Integer, TrackedObject> objects;
    /** 궤적 예측 알고리즘 구현체 (Predictor 인터페이스 위임) */
    private final Predictor predictor;
    /** 예측 가능 여부 판별 게이트키퍼 */
    private final PredictionConstraint constraint;

    /**
     * @param predictor  사용할 예측 알고리즘 구현체
     * @param constraint 예측 수행 전 데이터 유효성 검사 제약조건
     */
    public PredictionManager(Predictor predictor, PredictionConstraint constraint) {
        this.objects    = new HashMap<>();
        this.predictor  = predictor;
        this.constraint = constraint;
    }

    /**
     * 새 추적 대상 객체를 등록한다.
     * 이미 동일 ID가 존재하면 덮어쓴다.
     *
     * @param object 등록할 TrackedObject (null 불가)
     */
    public void addObject(TrackedObject object) {
        objects.put(object.getId(), object);
    }

    /**
     * 지정 ID의 추적 대상을 제거한다.
     *
     * @param id 제거할 추적 대상 ID
     * @return 제거 성공 시 true, 해당 ID가 없으면 false
     */
    public boolean removeObject(int id) {
        return objects.remove(id) != null;
    }

    /**
     * 지정 ID의 TrackedObject를 반환한다.
     *
     * @param id 조회할 ID
     * @return 해당 TrackedObject, 없으면 null
     */
    public TrackedObject getObject(int id) {
        return objects.get(id);
    }

    /**
     * 지정 ID의 TrackedObject에 새 궤적 점을 추가한다.
     * 점이 누적될수록 예측 정확도가 향상된다.
     *
     * @param id    추적 대상 ID
     * @param point 추가할 위치 점
     * @throws IllegalArgumentException 해당 ID가 등록되지 않은 경우
     */
    public void addPointToObject(int id, Point point) {
        TrackedObject object = objects.get(id);
        if (object == null) throw new IllegalArgumentException("No object with id " + id);
        object.addPoint(point);
    }

    /**
     * 지정 ID의 추적 대상에 대해 다음 예측 위치를 계산한다.
     *
     * @param id 예측할 추적 대상 ID
     * @return 다음 예측 위치 Point
     * @throws IllegalArgumentException 등록되지 않은 ID인 경우
     * @throws IllegalStateException    데이터 점 수가 부족한 경우
     */
    public Point predictNextObject(int id) {
        TrackedObject object = requireObject(id);
        requireValid(object);
        return predictor.predictNext(object.getPoints());
    }

    /**
     * 지정 ID의 추적 대상에 대해 이전 예측 위치를 계산한다.
     *
     * @param id 예측할 추적 대상 ID
     * @return 이전 예측 위치 Point
     * @throws IllegalArgumentException 등록되지 않은 ID인 경우
     * @throws IllegalStateException    데이터 점 수가 부족한 경우
     */
    public Point predictPreviousObject(int id) {
        TrackedObject object = requireObject(id);
        requireValid(object);
        return predictor.predictPrevious(object.getPoints());
    }

    /**
     * 예측 가능한 모든 추적 대상에 대해 다음 예측 위치를 일괄 계산한다.
     * 데이터 부족 객체는 결과 맵에서 제외되며 예외가 발생하지 않는다.
     *
     * @return ID → 다음 예측 Point 매핑 (등록 순서 보존은 미보장 — HashMap 기반 objects)
     */
    public Map<Integer, Point> predictAllNext() {
        Map<Integer, Point> results = new LinkedHashMap<>();
        for (TrackedObject obj : objects.values()) {
            if (constraint.validate(obj.getPoints())) { // 조건 충족 객체만 예측
                results.put(obj.getId(), predictor.predictNext(obj.getPoints()));
            }
        }
        return results;
    }

    /**
     * 예측 가능한 모든 추적 대상에 대해 이전 예측 위치를 일괄 계산한다.
     * 데이터 부족 객체는 결과 맵에서 제외되며 예외가 발생하지 않는다.
     *
     * @return ID → 이전 예측 Point 매핑
     */
    public Map<Integer, Point> predictAllPrevious() {
        Map<Integer, Point> results = new LinkedHashMap<>();
        for (TrackedObject obj : objects.values()) {
            if (constraint.validate(obj.getPoints())) {
                results.put(obj.getId(), predictor.predictPrevious(obj.getPoints()));
            }
        }
        return results;
    }

    // ── 내부 가드 메서드 ──────────────────────────────────────────────────────

    /** ID로 TrackedObject를 가져오고, 없으면 IllegalArgumentException 발생. */
    private TrackedObject requireObject(int id) {
        TrackedObject obj = objects.get(id);
        if (obj == null) throw new IllegalArgumentException("No object with id " + id);
        return obj;
    }

    /** 제약조건 미충족 시 IllegalStateException 발생 (단일 ID 예측의 강제 게이트). */
    private void requireValid(TrackedObject obj) {
        if (!constraint.validate(obj.getPoints())) {
            throw new IllegalStateException(
                "Insufficient point data for prediction (id=" + obj.getId() + ").");
        }
    }
}
