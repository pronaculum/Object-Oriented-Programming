package pac01;

public class Segment extends GeoObject {
	private Dot start;
	private Dot end;

	public Segment(int id, Dot start, Dot end) {
		super(id, "S" + id);
		this.start = start;
		this.end = end;
	}

	public Dot getStart() {
		return start;
	}

	public Dot getEnd() {
		return end;
	}

	public String getType() {
		return "Segment";
	}

	public String getInfo() {
		return getId() + "번 객체: " + getName() + " / Segment / " + start.getName() + " - " + end.getName();
	}
}
