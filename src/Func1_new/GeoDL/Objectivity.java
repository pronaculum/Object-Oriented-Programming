package GeoDL;

/**
 * GeoDL 라이브러리에서 사용하는 모든 기하 객체의 공통 부모 클래스입니다. 점, 선분, 직선, 다각선, 다각형은 모두
 * Objectivity를 상속받습니다.
 */
public class Objectivity {
	private int id;
	private String type;

	public Objectivity(String type) {
		this.id = 0;
		this.type = type;
	}

	void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public double getCenterX() {
		return 0.0;
	}

	public double getCenterY() {
		return 0.0;
	}

	public boolean hasDirection() {
		return false;
	}

	public double getAngle() {
		return 0.0;
	}

	public void moveBy(double dx, double dy) {
		// 방향이나 위치가 없는 기본 객체는 이동하지 않습니다.
	}

	public boolean rotateToAngle(double targetAngle) {
		// 기본 객체는 회전할 수 없습니다.
		return false;
	}

	public String getInfo() {
		return "Objectivity(type=" + type + ", id=" + id + ")";
	}
}
