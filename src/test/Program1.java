package test;

import GeoDL.Objectivity;
import GeoDL.Point;
import GeoDL.Position;
import GeoDL.Segment;
import GeoDL.StraightLine;

/**
 * Demo 1 — 점(Point)과 선(Line) 생명주기 시뮬레이터.
 *
 * 시연 목표:
 *   1. Point 객체를 독립적 정보(Information) 단위로 생성한다.
 *   2. 두 점으로부터 Segment(선분)와 StraightLine(무한 직선) 관계(Relation) 단위를 구성한다.
 *   3. Objectivity 인터페이스 계약(moveTo)으로 객체를 이동하며 캡슐화 장벽을 검증한다.
 *
 * GeoDL 도메인 철학 반영:
 *   "세상 만물은 정보와 관계로 나뉜다"
 *   — 점(Point)은 독립적 정보 단위, 선(Line/Segment/StraightLine)은 두 점으로 형성된 관계 단위.
 */
public class Program1 {

    public static void main(String[] args) {
        System.out.println("=== Demo 1: Point & Line Lifecycle Simulator ===\n");

        // ── 독립적 정보 단위 — Point 객체 3개 생성 ─────────────────────────────────
        // A(0,0): 원점, B(4,0): 수평 방향, C(0,3): 수직 방향
        Point a = new Point(new Position(0.0, 0.0));
        Point b = new Point(new Position(4.0, 0.0));
        Point c = new Point(new Position(0.0, 3.0));

        printPoint("A", a);
        printPoint("B", b);
        printPoint("C", c);

        // ── 관계 단위 구성 — 두 점으로 선 객체 생성 ──────────────────────────────
        Segment segment = new Segment(a, b);         // 선분 AB: 수평 (각도 0°)
        StraightLine line = new StraightLine(a, c);  // 무한 직선 AC: 수직 (각도 90°)

        System.out.println("\n[Segment AB] angle=" + String.format("%.2f", segment.getAngle()) + "°");
        System.out.println("[StraightLine AC] angle=" + String.format("%.2f", line.getAngle()) + "°");

        // ── Objectivity 계약으로 점 이동 — 캡슐화 검증 ────────────────────────────
        // Objectivity 인터페이스 타입으로만 참조하여 moveTo() 계약을 확인
        System.out.println("\n--- Moving point A to (1.0, 1.0) via Objectivity.moveTo() ---");
        Objectivity objA = a;
        objA.moveTo(new Position(1.0, 1.0)); // Point.moveTo() → 내부 position 갱신
        printPoint("A (after move)", a);

        // 선분(Segment) 이동: moveTo()는 point[0] 기준 평행 이동
        // A, B 두 점 모두 이동 벡터만큼 함께 이동하여 선분의 형태를 유지한다
        System.out.println("\n--- Moving Segment AB to origin (0,0) via Objectivity.moveTo() ---");
        Objectivity objSeg = segment;
        objSeg.moveTo(new Position(0.0, 0.0));
        printPoint("A (after segment move)", a); // A는 (0,0)으로 이동
        printPoint("B (after segment move)", b); // B는 A와 동일한 벡터만큼 이동됨

        System.out.println("\n=== Demo 1 Complete ===");
    }

    /** 점 레이블과 좌표를 포맷하여 출력하는 유틸리티 메서드. */
    private static void printPoint(String label, Point p) {
        System.out.printf("[Point %s] x=%.2f, y=%.2f%n",
            label, p.getPosition().getX(), p.getPosition().getY());
    }
}
