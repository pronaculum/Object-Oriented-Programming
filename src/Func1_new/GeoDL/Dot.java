package GeoDL;

/**
 * 점 객체입니다. x, y 좌표를 private으로 숨기고 메서드를 통해서만 접근합니다.
 */
public class Dot extends Objectivity {
    private double x;
    private double y;

    public Dot(double x, double y) {
        super("Dot");
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getCenterX() {
        return x;
    }

    public double getCenterY() {
        return y;
    }

    public void moveBy(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public String getInfo() {
        return "Dot(id=" + getId() + ", x=" + x + ", y=" + y + ")";
    }
}
