package GeoDL;

/**
 * 여러 점을 순서대로 연결한 다각선 객체입니다.
 */
public class Polyline extends Objectivity {
    private Dot[] dots;
    private int dotCount;

    public Polyline(Dot[] inputDots, int dotCount) {
        super("Polyline");
        this.dotCount = dotCount;
        this.dots = new Dot[dotCount];

        for (int i = 0; i < dotCount; i++) {
            this.dots[i] = inputDots[i];
        }
    }

    public int getDotCount() {
        return dotCount;
    }

    public Dot getDot(int index) {
        if (index < 0 || index >= dotCount) {
            return null;
        }

        return dots[index];
    }

    public Dot[] getDots() {
        Dot[] result = new Dot[dotCount];

        for (int i = 0; i < dotCount; i++) {
            result[i] = dots[i];
        }

        return result;
    }

    public double getCenterX() {
        double sum = 0.0;

        for (int i = 0; i < dotCount; i++) {
            sum += dots[i].getX();
        }

        return sum / dotCount;
    }

    public double getCenterY() {
        double sum = 0.0;

        for (int i = 0; i < dotCount; i++) {
            sum += dots[i].getY();
        }

        return sum / dotCount;
    }

    public void moveBy(double dx, double dy) {
        for (int i = 0; i < dotCount; i++) {
            dots[i].moveBy(dx, dy);
        }
    }

    public String getInfo() {
        return "Polyline(id=" + getId() + ", dotCount=" + dotCount + ")";
    }
}
