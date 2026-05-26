package pac01;

public class GeoManager {
	private GeoObjectHandler geoObjectHandler;
	private ConstraintHandler constraintHandler;

	public GeoManager() {
		geoObjectHandler = new GeoObjectHandler();
		constraintHandler = new ConstraintHandler(geoObjectHandler);
	}

	public Dot createDot(double x, double y) {
		return geoObjectHandler.createDot(x, y);
	}

	public Segment createSegment(Dot start, Dot end) {
		return geoObjectHandler.createSegment(start, end);
	}

	public Line createLine(Dot point1, Dot point2) {
		return geoObjectHandler.createLine(point1, point2);
	}

	public Polyline createPolyline(Dot[] points) {
		return geoObjectHandler.createPolyline(points);
	}

	public Polygon createPolygon(Dot[] vertices) {
		return geoObjectHandler.createPolygon(vertices);
	}

	public boolean removeObject(int id) {
		return geoObjectHandler.removeObject(id);
	}

	public DistanceConstraint addDistanceConstraint(int id1, int id2, double distance) {
		return constraintHandler.addDistanceConstraint(id1, id2, distance);
	}

	public ParallelConstraint addParallelConstraint(int id1, int id2) {
		return constraintHandler.addParallelConstraint(id1, id2);
	}

	public PerpendicularConstraint addPerpendicularConstraint(int id1, int id2) {
		return constraintHandler.addPerpendicularConstraint(id1, id2);
	}

	public boolean removeConstraint(int id) {
		return constraintHandler.removeConstraint(id);
	}

	public void printObjects() {
		geoObjectHandler.printObjects();
	}

	public void printConstraints() {
		constraintHandler.printConstraints();
	}
}