package GeoDL;

/**
 * 직선 객체입니다. 실제 무한 직선이지만 두 점을 기준으로 방향을 표현합니다.
 */
public class Line extends Objectivity {
    private Dot point1;
    private Dot point2;

    public Line(Dot point1, Dot point2) {
        super("Line");
        this.point1 = point1;
        this.point2 = point2;
    }

    public Dot getPoint1() {
        return point1;
    }

    public Dot getPoint2() {
        return point2;
    }

    public boolean isVerticalLine() {
        return point1.getX() == point2.getX();
    }

    public double getSlope() {
        if (isVerticalLine()) {
            return 0.0;
        }

        return (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
    }

    public double getCenterX() {
        return (point1.getX() + point2.getX()) / 2.0;
    }

    public double getCenterY() {
        return (point1.getY() + point2.getY()) / 2.0;
    }

    public boolean hasDirection() {
        return true;
    }

    public double getAngle() {
        double dx = point2.getX() - point1.getX();
        double dy = point2.getY() - point1.getY();
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public void moveBy(double dx, double dy) {
        point1.moveBy(dx, dy);
        point2.moveBy(dx, dy);
    }

    public boolean rotateToAngle(double targetAngle) {
        double centerX = getCenterX();
        double centerY = getCenterY();

        double dx0 = point2.getX() - point1.getX();
        double dy0 = point2.getY() - point1.getY();
        double length = Math.sqrt(dx0 * dx0 + dy0 * dy0);
        double halfLength = length / 2.0;
        double radian = Math.toRadians(targetAngle);

        double dx = Math.cos(radian) * halfLength;
        double dy = Math.sin(radian) * halfLength;

        point1.setPosition(centerX - dx, centerY - dy);
        point2.setPosition(centerX + dx, centerY + dy);

        return true;
    }

    public String getInfo() {
        return "Line(id=" + getId() + ", point1=" + point1.getInfo() + ", point2=" + point2.getInfo() + ")";
    }
}
