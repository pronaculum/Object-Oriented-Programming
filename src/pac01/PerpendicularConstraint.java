package pac01;

public class PerpendicularConstraint extends Constraint {
	public PerpendicularConstraint(int id, GeoObject object1, GeoObject object2) {
		super(id, object1, object2);
	}

	public String getConstraintType() {
		return "Perpendicular";
	}
}