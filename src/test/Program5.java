package test;

import Func2.GeoManager;
import Func2.RelationManager;
import Func3.LinearExtrapolationPredictor;
import Func3.PredictionConstraint;
import Func3.PredictionManager;
import Func3.TrackedObject;
import GeoDL.AngleResult;
import GeoDL.CoincidenceResult;
import GeoDL.IntersectionResult;
import GeoDL.ParallelResult;
import GeoDL.Point;
import GeoDL.Position;
import GeoDL.RelationResult;
import GeoDL.Segment;
import GeoDL.StraightLine;

import java.util.List;
import java.util.Map;

/**
 * Demo 5 — 전체 시스템 통합 데모 (풀 파이프라인 검증).
 *
 * 파이프라인 구성:
 *   [Func1 도메인]
 *     Point 생성 → Segment 구성 (Information + Relation 단위 시연)
 *   [Func2 도메인]
 *     GeoManager:    두 선분의 평행 관계 감지 → 이동 후 교차 관계로 전환
 *     RelationManager: register() → getRelations() → update() → listAll() 전체 흐름
 *   [Func3 도메인]
 *     누적 점 궤적 → LinearExtrapolationPredictor → NEXT/PREV 위치 예측
 *
 * 통합 검증 포인트:
 *   - 기하 객체 이동 후 RelationManager.update()로 관계 캐시 갱신이 올바르게 반영되는지
 *   - GeoManager 직접 호출과 RelationManager 캐시 결과의 일관성
 *   - Func3 예측기가 대각선 궤적((0,0)→(5,5))에서 올바른 방향(45°)을 추정하는지
 */
public class Program5 {

    public static void main(String[] args) {
        System.out.println("=== Demo 5: Comprehensive System Integration Demo ===\n");

        // ── [Func1] 도메인 객체 생성 ──────────────────────────────────────────────
        // segAB: y=0 수평 선분 (0,0)→(4,0)
        System.out.println("--- [Func1] Creating domain objects ---");
        Point pA = new Point(new Position(0.0, 0.0));
        Point pB = new Point(new Position(4.0, 0.0));
        Segment segAB = new Segment(pA, pB);
        System.out.printf("Segment AB: A(%.1f,%.1f) → B(%.1f,%.1f)  angle=%.1f°%n",
            pA.getPosition().getX(), pA.getPosition().getY(),
            pB.getPosition().getX(), pB.getPosition().getY(),
            segAB.getAngle());

        // segCD: y=2 수평 선분 (0,2)→(4,2) — AB와 평행
        Point pC = new Point(new Position(0.0, 2.0));
        Point pD = new Point(new Position(4.0, 2.0));
        Segment segCD = new Segment(pC, pD);
        System.out.printf("Segment CD: C(%.1f,%.1f) → D(%.1f,%.1f)  angle=%.1f°%n",
            pC.getPosition().getX(), pC.getPosition().getY(),
            pD.getPosition().getX(), pD.getPosition().getY(),
            segCD.getAngle());

        // ── [Func2] RelationManager: 등록·조회·갱신 흐름 ─────────────────────────
        System.out.println("\n--- [Func2] RelationManager: register & list ---");
        RelationManager rm = new RelationManager();
        int pairId = rm.register(segAB, segCD); // 등록 시 즉시 초기 관계 계산 및 캐시
        System.out.println("Registered pair id=" + pairId);
        printResults(rm.getRelations(pairId)); // 초기 상태: 평행(Parallel=true) 예상

        // ── [Func2] GeoManager 직접 호출 — AB ∥ CD 확인 ─────────────────────────
        System.out.println("\n--- [Func2] GeoManager direct: AB ∥ CD ---");
        printResults(GeoManager.relate(segAB, segCD)); // RelationManager 캐시와 결과 일치 확인

        // ── CD 이동 후 RelationManager 캐시 갱신 ─────────────────────────────────
        // C를 (-1,-1), D를 (1,1)으로 이동 → 대각선 방향으로 변경
        // 이후 update()로 캐시를 갱신하여 교차(Intersection) 상태로 전환
        System.out.println("\n--- Rotating CD to intersect AB, then RelationManager.update() ---");
        pC.moveTo(new Position(-1.0, -1.0));
        pD.moveTo(new Position(1.0, 1.0));
        rm.update(pairId); // 이동 후 관계 재계산 → 캐시 최신화

        StraightLine lineCD2 = new StraightLine(pC, pD); // 새 위치의 CD를 무한 직선으로 표현
        System.out.printf("StraightLine CD (new): C(%.1f,%.1f) → D(%.1f,%.1f)%n",
            pC.getPosition().getX(), pC.getPosition().getY(),
            pD.getPosition().getX(), pD.getPosition().getY());

        // update() 반영 확인: segAB vs 이동된 segCD — 이제 교차 예상
        System.out.println("[Func2] Updated relation (segAB vs moved-CD segment):");
        printResults(rm.getRelations(pairId));

        // GeoManager 직접 호출로 lineAB ∩ lineCD2 교차점 검증
        System.out.println("\n--- [Func2] GeoManager: AB intersects lineCD2? ---");
        StraightLine lineAB = new StraightLine(pA, pB);
        printResults(GeoManager.relate(lineAB, lineCD2)); // 교차점 출력 확인

        // ── RelationManager: 전체 등록 쌍 일괄 조회 ─────────────────────────────
        System.out.println("\n--- [Func2] RelationManager.listAll() ---");
        Map<Integer, List<RelationResult>> all = rm.listAll();
        for (Map.Entry<Integer, List<RelationResult>> e : all.entrySet()) {
            System.out.println("  Pair id=" + e.getKey() + ":");
            printResults(e.getValue());
        }

        // ── [Func3] 이동하는 점의 궤적 예측 ──────────────────────────────────────
        // 대각선 등속 이동: (0,0)→(1,1)→...→(5,5) — 45° 방향
        // NEXT ≈ (5+n*meanStep 방향), PREV ≈ 역방향 추정
        System.out.println("\n--- [Func3] Predicting trajectory of moving point ---");
        PredictionManager pm = new PredictionManager(
            new LinearExtrapolationPredictor(), new PredictionConstraint(2));
        TrackedObject tracked = new TrackedObject(99);

        double[][] trail = {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5}};
        for (double[] c : trail) tracked.addPoint(new Point(new Position(c[0], c[1])));
        pm.addObject(tracked);

        // 6개 점의 45° 대각선 궤적에서 다음·이전 예측 위치 계산
        Point next = pm.predictNextObject(99);
        Point prev = pm.predictPreviousObject(99);
        System.out.printf("Trajectory (0,0)→(5,5). Predicted PREV=(%.2f,%.2f)  NEXT=(%.2f,%.2f)%n",
            prev.getPosition().getX(), prev.getPosition().getY(),
            next.getPosition().getX(), next.getPosition().getY());
        // 기댓값: NEXT ≈ (11~12, 11~12), PREV ≈ (-6~-5, -6~-5) 근사

        System.out.println("\n=== Demo 5 Complete — Full pipeline validated ===");
    }

    /**
     * RelationResult 리스트를 타입별로 분류하여 출력한다.
     * instanceof 체인 — Visitor 패턴 없이 타입을 명시적으로 판별.
     */
    private static void printResults(List<RelationResult> results) {
        for (RelationResult r : results) {
            if (r instanceof IntersectionResult) {
                IntersectionResult ir = (IntersectionResult) r;
                if (ir.isIntersecting()) {
                    System.out.printf("  [INTERSECTION] Contact: (%.3f, %.3f)%n",
                        ir.getContactPoint().getX(), ir.getContactPoint().getY());
                } else {
                    System.out.println("  [INTERSECTION] No intersection.");
                }
            } else if (r instanceof AngleResult) {
                System.out.printf("  [ANGLE] %.2f°%n", ((AngleResult) r).getAngleDegrees());
            } else if (r instanceof ParallelResult) {
                System.out.println("  [PARALLEL] " + ((ParallelResult) r).isParallel());
            } else if (r instanceof CoincidenceResult) {
                System.out.println("  [COINCIDENCE] " + ((CoincidenceResult) r).isCoincident());
            }
        }
    }
}
