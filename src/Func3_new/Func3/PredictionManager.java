package mycode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PredictionManager {
	private Map<Integer, PointPredictObject> objects;
	private LinearRegressionPredictor predictor;
	private PredictionConstraint constraint;

	public PredictionManager() {
		this.objects = new HashMap<>();
		this.predictor = new LinearRegressionPredictor();
		this.constraint = new PredictionConstraint(2);
	}

	public void addObject(PointPredictObject object) {
		objects.put(object.getId(), object);
	}

	public boolean removeObject(int id) {
		return objects.remove(id) != null;
	}

	public PointPredictObject getObject(int id) {
		return objects.get(id);
	}

	public void addPointToObject(int id, double x, double y, double time) {
		PointPredictObject object = objects.get(id);

		if (object == null) {
			throw new IllegalArgumentException("해당 id의 객체가 존재하지 않습니다.");
		}

		object.addPoint(x, y, time);
	}

	public PointData predictNextObject(int id) {
		PointPredictObject object = objects.get(id);

		if (object == null) {
			throw new IllegalArgumentException("해당 id의 객체가 존재하지 않습니다.");
		}

		List<PointData> points = object.getPoints();

		if (!constraint.validate(points)) {
			throw new IllegalStateException("예측에 필요한 점 데이터가 부족하거나 시간 데이터가 올바르지 않습니다.");
		}

		return predictor.predictNext(points);
	}

	public PointData predictPreviousObject(int id) {
		PointPredictObject object = objects.get(id);

		if (object == null) {
			throw new IllegalArgumentException("해당 id의 객체가 존재하지 않습니다.");
		}

		List<PointData> points = object.getPoints();

		if (!constraint.validate(points)) {
			throw new IllegalStateException("예측에 필요한 점 데이터가 부족하거나 시간 데이터가 올바르지 않습니다.");
		}

		return predictor.predictPrevious(points);
	}

	public Map<Integer, PointData> predictAllNext() {
		Map<Integer, PointData> results = new LinkedHashMap<>();

		for (PointPredictObject object : objects.values()) {
			List<PointData> points = object.getPoints();

			if (constraint.validate(points)) {
				PointData predictedPoint = predictor.predictNext(points);
				results.put(object.getId(), predictedPoint);
			}
		}

		return results;
	}

	public Map<Integer, PointData> predictAllPrevious() {
		Map<Integer, PointData> results = new LinkedHashMap<>();

		for (PointPredictObject object : objects.values()) {
			List<PointData> points = object.getPoints();

			if (constraint.validate(points)) {
				PointData predictedPoint = predictor.predictPrevious(points);
				results.put(object.getId(), predictedPoint);
			}
		}

		return results;
	}
}