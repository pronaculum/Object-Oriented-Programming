package pac01;

public class Dot extends GeoObject {
	private double x;
	private double y;

	public Dot(int id, double x, double y) {
		super(id, "P" + id);
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getType() {
		return "Dot";
	}

	public String getInfo() {
		return getId() + "번 객체: " + getName() + " / Dot / 좌표(" + x + ", " + y + ")";
	}
}