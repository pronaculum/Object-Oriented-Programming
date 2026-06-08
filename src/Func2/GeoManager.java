package Func2;

import GeoDL.AngleResult;
import GeoDL.CoincidenceResult;
import GeoDL.IntersectionResult;
import GeoDL.Line;
import GeoDL.Objectivity;
import GeoDL.ParallelResult;
import GeoDL.Point;
import GeoDL.Position;
import GeoDL.RelationResult;
import GeoDL.Segment;
import GeoDL.StraightLine;

import java.util.ArrayList;
import java.util.List;

/**
 * 모든 기하 객체 간 관계 연산의 단일 진입점(Single Entry-Point).
 *
 * Epic 5 재배치:
 *   기존 GeoDL.core에 있던 GeoManager를 Func2 패키지로 이관.
 *   관계 연산과 관계 관리 책임을 GeoDL 도메인에서 분리하여 패키지 단일화 원칙을 준수.
 *
 * 핵심 설계 원칙 (instructions.md §0 준수):
 *   더블 디스패치(Visitor 패턴) 절대 금지.
 *   모든 런타임 타입 판별은 relate()와 하위 헬퍼 메서드 내의 명시적 instanceof 체인으로만 수행.
 *   이는 팀이 합의한 "직관적 조건문 판별 구조"이며, 어떠한 accept()/visit() 우회 도입도 차단.
 *
 * 지원하는 4대 관계 출력:
 *   1. IntersectionResult  — 두 객체의 교차 여부 및 교점
 *   2. AngleResult         — 두 객체 방향 사이의 각도
 *   3. ParallelResult      — 두 선의 방향 평행 여부 (일치 제외)
 *   4. CoincidenceResult   — 두 객체의 위상적 일치(완전 겹침) 여부
 *
 * 지원 타입 조합:
 *   - Point × Point  → IntersectionResult + CoincidenceResult
 *   - Point × Line   → IntersectionResult + AngleResult
 *   - Line  × Line   → IntersectionResult + ParallelResult + CoincidenceResult + AngleResult
 */
public class GeoManager {

    /** 부동소수점 방향 벡터 비교 허용 오차 */
    private static final double EPSILON = 1e-9;

    /** 유틸리티 클래스 — 인스턴스 생성 불가 */
    private GeoManager() {}

    /**
     * obj1과 obj2 사이의 모든 적용 가능한 기하학적 관계를 계산한다.
     *
     * @param obj1 첫 번째 기하 객체 (Point, Segment, StraightLine)
     * @param obj2 두 번째 기하 객체
     * @return 하나 이상의 RelationResult를 담은 리스트 (null 또는 빈 리스트 반환 없음)
     * @throws IllegalArgumentException null 입력 또는 지원되지 않는 타입 조합인 경우
     */
    public static List<RelationResult> relate(Objectivity obj1, Objectivity obj2) {
        if (obj1 == null || obj2 == null) {
            throw new IllegalArgumentException("Objectivity objects must not be null.");
        }

        List<RelationResult> results = new ArrayList<>();

        // 타입 조합별 분기: 더블 디스패치 없이 명시적 instanceof 체인으로 판별
        if (obj1 instanceof Point && obj2 instanceof Point) {
            relatePointPoint(results, (Point) obj1, (Point) obj2);

        } else if (obj1 instanceof Line && obj2 instanceof Line) {
            relateLineLine(results, (Line) obj1, (Line) obj2);

        } else if (obj1 instanceof Point && obj2 instanceof Line) {
            relatePointLine(results, (Point) obj1, (Line) obj2);

        } else if (obj1 instanceof Line && obj2 instanceof Point) {
            // 인자 순서를 바꿔서 동일한 로직 재사용 (교환법칙 대칭성 보장)
            relatePointLine(results, (Point) obj2, (Line) obj1);

        } else {
            throw new IllegalArgumentException(
                "Unsupported type combination: "
                + obj1.getClass().getSimpleName() + " and "
                + obj2.getClass().getSimpleName());
        }

        return results;
    }

    // ── Point ∩ Point ──────────────────────────────────────────────────────────

    /**
     * 두 점의 거리 제곱이 EPSILON² 미만이면 일치(교차) 판정.
     * sqrt 호출을 생략하여 성능 최적화.
     */
    private static void relatePointPoint(List<RelationResult> out, Point p1, Point p2) {
        double x1 = p1.getPosition().getX(), y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX(), y2 = p2.getPosition().getY();
        double dx = x1 - x2, dy = y1 - y2;
        boolean meets = (dx * dx + dy * dy) < EPSILON * EPSILON;

        if (meets) {
            out.add(new IntersectionResult(true, p1.getPosition(), null, null));
            out.add(new CoincidenceResult(true)); // 두 점이 완전히 일치
        } else {
            out.add(new IntersectionResult(false, null, p1.getPosition(), p2.getPosition()));
            out.add(new CoincidenceResult(false));
        }
    }

    // ── Point ∩ Line ───────────────────────────────────────────────────────────

    /**
     * 선의 구체 타입에 따라 적절한 알고리즘으로 분기한다.
     * Segment, StraightLine, 기타 Line 하위 타입 순으로 instanceof 체크.
     */
    private static void relatePointLine(List<RelationResult> out, Point p, Line line) {
        if (line instanceof Segment) {
            relatePointSegment(out, p, (Segment) line);
        } else if (line instanceof StraightLine) {
            relatePointStraightLine(out, p, (StraightLine) line);
        } else {
            relatePointGenericLine(out, p, line); // 미지의 Line 하위 타입 → StraightLine으로 대체
        }
    }

    /**
     * 점 p와 선분 seg의 교차 여부를 판별한다.
     *
     * 알고리즘:
     *   1. t = dot(p-p1, dir) / |dir|²  — 점의 선분 방향 투영 매개변수
     *   2. |(p-p1) × dir| < EPSILON    — 점이 선분 연장선 위에 있는지 확인 (collinear 판별)
     *   3. 0 ≤ t ≤ 1이면 선분 구간 내 교차
     *   4. 비교차 시 t 값에 따라 최근접 점 결정 (시작점/끝점/투영점)
     */
    private static void relatePointSegment(List<RelationResult> out, Point p, Segment seg) {
        Point p1 = seg.getPoint1(), p2 = seg.getPoint2();

        double x  = p.getPosition().getX(),  y  = p.getPosition().getY();
        double x1 = p1.getPosition().getX(), y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX(), y2 = p2.getPosition().getY();

        double dX = x2 - x1, dY = y2 - y1;    // 선분 방향벡터
        double lenSq = dX * dX + dY * dY;       // 방향벡터 크기 제곱
        // t: 점 p의 선분 방향 투영 매개변수 (0~1이면 선분 구간 내)
        double t = (lenSq < EPSILON) ? 0 : ((x - x1) * dX + (y - y1) * dY) / lenSq;

        boolean onLine = Math.abs((x - x1) * dY - (y - y1) * dX) < EPSILON; // collinear 판별
        boolean meets  = onLine && t >= 0 && t <= 1;

        if (meets) {
            out.add(new IntersectionResult(true, p.getPosition(), null, null));
        } else {
            // 최근접 점 산정: t < 0 → 시작점, t > 1 → 끝점, 그 외 → 투영점
            Position nearest;
            if (t < 0)      nearest = p1.getPosition();
            else if (t > 1) nearest = p2.getPosition();
            else             nearest = new Position(x1 + t * dX, y1 + t * dY);
            out.add(new IntersectionResult(false, null, p.getPosition(), nearest));
        }

        // 선분과의 각도: atan2(dy, dx) → normalizeAngle로 0~180° 범위 정규화
        double segAngle = Math.toDegrees(Math.atan2(dY, dX));
        out.add(new AngleResult(normalizeAngle(segAngle)));
    }

    /**
     * 점 p와 무한 직선 line의 교차 여부를 판별한다.
     *
     * 알고리즘:
     *   (p - P) × D = 0이면 직선 위의 점 (P: 원점, D: 방향벡터 끝점)
     *   비교차 시 수선의 발: t = dot(p-P, D), foot = P + t*D
     *
     * 주의: D가 단위 벡터라고 가정. 호출자가 단위화를 보장해야 한다.
     */
    private static void relatePointStraightLine(List<RelationResult> out, Point p, StraightLine line) {
        Point P = line.getPoint1(); // 직선 원점 P
        Point D = line.getPoint2(); // 방향벡터 끝점 D

        double x  = p.getPosition().getX(), y  = p.getPosition().getY();
        double xp = P.getPosition().getX(), yp = P.getPosition().getY();
        double xd = D.getPosition().getX(), yd = D.getPosition().getY();

        // 외적으로 collinear 판별: (p-P) × D ≈ 0
        boolean meets = Math.abs((x - xp) * yd - (y - yp) * xd) < EPSILON;

        if (meets) {
            out.add(new IntersectionResult(true, p.getPosition(), null, null));
        } else {
            // 수선의 발 계산: t = dot(p-P, D), foot = P + t*D
            double t = (x - xp) * xd + (y - yp) * yd;
            Position foot = new Position(xp + t * xd, yp + t * yd);
            out.add(new IntersectionResult(false, null, p.getPosition(), foot));
        }

        double lineAngle = Math.toDegrees(Math.atan2(yd, xd));
        out.add(new AngleResult(normalizeAngle(lineAngle)));
    }

    /** 지원되지 않는 Line 하위 타입에 대한 폴백: StraightLine으로 취급하여 계산한다. */
    private static void relatePointGenericLine(List<RelationResult> out, Point p, Line line) {
        relatePointStraightLine(out, p, toStraightLine(line));
    }

    // ── Line ∩ Line ────────────────────────────────────────────────────────────

    /**
     * 두 선 객체 간의 모든 관계(교차, 평행, 일치, 각도)를 계산한다.
     *
     * 알고리즘 흐름:
     *   1. 두 선의 단위 방향벡터를 계산
     *   2. 외적(cross) ≈ 0이면 방향이 평행(또는 반평행)
     *      - 평행이면 오프셋 외적으로 일치(coincident) 여부 추가 판별
     *        - 오프셋 외적 ≈ 0: 일치 상태
     *        - 오프셋 외적 ≠ 0: 평행하지만 다른 직선
     *   3. 외적 ≠ 0이면 교차점 계산 (크라메르 공식 적용)
     *      - Segment가 포함된 경우 t 매개변수 범위 검사로 구간 내 교차 여부 추가 확인
     */
    private static void relateLineLine(List<RelationResult> out, Line a, Line b) {
        double[] dirA = directionVector(a); // 선 a의 단위 방향벡터 [ax, ay]
        double[] dirB = directionVector(b); // 선 b의 단위 방향벡터 [bx, by]
        double axDy = dirA[0], ayDy = dirA[1];
        double bxDy = dirB[0], byDy = dirB[1];

        // 외적: 두 방향벡터가 평행하면 0
        double cross = axDy * byDy - ayDy * bxDy;
        boolean dirParallel = Math.abs(cross) < EPSILON;

        if (!dirParallel) {
            // ── 교차 케이스: 크라메르 공식으로 교차점 계산 ──────────────────
            double[] originA = originOf(a);
            double[] originB = originOf(b);
            double ox = originB[0] - originA[0]; // 두 원점 차이 벡터
            double oy = originB[1] - originA[1];
            double tA = (ox * byDy - oy * bxDy) / cross; // 선 a의 매개변수 t
            Position contact = new Position(originA[0] + tA * axDy, originA[1] + tA * ayDy);

            // Segment가 포함된 경우 구간 내 교차 여부 추가 확인
            boolean meets = true;
            if (a instanceof Segment && b instanceof Segment) {
                double tB = (ox * ayDy - oy * axDy) / cross;
                double lenA = segmentLength((Segment) a);
                double lenB = segmentLength((Segment) b);
                double tANorm = (lenA < EPSILON) ? 0 : tA / lenA;
                double tBNorm = (lenB < EPSILON) ? 0 : tB / lenB;
                meets = tANorm >= -EPSILON && tANorm <= 1 + EPSILON
                     && tBNorm >= -EPSILON && tBNorm <= 1 + EPSILON;
            } else if (a instanceof Segment) {
                double lenA = segmentLength((Segment) a);
                double tANorm = (lenA < EPSILON) ? 0 : tA / lenA;
                meets = tANorm >= -EPSILON && tANorm <= 1 + EPSILON;
            } else if (b instanceof Segment) {
                double tB = (ox * ayDy - oy * axDy) / cross;
                double lenB = segmentLength((Segment) b);
                double tBNorm = (lenB < EPSILON) ? 0 : tB / lenB;
                meets = tBNorm >= -EPSILON && tBNorm <= 1 + EPSILON;
            }

            if (meets) {
                out.add(new IntersectionResult(true, contact, null, null));
            } else {
                out.add(new IntersectionResult(false, null,
                    new Position(originA[0], originA[1]),
                    new Position(originB[0], originB[1])));
            }
            out.add(new ParallelResult(false));    // 교차하므로 평행 아님
            out.add(new CoincidenceResult(false)); // 교차하므로 일치 아님

        } else {
            // ── 평행/일치 케이스: 오프셋 외적으로 일치 여부 판별 ─────────────
            double[] originA = originOf(a);
            double[] originB = originOf(b);
            double offsetX = originB[0] - originA[0]; // 두 원점 연결 벡터
            double offsetY = originB[1] - originA[1];
            // 오프셋 벡터와 방향벡터의 외적 → 0이면 같은 직선(일치 후보)
            double offsetCross = offsetX * ayDy - offsetY * axDy;

            if (Math.abs(offsetCross) < EPSILON) {
                // 같은 직선 위 — Segment의 경우 실제 겹침 구간 추가 확인
                boolean actuallyMeet = true;
                if (a instanceof Segment && b instanceof Segment) {
                    actuallyMeet = segmentsOverlap1D((Segment) a, (Segment) b, dirA);
                }
                if (actuallyMeet) {
                    out.add(new IntersectionResult(true, new Position(originA[0], originA[1]), null, null));
                } else {
                    out.add(new IntersectionResult(false, null,
                        new Position(originA[0], originA[1]),
                        new Position(originB[0], originB[1])));
                }
                out.add(new ParallelResult(false));   // 일치는 평행의 특수 케이스 (ParallelResult false)
                out.add(new CoincidenceResult(true)); // 완전 일치
            } else {
                // 방향은 같지만 다른 직선 — 진정한 평행 상태
                out.add(new IntersectionResult(false, null,
                    new Position(originA[0], originA[1]),
                    new Position(originB[0], originB[1])));
                out.add(new ParallelResult(true));     // 평행
                out.add(new CoincidenceResult(false)); // 일치 아님
            }
        }

        // 두 방향벡터 사이의 사잇각 추가 (0°~90° 범위)
        double angle = angleBetweenDirections(dirA, dirB);
        out.add(new AngleResult(angle));
    }

    // ── 기하 유틸리티 내부 메서드 ──────────────────────────────────────────────

    /** Segment의 유클리드 길이를 계산한다. */
    private static double segmentLength(Segment seg) {
        double dx = seg.getPoint2().getPosition().getX() - seg.getPoint1().getPosition().getX();
        double dy = seg.getPoint2().getPosition().getY() - seg.getPoint1().getPosition().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 같은 직선 위에 있는 두 Segment가 1D 투영 공간에서 겹치는지 확인한다.
     * dir 방향으로 각 끝점을 투영하여 [min, max] 구간이 겹치면 overlap.
     */
    private static boolean segmentsOverlap1D(Segment a, Segment b, double[] dir) {
        double a1 = project(a.getPoint1(), dir);
        double a2 = project(a.getPoint2(), dir);
        double b1 = project(b.getPoint1(), dir);
        double b2 = project(b.getPoint2(), dir);
        double aMin = Math.min(a1, a2), aMax = Math.max(a1, a2);
        double bMin = Math.min(b1, b2), bMax = Math.max(b1, b2);
        return aMin <= bMax + EPSILON && bMin <= aMax + EPSILON;
    }

    /** 점 p를 방향벡터 dir에 투영한 스칼라 값을 반환한다. */
    private static double project(Point p, double[] dir) {
        return p.getPosition().getX() * dir[0] + p.getPosition().getY() * dir[1];
    }

    /**
     * 선의 단위 방향벡터를 반환한다.
     * 길이가 EPSILON 미만인 경우(길이 0 선) → 기본 방향 (1.0, 0.0)으로 대체.
     */
    private static double[] directionVector(Line line) {
        Point p1 = line.getPoint1(), p2 = line.getPoint2();
        double dx = p2.getPosition().getX() - p1.getPosition().getX();
        double dy = p2.getPosition().getY() - p1.getPosition().getY();
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < EPSILON) return new double[]{1.0, 0.0}; // 길이 0 선 방어 처리
        return new double[]{dx / len, dy / len};
    }

    /** 선의 point1 좌표를 배열로 반환한다 (크라메르 공식의 원점으로 사용). */
    private static double[] originOf(Line line) {
        Point p1 = line.getPoint1();
        return new double[]{p1.getPosition().getX(), p1.getPosition().getY()};
    }

    /**
     * 두 단위 방향벡터 사이의 사잇각을 계산한다 (0°~90° 범위).
     * Math.acos(|dot|)를 사용하여 예각 또는 직각 범위로 정규화.
     */
    private static double angleBetweenDirections(double[] d1, double[] d2) {
        double dot = d1[0] * d2[0] + d1[1] * d2[1];
        dot = Math.max(-1.0, Math.min(1.0, dot)); // acos 입력 범위 보장 [-1, 1]
        double angleRad = Math.acos(Math.abs(dot)); // |dot|로 예각(0~90°) 보장
        return Math.toDegrees(angleRad);
    }

    /**
     * 각도를 0°~180° 범위로 정규화한다.
     * atan2 반환 범위(-180°~180°)를 보정하여 항상 양의 예각 또는 둔각을 반환.
     */
    private static double normalizeAngle(double degrees) {
        degrees = degrees % 360;
        if (degrees < 0) degrees += 360;
        if (degrees > 180) degrees = 360 - degrees;
        return degrees;
    }

    /** Line 객체를 StraightLine으로 변환한다 (폴백 처리용 내부 유틸리티). */
    private static StraightLine toStraightLine(Line line) {
        return new StraightLine(line.getPoint1(), line.getPoint2());
    }
}
