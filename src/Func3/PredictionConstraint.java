package Func3;

import GeoDL.Point;
import java.util.List;

/**
 * 예측 알고리즘 실행 가능 여부를 판별하는 입력 유효성 제약조건.
 *
 * 역할:
 *   PredictionManager가 예측 함수를 호출하기 전에 TrackedObject의 궤적 데이터가
 *   최소 요건을 충족하는지 사전 검사하는 게이트키퍼(Gatekeeper) 역할을 한다.
 *
 * 설계 의도:
 *   - minimumPoints를 생성자 매개변수로 외부에서 주입 — 알고리즘마다 다른 최소 점 수를 설정 가능.
 *   - LinearExtrapolationPredictor는 OLS 계산을 위해 최소 2점이 필요하므로
 *     PredictionManager 생성 시 new PredictionConstraint(2)로 사용하는 것이 기본 설정.
 *
 * validate() 반환값 의미:
 *   - true : 예측 가능 → predictor.predict*() 호출 허용
 *   - false: 데이터 부족 → 예측 건너뜀 (predictAll*에서) 또는 예외 발생 (단일 ID 예측에서)
 */
public class PredictionConstraint {

    /** 예측 수행을 위한 최소 궤적 점 수 (OLS 기준: 2점 이상 필요) */
    private final int minimumPoints;

    /**
     * @param minimumPoints 예측 허용을 위한 최소 점 수 (통상 2 이상)
     */
    public PredictionConstraint(int minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    /**
     * 주어진 점 수열이 최소 요건을 충족하는지 확인한다.
     *
     * @param points 검사할 궤적 점 수열 (null이면 자동으로 false 반환)
     * @return true이면 예측 수행 가능, false이면 데이터 부족
     */
    public boolean validate(List<Point> points) {
        return points != null && points.size() >= minimumPoints;
    }
}
