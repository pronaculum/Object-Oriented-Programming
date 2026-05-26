package pac01;

public class DistanceConstraint extends Constraint {
	private double distance;

	public DistanceConstraint(int id, GeoObject object1, GeoObject object2, double distance) {
		super(id, object1, object2);
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public String getConstraintType() {
		return "Distance";
	}

	public String getInfo() {
		return getId() + "번 제약조건: " + getObject1().getName() + " - " + getObject2().getName() + " / Distance / 거리: "
				+ distance;
	}
}