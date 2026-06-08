package Func3;

import GeoDL.Point;
import GeoDL.Position;
import java.util.List;

/**
 * OLS(최소자승법) 기반 4단계 선형 외삽 예측 알고리즘 구현체.
 *
 * 알고리즘 개요 (4단계):
 *
 *   [Step 1 — OLS 선형 회귀]
 *     인덱스 i를 독립변수, 각 점의 x·y를 종속변수로 삼아 OLS를 적용.
 *     결과로 궤적의 대표 방향벡터(단위벡터 dirX, dirY)를 도출한다.
 *     수식:
 *       slopeX = (n·ΣiXi − ΣiΣXi) / (n·Σi² − (Σi)²)
 *       slopeY = (n·ΣiYi − ΣiΣYi) / (n·Σi² − (Σi)²)
 *     분모(denom) ≈ 0인 퇴화 케이스: 마지막점 - 첫점 벡터로 대체.
 *
 *   [Step 2 — 평균 변위 계산]
 *     연속 점 사이의 유클리드 거리 합을 구간 수(n-1)로 나눈 meanStep.
 *     meanStep = Σ|p[i+1] - p[i]| / (n-1)
 *
 *   [Step 3 — 기준선 연장]
 *     무게중심(centroidX, centroidY)에서 방향벡터를 따라 n × meanStep 거리만큼 앞뒤로 연장.
 *     extension = n × meanStep
 *     prevX/Y = centroid ± dir × extension (마이너스 방향)
 *     nextX/Y = centroid ± dir × extension (플러스 방향)
 *
 *   [Step 4 — 결과 매핑]
 *     후방 끝점(prev*) → predictPrevious 결과
 *     전방 끝점(next*) → predictNext 결과
 *
 * Func3_new와의 차이점 (보호 자산 참고):
 *   Func3_new는 마지막 두 점의 차이(Last-Delta)를 사용한 O(1) 외삽.
 *   이 구현은 OLS 회귀를 사용한 O(n) 배치 외삽 — 더 많은 점일수록 안정적인 방향 추정.
 */
public class LinearExtrapolationPredictor implements Predictor {

    /**
     * 궤적 수열에서 다음 예측 위치를 반환한다.
     * extrapolate() 결과 배열의 인덱스 2,3 (nextX, nextY)을 사용.
     */
    @Override
    public Point predictNext(List<Point> points) {
        validateInput(points);
        double[] result = extrapolate(points);
        return new Point(new Position(result[2], result[3])); // result[2]=nextX, result[3]=nextY
    }

    /**
     * 궤적 수열에서 이전 예측 위치를 반환한다.
     * extrapolate() 결과 배열의 인덱스 0,1 (prevX, prevY)을 사용.
     */
    @Override
    public Point predictPrevious(List<Point> points) {
        validateInput(points);
        double[] result = extrapolate(points);
        return new Point(new Position(result[0], result[1])); // result[0]=prevX, result[1]=prevY
    }

    /**
     * 4단계 OLS 외삽 핵심 알고리즘.
     *
     * @param points 시간순 궤적 점 수열 (최소 2개)
     * @return double[4] { prevX, prevY, nextX, nextY }
     */
    private double[] extrapolate(List<Point> points) {
        int n = points.size();

        // ── Step 1: OLS 선형 회귀 ─────────────────────────────────────────────────
        // 누산 변수: 인덱스 i, 좌표 xi/yi에 대한 합산
        double sumI = 0, sumX = 0, sumY = 0, sumI2 = 0, sumIX = 0, sumIY = 0;
        for (int i = 0; i < n; i++) {
            double xi = points.get(i).getPosition().getX();
            double yi = points.get(i).getPosition().getY();
            sumI  += i;           // Σi
            sumX  += xi;          // ΣXi
            sumY  += yi;          // ΣYi
            sumI2 += (double) i * i;   // Σi²
            sumIX += (double) i * xi;  // ΣiXi
            sumIY += (double) i * yi;  // ΣiYi
        }
        // OLS 분모: n·Σi² − (Σi)² — 이 값이 0이면 모든 점이 같은 인덱스(퇴화 케이스)
        double denom = n * sumI2 - sumI * sumI;

        // 무게중심: 모든 점의 평균 위치 (Step 3에서 기준점으로 사용)
        double centroidX = sumX / n;
        double centroidY = sumY / n;

        double dirX, dirY;
        if (Math.abs(denom) < 1e-12) {
            // 퇴화 케이스: 분모 ≈ 0 → 첫점→마지막점 벡터로 대체
            double dx = points.get(n - 1).getPosition().getX() - points.get(0).getPosition().getX();
            double dy = points.get(n - 1).getPosition().getY() - points.get(0).getPosition().getY();
            double len = Math.sqrt(dx * dx + dy * dy);
            dirX = (len < 1e-12) ? 1.0 : dx / len; // 길이 0 방어: 기본 방향 (1, 0)
            dirY = (len < 1e-12) ? 0.0 : dy / len;
        } else {
            // 정상 OLS 기울기 계산 후 단위벡터로 정규화
            double slopeX = (n * sumIX - sumI * sumX) / denom;
            double slopeY = (n * sumIY - sumI * sumY) / denom;
            double len = Math.sqrt(slopeX * slopeX + slopeY * slopeY);
            dirX = (len < 1e-12) ? 1.0 : slopeX / len;
            dirY = (len < 1e-12) ? 0.0 : slopeY / len;
        }

        // ── Step 2: 평균 변위 계산 ───────────────────────────────────────────────
        // 연속 점 쌍 사이의 유클리드 거리를 합산한 후 구간 수(n-1)로 나눔
        double totalDist = 0;
        for (int i = 0; i < n - 1; i++) {
            double dx = points.get(i + 1).getPosition().getX() - points.get(i).getPosition().getX();
            double dy = points.get(i + 1).getPosition().getY() - points.get(i).getPosition().getY();
            totalDist += Math.sqrt(dx * dx + dy * dy);
        }
        double meanStep = totalDist / (n - 1); // 평균 보폭

        // ── Step 3: 무게중심에서 n×meanStep 거리만큼 전후 연장 ─────────────────
        double extension = n * meanStep; // 연장 거리 (점 수에 비례)
        double prevX = centroidX - dirX * extension; // 후방(과거 방향)
        double prevY = centroidY - dirY * extension;
        double nextX = centroidX + dirX * extension; // 전방(미래 방향)
        double nextY = centroidY + dirY * extension;

        // ── Step 4: 결과 배열 반환 { prevX, prevY, nextX, nextY } ───────────────
        return new double[]{prevX, prevY, nextX, nextY};
    }

    /**
     * 입력 유효성 검사: null이거나 2점 미만이면 예외 발생.
     * OLS는 최소 2점(인덱스 0, 1)이 있어야 기울기 계산이 가능하다.
     */
    private void validateInput(List<Point> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("At least 2 points are required for linear extrapolation.");
        }
    }
}
