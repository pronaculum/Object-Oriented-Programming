package GeoDL;

/**
 * 선분 객체입니다. 시작점과 끝점을 이용해 길이, 중심점, 각도를 계산합니다.
 */
public class Segment extends Objectivity {
    private Dot start;
    private Dot end;

    public Segment(Dot start, Dot end) {
        super("Segment");
        this.start = start;
        this.end = end;
    }

    public Dot getStart() {
        return start;
    }

    public Dot getEnd() {
        return end;
    }

    public double getLength() {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getCenterX() {
        return (start.getX() + end.getX()) / 2.0;
    }

    public double getCenterY() {
        return (start.getY() + end.getY()) / 2.0;
    }

    public boolean hasDirection() {
        return true;
    }

    public double getAngle() {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public void moveBy(double dx, double dy) {
        start.moveBy(dx, dy);
        end.moveBy(dx, dy);
    }

    public boolean rotateToAngle(double targetAngle) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        double halfLength = getLength() / 2.0;
        double radian = Math.toRadians(targetAngle);

        double dx = Math.cos(radian) * halfLength;
        double dy = Math.sin(radian) * halfLength;

        start.setPosition(centerX - dx, centerY - dy);
        end.setPosition(centerX + dx, centerY + dy);

        return true;
    }

    public String getInfo() {
        return "Segment(id=" + getId() + ", start=" + start.getInfo() + ", end=" + end.getInfo() + ")";
    }
}
