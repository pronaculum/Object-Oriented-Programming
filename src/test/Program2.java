package test;

import Func2.GeoManager;
import GeoDL.AngleResult;
import GeoDL.IntersectionResult;
import GeoDL.Point;
import GeoDL.Position;
import GeoDL.RelationResult;
import GeoDL.Segment;

import java.util.List;

/**
 * Demo 2 — 교차점(Intersection) 및 각도(Angle) 추적기.
 *
 * 시연 목표:
 *   1. 알려진 각도의 두 선분(Segment) 객체를 생성한다.
 *   2. GeoManager.relate()를 단일 진입점으로 호출한다 (더블 디스패치 없음).
 *   3. 반환된 리스트에서 IntersectionResult와 AngleResult를 instanceof로 추출한다.
 *
 * 설계 포인트:
 *   호출자는 GeoManager 내부 알고리즘 구조를 알 필요 없이
 *   relate() 하나로 4대 관계 중 적용 가능한 모든 결과를 받는다.
 *   결과 타입 분기도 instanceof 체인으로만 수행 — Visitor 패턴 없음.
 */
public class Program2 {

    public static void main(String[] args) {
        System.out.println("=== Demo 2: Intersection & Angle Tracker ===\n");

        // ── 선분 A: (-3, 0) → (3, 0) — 수평 선분 (각도 0°) ────────────────────────
        Point a1 = new Point(new Position(-3.0, 0.0));
        Point a2 = new Point(new Position(3.0, 0.0));
        Segment segA = new Segment(a1, a2);

        // ── 선분 B: (0, -3) → (0, 3) — 수직 선분 (각도 90°, segA와 직교) ────────────
        Point b1 = new Point(new Position(0.0, -3.0));
        Point b2 = new Point(new Position(0.0, 3.0));
        Segment segB = new Segment(b1, b2);

        System.out.println("[SegmentA] from (-3,0) to (3,0)  angle=" + String.format("%.1f", segA.getAngle()) + "°");
        System.out.println("[SegmentB] from (0,-3) to (0,3)  angle=" + String.format("%.1f", segB.getAngle()) + "°");

        // ── GeoManager: 단일 진입점으로 관계 계산 ─────────────────────────────────
        // segA ∩ segB → 교차점 (0, 0) 및 사잇각 90° 예상
        List<RelationResult> results = GeoManager.relate(segA, segB);

        System.out.println("\n--- GeoManager.relate(segA, segB) results ---");
        for (RelationResult r : results) {
            if (r instanceof IntersectionResult) {
                IntersectionResult ir = (IntersectionResult) r;
                if (ir.isIntersecting()) {
                    // 교차 시 교점 좌표 출력
                    System.out.printf("[INTERSECTION] Contact: (%.2f, %.2f)%n",
                        ir.getContactPoint().getX(), ir.getContactPoint().getY());
                } else {
                    System.out.println("[INTERSECTION] No intersection.");
                }
            } else if (r instanceof AngleResult) {
                // 두 선 방향 사이의 예각 출력
                System.out.printf("[ANGLE] Between segments: %.2f°%n",
                    ((AngleResult) r).getAngleDegrees());
            } else {
                System.out.println(r); // 기타 결과 타입 (ParallelResult, CoincidenceResult)
            }
        }

        // ── 비교차 케이스 검증 — segA(x:-3~3, y=0)와 segC(x:5~8, y=0) ─────────────
        // 두 선분이 같은 y=0 직선 위이지만 x 범위가 겹치지 않아 교차 없음
        System.out.println("\n--- Testing non-crossing segments ---");
        Point c1 = new Point(new Position(5.0, 0.0));
        Point c2 = new Point(new Position(8.0, 0.0));
        Segment segC = new Segment(c1, c2);

        List<RelationResult> results2 = GeoManager.relate(segA, segC);
        for (RelationResult r : results2) {
            if (r instanceof IntersectionResult) {
                IntersectionResult ir = (IntersectionResult) r;
                System.out.println("[INTERSECTION] intersects=" + ir.isIntersecting()); // false 예상
            } else if (r instanceof AngleResult) {
                System.out.printf("[ANGLE] %.2f°%n", ((AngleResult) r).getAngleDegrees()); // 0° 예상 (평행)
            }
        }

        System.out.println("\n=== Demo 2 Complete ===");
    }
}
