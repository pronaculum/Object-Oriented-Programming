package Func3;

import GeoDL.Point;
import java.util.List;

/**
 * 궤적 예측 알고리즘의 계약 인터페이스.
 *
 * GeoDL 도메인 철학에서 점(Point)은 독립적 정보 단위이며,
 * 시간 축 위에 배열된 점의 수열(List<Point>)이 하나의 '궤적 정보'를 형성한다.
 * Predictor는 그 궤적 수열을 분석하여 미래(NEXT)와 과거(PREVIOUS) 위치를 추론한다.
 *
 * 구현 책임:
 *   - predictNext()    : 수열의 진행 방향을 연장한 다음 예측점 반환 (미래 예측)
 *   - predictPrevious(): 수열의 역방향으로 연장한 이전 예측점 반환 (과거 역추적)
 *
 * 확장 설계:
 *   이 인터페이스를 구현하면 알고리즘 교체가 쉽다.
 *   현재 구현체: LinearExtrapolationPredictor (OLS 4단계 알고리즘)
 *   PredictionManager는 Predictor 인터페이스 타입으로만 참조하므로
 *   구현체 교체 시 PredictionManager 코드를 수정하지 않아도 된다 (OCP 준수).
 *
 * 입력 제약:
 *   최소 2개 이상의 Point를 포함해야 한다.
 *   단일 점으로는 방향을 결정할 수 없으므로 구현체는 IllegalArgumentException을 던져야 한다.
 */
public interface Predictor {
    /**
     * 주어진 점 수열의 진행 방향을 외삽하여 다음 예측 위치를 반환한다.
     *
     * @param points 시간순으로 정렬된 궤적 점 수열 (최소 2개)
     * @return 다음 예측 위치를 나타내는 새 Point 객체
     */
    Point predictNext(List<Point> points);

    /**
     * 주어진 점 수열의 역방향으로 외삽하여 이전 예측 위치를 반환한다.
     *
     * @param points 시간순으로 정렬된 궤적 점 수열 (최소 2개)
     * @return 이전 예측 위치를 나타내는 새 Point 객체
     */
    Point predictPrevious(List<Point> points);
}
