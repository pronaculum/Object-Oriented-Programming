package Func3;

import GeoDL.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 단일 추적 대상 객체의 궤적 데이터를 보관하는 불변 ID 컨테이너.
 *
 * 구성:
 *   - id     : 추적 대상의 고유 식별자. 생성 시 주입되며 이후 변경 불가(final).
 *   - points : 시간순으로 축적된 위치 점 수열 (ArrayList — 동적 추가 허용).
 *
 * 캡슐화 전략:
 *   getPoints()는 Collections.unmodifiableList()로 래핑된 읽기 전용 뷰를 반환한다.
 *   외부 코드가 리스트를 직접 수정하지 못하도록 보호하면서도
 *   추가(addPoint)는 내부 메서드로만 허용하여 데이터 무결성을 보장한다.
 *
 * null 방어:
 *   addPoint()에서 null 포인트를 무시(silent ignore)하여
 *   비어있는 예측 불가 상태를 방지한다.
 *
 * 궤적 누적 패턴:
 *   PredictionManager.addPointToObject(id, point)를 통해 외부에서 점을 추가하고,
 *   points가 minimumPoints 이상 쌓이면 Predictor가 예측을 수행한다.
 */
public class TrackedObject {

    /** 추적 대상의 불변 고유 ID */
    private final int id;
    /** 시간순으로 축적된 궤적 점 수열 (내부 가변 저장, 외부 읽기 전용 노출) */
    private final List<Point> points;

    /**
     * @param id 추적 대상의 고유 ID (PredictionManager가 관리하는 키와 일치해야 한다)
     */
    public TrackedObject(int id) {
        this.id     = id;
        this.points = new ArrayList<>();
    }

    /** @return 이 추적 대상의 고유 ID */
    public int getId() { return id; }

    /**
     * 현재 누적된 궤적 점 수열을 읽기 전용으로 반환한다.
     * Predictor는 이 리스트를 바탕으로 예측을 수행한다.
     *
     * @return 읽기 전용 점 수열 (unmodifiableList)
     */
    public List<Point> getPoints() { return Collections.unmodifiableList(points); }

    /**
     * 궤적 수열에 새 위치 점을 추가한다.
     * null이 들어오면 무시(silent ignore)하여 빈 데이터 방어.
     *
     * @param point 추가할 위치 점 (null이면 무시)
     */
    public void addPoint(Point point) {
        if (point != null) points.add(point);
    }
}
