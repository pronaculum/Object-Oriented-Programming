package 정보;

public class 선정보 extends 정보 {
    // 타입
    public static enum type { 선분, 직선, 반직선, 다각선, 곡선 }

    // 필드
    private type 선종류;

    // 생성자
    public 선정보() { this.선종류 = type.선분; }
    public 선정보(type 선종류) { this.선종류 = 선종류; }

    // Getter 및 Setter 메서드
    public type get선종류() { return 선종류; }
    public void set선종류(type 선종류) { this.선종류 = 선종류; }
}