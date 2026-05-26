package pac01;

public class ParallelConstraint extends Constraint {
	public ParallelConstraint(int id, GeoObject object1, GeoObject object2) {
		super(id, object1, object2);
	}

	public String getConstraintType() {
		return "Parallel";
	}
}