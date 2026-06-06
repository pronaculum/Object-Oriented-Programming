package GeoDL;

/**
 * 세 개 이상의 점으로 이루어진 다각형 객체입니다.
 */
public class Polygon extends Objectivity {
    private Dot[] dots;
    private int dotCount;

    public Polygon(Dot[] inputDots, int dotCount) {
        super("Polygon");
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
        return "Polygon(id=" + getId() + ", dotCount=" + dotCount + ")";
    }
}
