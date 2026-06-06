package GeoDL;

/**
 * 2D 좌표 쌍을 표현하는 불변(Immutable) 값 객체.
 *
 * 불변성 보장 원칙:
 *   - x, y 필드를 private final로 선언하여 생성 이후 외부에서 수정 불가.
 *   - 점의 위치를 바꾸려면 새로운 Position 인스턴스를 생성하여 Point.moveTo()에 전달해야 한다.
 *   - 이 구조 덕분에 여러 객체가 같은 Position 참조를 공유해도 데이터 오염이 발생하지 않는다.
 *
 * 기존 한글 패키지의 '위치정보' 클래스에서 이관 및 영문화한 클래스.
 */
public class Position extends Information {
    private final double x; // X 좌표 (불변)
    private final double y; // Y 좌표 (불변)

    /** 기본 생성자: 원점 (0, 0)을 나타내는 Position 생성 */
    public Position() {
        this.x = 0;
        this.y = 0;
    }

    /** 지정된 좌표로 Position 생성 */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
