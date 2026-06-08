package GeoDL;

/**
 * 두 기하 객체의 교차(만남, Intersection) 관계 결과를 담는 불변 데이터 클래스.
 *
 * 필드 의미:
 *   - intersects = true:  두 객체가 교차함. contactPoint에 교점 좌표가 담긴다.
 *   - intersects = false: 두 객체가 교차하지 않음.
 *                          optimalPoint1: obj1 쪽의 가장 가까운 점
 *                          optimalPoint2: obj2 쪽의 가장 가까운 점 (최근접점)
 *
 * 캡슐화:
 *   - 모든 필드는 private final — 생성자 주입 후 변경 불가.
 *   - 필요한 데이터에 맞는 생성자를 사용하여 불필요한 필드는 null로 전달한다.
 */
public class IntersectionResult extends RelationResult {

    private final boolean  intersects;    // 실제로 교차하는지 여부
    private final Position contactPoint;  // 교점 좌표 (intersects=true일 때만 유효)
    private final Position optimalPoint1; // obj1의 최근접 점 (intersects=false일 때 유효)
    private final Position optimalPoint2; // obj2의 최근접 점 (intersects=false일 때 유효)

    public IntersectionResult(boolean intersects, Position contactPoint,
                              Position optimalPoint1, Position optimalPoint2) {
        super(RelationType.INTERSECTION);
        this.intersects    = intersects;
        this.contactPoint  = contactPoint;
        this.optimalPoint1 = optimalPoint1;
        this.optimalPoint2 = optimalPoint2;
    }

    /** 두 객체가 실제로 교차하면 true를 반환한다. */
    public boolean isIntersecting()    { return intersects; }

    /** 교점 좌표를 반환한다. 교차하지 않으면 null. */
    public Position getContactPoint()  { return contactPoint; }

    /** obj1 쪽의 최근접 점을 반환한다. 교차하면 null. */
    public Position getOptimalPoint1() { return optimalPoint1; }

    /** obj2 쪽의 최근접 점을 반환한다. 교차하면 null. */
    public Position getOptimalPoint2() { return optimalPoint2; }

    @Override
    public String toString() {
        if (intersects) {
            return super.toString() + " intersects=true contactPoint=("
                + contactPoint.getX() + "," + contactPoint.getY() + ")";
        }
        return super.toString() + " intersects=false";
    }
}
