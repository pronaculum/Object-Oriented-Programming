package GeoDL;

/**
 * 선 유형 기하학 관계의 메타데이터 운반 클래스.
 *
 * 역할:
 *   - Line 생성 시 어떤 종류의 선인지(선분, 직선, 광선 등)를 LineType 열거형으로 기록.
 *   - private final lineType으로 불변성 보장 — 한번 생성된 선의 유형은 변경 불가.
 *   - GeoManager, IntersectionLogic 등에서 instanceof 체크 없이 유형을 확인하는 보조 수단.
 *
 * 기존 한글 패키지의 '선정보' 클래스에서 이관 및 영문화.
 */
public class LineInfo extends Information {

    /** 지원하는 선 유형 목록 */
    public enum LineType {
        SEGMENT,      // 유한 선분 (두 끝점 사이의 구간)
        STRAIGHT_LINE, // 무한 직선 (원점 P + 방향벡터 D)
        RAY,          // 반직선 (미래 확장 예약)
        POLYLINE,     // 다각선 (미래 확장 예약)
        CURVE         // 곡선 (미래 확장 예약)
    }

    private final LineType lineType; // 이 선 객체의 유형

    /** 기본값: 선분(SEGMENT)으로 생성 */
    public LineInfo() {
        this.lineType = LineType.SEGMENT;
    }

    /** 지정된 선 유형으로 생성 */
    public LineInfo(LineType lineType) {
        this.lineType = lineType;
    }

    /** 이 선 객체의 유형을 반환한다. */
    public LineType getLineType() { return lineType; }
}
