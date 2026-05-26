package pac01;

public class Polygon extends GeoObject {
	private Dot[] vertices;

	public Polygon(int id, Dot[] vertices) {
		super(id, "PG" + id);
		this.vertices = vertices;
	}

	public Dot[] getVertices() {
		return vertices;
	}

	public int getVertexCount() {
		return vertices.length;
	}

	public String getType() {
		return "Polygon";
	}

	public String getInfo() {
		return getId() + "번 객체: " + getName() + " / Polygon / 꼭짓점 개수: " + vertices.length;
	}
}
