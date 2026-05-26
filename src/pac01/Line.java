package pac01;

public class Line extends GeoObject {
	private Dot point1;
	private Dot point2;

	public Line(int id, Dot point1, Dot point2) {
		super(id, "L" + id);
		this.point1 = point1;
		this.point2 = point2;
	}

	public Dot getPoint1() {
		return point1;
	}

	public Dot getPoint2() {
		return point2;
	}

	public String getType() {
		return "Line";
	}

	public String getInfo() {
		return getId() + "번 객체: " + getName() + " / Line / " + point1.getName() + " - " + point2.getName();
	}
}