package 정보;

public class 위치정보 extends 정보 {
    // 필드
    private double x;
    private double y;
    
    // 생성자
    public 위치정보() {
        this.x = 0;
        this.y = 0;
    }
    public 위치정보(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getter 및 Setter 메서드
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}