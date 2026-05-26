package pac01;

public class Constraint {
	private int id;
	private GeoObject object1;
	private GeoObject object2;

	public Constraint(int id, GeoObject object1, GeoObject object2) {
		this.id = id;
		this.object1 = object1;
		this.object2 = object2;
	}

	public int getId() {
		return id;
	}

	public GeoObject getObject1() {
		return object1;
	}

	public GeoObject getObject2() {
		return object2;
	}

	public String getConstraintType() {
		return "Constraint";
	}

	public String getInfo() {
		return id + "번 제약조건: " + object1.getName() + " - " + object2.getName() + " / " + getConstraintType();
	}
}