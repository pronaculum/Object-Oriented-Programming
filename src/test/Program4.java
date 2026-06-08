package test;

import Func3.LinearExtrapolationPredictor;
import Func3.PredictionConstraint;
import Func3.PredictionManager;
import Func3.TrackedObject;
import GeoDL.Point;
import GeoDL.Position;

/**
 * Demo 4 — OLS 선형 외삽 궤적 예측기.
 *
 * 시연 목표:
 *   1. PredictionManager에 과거 궤적 점(≥5개)을 스트림으로 주입한다.
 *   2. 4단계 LinearExtrapolationPredictor OLS 알고리즘을 실행한다.
 *   3. 추정된 이전(PREVIOUS) 위치와 다음(NEXT) 위치를 출력하여 검증한다.
 *
 * 검증 데이터 설계:
 *   등속 이동 (+2, +1) 방향: (0,0) → (2,1) → (4,2) → (6,3) → (8,4) → (10,5)
 *   이론값:
 *     - NEXT  ≈ (12.0, 6.0)  [+2/+1 방향 연장]
 *     - PREV  ≈ (-2.0, -1.0) [역방향 연장]
 *   실제값은 OLS 회귀와 extension = n × meanStep 계산으로 결정되므로 근사값이 허용 범위 내이면 통과.
 */
public class Program4 {

    public static void main(String[] args) {
        System.out.println("=== Demo 4: Linear Flow Trajectory Predictor ===\n");

        // ── 과거 궤적 데이터: (+2, +1) 등속 이동 6개 점 ─────────────────────────
        double[][] pastCoords = {
            { 0.0,  0.0},
            { 2.0,  1.0},
            { 4.0,  2.0},
            { 6.0,  3.0},
            { 8.0,  4.0},
            {10.0,  5.0}
        };

        // PredictionManager 조립: OLS 예측기 + 최소 2점 제약
        PredictionManager manager = new PredictionManager(
            new LinearExtrapolationPredictor(),
            new PredictionConstraint(2)
        );

        // TrackedObject(id=1)에 과거 궤적 누적
        TrackedObject obj = new TrackedObject(1);
        System.out.println("Past trajectory:");
        for (double[] coord : pastCoords) {
            Point p = new Point(new Position(coord[0], coord[1]));
            obj.addPoint(p);
            System.out.printf("  Point(%.1f, %.1f)%n", coord[0], coord[1]);
        }
        manager.addObject(obj);

        // ── OLS 4단계 알고리즘으로 이전/다음 위치 예측 ──────────────────────────
        Point predictedNext = manager.predictNextObject(1);  // 미래 위치 예측
        Point predictedPrev = manager.predictPreviousObject(1); // 과거 역추적

        System.out.printf("%nPredicted PREVIOUS: (%.4f, %.4f)%n",
            predictedPrev.getPosition().getX(), predictedPrev.getPosition().getY());
        System.out.printf("Predicted NEXT:     (%.4f, %.4f)%n",
            predictedNext.getPosition().getX(), predictedNext.getPosition().getY());

        // 기댓값 안내: 실제 계산 결과가 이 범위 근처이면 알고리즘 정상 동작 확인
        System.out.println("\n[Expected: direction (+2,+1) normalized, extension ≈ n*meanStep each side]");
        System.out.println("Ideal next  ≈ (12.0, 6.0), ideal prev ≈ (-2.0, -1.0) [approximate]");

        System.out.println("\n=== Demo 4 Complete ===");
    }
}
