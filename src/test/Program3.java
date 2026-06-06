package test;

import Func2.GeoManager;
import GeoDL.CoincidenceResult;
import GeoDL.ParallelResult;
import GeoDL.Point;
import GeoDL.Position;
import GeoDL.RelationResult;
import GeoDL.StraightLine;

import java.util.List;

/**
 * Demo 3 — 평행(Parallel) 및 일치(Coincidence) 검증기.
 *
 * 시연 목표:
 *   1. 같은 기울기, 다른 y절편 → 평행(Parallel) 상태 유도 및 검증.
 *   2. 같은 직선 위의 서로 다른 원점 → 일치(Coincidence) 상태 유도 및 검증.
 *   3. 다른 기울기 → 교차(Intersection) 상태 및 평행·일치 모두 false 검증.
 *
 * GeoManager 동작:
 *   Line×Line 조합에서는 항상 IntersectionResult + ParallelResult + CoincidenceResult + AngleResult
 *   4종 결과를 반환한다.
 *   - 일치(Coincidence)이면: Parallel=false, Coincidence=true, Intersection=true
 *   - 평행(Parallel)이면:    Parallel=true, Coincidence=false, Intersection=false
 *   - 교차(Intersection)이면: Parallel=false, Coincidence=false, Intersection=true
 */
public class Program3 {

    public static void main(String[] args) {
        System.out.println("=== Demo 3: Parallel & Coincidence Validator ===\n");

        // ── 케이스 1: 평행 (같은 기울기 0°, 다른 y절편) ───────────────────────────
        // line1: y=0 (x축), line2: y=2 (y축에서 2만큼 위)
        Point p1 = new Point(new Position(0.0, 0.0));
        Point d1 = new Point(new Position(1.0, 0.0));
        StraightLine line1 = new StraightLine(p1, d1); // 방향: (1,0) — 수평

        Point p2 = new Point(new Position(0.0, 2.0));
        Point d2 = new Point(new Position(1.0, 2.0));
        StraightLine line2 = new StraightLine(p2, d2); // 방향: (1,0) — 수평, y=2

        System.out.println("--- Parallel test (same slope, different intercept) ---");
        printRelation(GeoManager.relate(line1, line2)); // ParallelResult=true 예상

        // ── 케이스 2: 일치 (같은 직선, 서로 다른 원점) ───────────────────────────
        // line3: (0,0)→(1,0), line4: (5,0)→(6,0) — 모두 y=0 수평선 위
        Point p3 = new Point(new Position(0.0, 0.0));
        Point d3 = new Point(new Position(1.0, 0.0));
        StraightLine line3 = new StraightLine(p3, d3);

        Point p4 = new Point(new Position(5.0, 0.0));
        Point d4 = new Point(new Position(6.0, 0.0));
        StraightLine line4 = new StraightLine(p4, d4); // 같은 직선, 원점만 다름

        System.out.println("\n--- Coincidence test (same line, different origin points) ---");
        printRelation(GeoManager.relate(line3, line4)); // CoincidenceResult=true 예상

        // ── 케이스 3: 교차 (다른 기울기) ─────────────────────────────────────────
        // line1: 수평(0°), line5: 45° 방향 — 원점(0,0)에서 교차
        Point p5 = new Point(new Position(0.0, 0.0));
        Point d5 = new Point(new Position(1.0, 1.0));
        StraightLine line5 = new StraightLine(p5, d5); // 방향: (1,1) — 45°

        System.out.println("\n--- Intersection test (different slopes) ---");
        printRelation(GeoManager.relate(line1, line5)); // IntersectionResult=true, Parallel=false, Coincidence=false 예상

        System.out.println("\n=== Demo 3 Complete ===");
    }

    /**
     * RelationResult 리스트에서 Parallel과 Coincidence 결과만 추출하여 출력한다.
     * 기타 결과 타입(IntersectionResult, AngleResult)은 toString()으로 출력.
     */
    private static void printRelation(List<RelationResult> results) {
        for (RelationResult r : results) {
            if (r instanceof ParallelResult) {
                System.out.println("  [PARALLEL] parallel=" + ((ParallelResult) r).isParallel());
            } else if (r instanceof CoincidenceResult) {
                System.out.println("  [COINCIDENCE] coincident=" + ((CoincidenceResult) r).isCoincident());
            } else {
                System.out.println("  " + r); // IntersectionResult, AngleResult 등 toString 출력
            }
        }
    }
}
