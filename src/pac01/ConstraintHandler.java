package pac01;

public class ConstraintHandler {
	private Constraint[] constraints;
	private int constraintCount;
	private int nextId;
	private GeoObjectHandler geoObjectHandler;

	public ConstraintHandler(GeoObjectHandler geoObjectHandler) {
		constraints = new Constraint[100];
		constraintCount = 0;
		nextId = 1;
		this.geoObjectHandler = geoObjectHandler;
	}

	public DistanceConstraint addDistanceConstraint(int id1, int id2, double distance) {
		GeoObject object1 = geoObjectHandler.findObjectById(id1);
		GeoObject object2 = geoObjectHandler.findObjectById(id2);

		if (object1 == null || object2 == null) {
			return null;
		}

		DistanceConstraint constraint = new DistanceConstraint(nextId, object1, object2, distance);

		constraints[constraintCount] = constraint;
		constraintCount++;
		nextId++;

		return constraint;
	}

	public ParallelConstraint addParallelConstraint(int id1, int id2) {
		GeoObject object1 = geoObjectHandler.findObjectById(id1);
		GeoObject object2 = geoObjectHandler.findObjectById(id2);

		if (object1 == null || object2 == null) {
			return null;
		}

		ParallelConstraint constraint = new ParallelConstraint(nextId, object1, object2);

		constraints[constraintCount] = constraint;
		constraintCount++;
		nextId++;

		return constraint;
	}

	public PerpendicularConstraint addPerpendicularConstraint(int id1, int id2) {
		GeoObject object1 = geoObjectHandler.findObjectById(id1);
		GeoObject object2 = geoObjectHandler.findObjectById(id2);

		if (object1 == null || object2 == null) {
			return null;
		}

		PerpendicularConstraint constraint = new PerpendicularConstraint(nextId, object1, object2);

		constraints[constraintCount] = constraint;
		constraintCount++;
		nextId++;

		return constraint;
	}

	public boolean removeConstraint(int id) {
		for (int i = 0; i < constraintCount; i++) {
			if (constraints[i].getId() == id) {
				for (int j = i; j < constraintCount - 1; j++) {
					constraints[j] = constraints[j + 1];
				}

				constraints[constraintCount - 1] = null;
				constraintCount--;

				return true;
			}
		}

		return false;
	}

	public void printConstraints() {
		for (int i = 0; i < constraintCount; i++) {
			System.out.println(constraints[i].getInfo());
		}
	}
}