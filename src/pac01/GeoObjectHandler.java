package pac01;

public class GeoObjectHandler {
	private GeoObject[] objects;
	private int objectCount;
	private int nextId;

	public GeoObjectHandler() {
		objects = new GeoObject[100];
		objectCount = 0;
		nextId = 1;
	}

	public Dot createDot(double x, double y) {
		Dot dot = new Dot(nextId, x, y);

		objects[objectCount] = dot;
		objectCount++;
		nextId++;

		return dot;
	}

	public Segment createSegment(Dot start, Dot end) {
		Segment segment = new Segment(nextId, start, end);

		objects[objectCount] = segment;
		objectCount++;
		nextId++;

		return segment;
	}

	public Line createLine(Dot point1, Dot point2) {
		Line line = new Line(nextId, point1, point2);

		objects[objectCount] = line;
		objectCount++;
		nextId++;

		return line;
	}

	public Polyline createPolyline(Dot[] points) {
		Polyline polyline = new Polyline(nextId, points);

		objects[objectCount] = polyline;
		objectCount++;
		nextId++;

		return polyline;
	}

	public Polygon createPolygon(Dot[] vertices) {
		Polygon polygon = new Polygon(nextId, vertices);

		objects[objectCount] = polygon;
		objectCount++;
		nextId++;

		return polygon;
	}

	public boolean removeObject(int id) {
		for (int i = 0; i < objectCount; i++) {
			if (objects[i].getId() == id) {
				for (int j = i; j < objectCount - 1; j++) {
					objects[j] = objects[j + 1];
				}

				objects[objectCount - 1] = null;
				objectCount--;

				return true;
			}
		}

		return false;
	}

	public GeoObject findObjectById(int id) {
		for (int i = 0; i < objectCount; i++) {
			if (objects[i].getId() == id) {
				return objects[i];
			}
		}

		return null;
	}

	public void printObjects() {
		for (int i = 0; i < objectCount; i++) {
			System.out.println(objects[i].getInfo());
		}
	}
}