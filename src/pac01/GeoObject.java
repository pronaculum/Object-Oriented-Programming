package pac01;

public class GeoObject {
	private int id;
	private String name;

	public GeoObject(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return "GeoObject";
	}

	public String getInfo() {
		return id + "번 객체: " + name + " / " + getType();
	}
}
