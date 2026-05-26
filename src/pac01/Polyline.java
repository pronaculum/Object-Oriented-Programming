package pac01;

public class Polyline extends GeoObject {
	private Dot[] points;

	public Polyline(int id, Dot[] points) {
		super(id, "PL" + id);
		this.points = points;
	}

	public Dot[] getPoints() {
		return points;
	}

	public int getPointCount() {
		return points.length;
	}

	public String getType() {
		return "Polyline";
	}

	public String getInfo() {
		return getId() + "번 객체: " + getName() + " / Polyline / 점 개수: " + points.length;
	}
}