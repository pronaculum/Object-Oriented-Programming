package pac01;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		GeoManager manager = new GeoManager();

		System.out.println("GeoDL 프로그램");
		System.out.println("점 4개의 좌표를 입력하세요.");

		System.out.print("1번 점 x y 입력: ");
		Dot p1 = manager.createDot(scanner.nextDouble(), scanner.nextDouble());

		System.out.print("2번 점 x y 입력: ");
		Dot p2 = manager.createDot(scanner.nextDouble(), scanner.nextDouble());

		System.out.print("3번 점 x y 입력: ");
		Dot p3 = manager.createDot(scanner.nextDouble(), scanner.nextDouble());

		System.out.print("4번 점 x y 입력: ");
		Dot p4 = manager.createDot(scanner.nextDouble(), scanner.nextDouble());

		Segment segment = manager.createSegment(p1, p2);
		Line line = manager.createLine(p1, p3);

		Dot[] polylineDots = new Dot[3];
		polylineDots[0] = p1;
		polylineDots[1] = p2;
		polylineDots[2] = p3;

		Polyline polyline = manager.createPolyline(polylineDots);

		Dot[] polygonDots = new Dot[4];
		polygonDots[0] = p1;
		polygonDots[1] = p2;
		polygonDots[2] = p3;
		polygonDots[3] = p4;

		Polygon polygon = manager.createPolygon(polygonDots);

		manager.addDistanceConstraint(p1.getId(), p2.getId(), 5.0);
		manager.addParallelConstraint(segment.getId(), line.getId());
		manager.addPerpendicularConstraint(segment.getId(), line.getId());

		System.out.println();
		System.out.println("===== 생성된 객체 목록 =====");
		manager.printObjects();

		System.out.println();
		System.out.println("===== 설정된 제약조건 목록 =====");
		manager.printConstraints();

		System.out.println();
		System.out.print("제거할 객체 번호 입력, 없으면 0 입력: ");
		int removeObjectId = scanner.nextInt();

		if (removeObjectId != 0) {
			manager.removeObject(removeObjectId);
		}

		System.out.print("해제할 제약조건 번호 입력, 없으면 0 입력: ");
		int removeConstraintId = scanner.nextInt();

		if (removeConstraintId != 0) {
			manager.removeConstraint(removeConstraintId);
		}

		System.out.println();
		System.out.println("===== 객체 제거 후 목록 =====");
		manager.printObjects();

		System.out.println();
		System.out.println("===== 제약조건 해제 후 목록 =====");
		manager.printConstraints();

		scanner.close();
	}
}