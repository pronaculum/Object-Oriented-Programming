package GeoDL;

/**
 * GeoDL 도메인 모델의 최상위 루트 추상 클래스.
 *
 * 설계 철학 — "세상 만물은 정보와 관계로 나뉜다":
 *   - 정보(Information) 계층: 독립적으로 존재할 수 있는 객체 (예: Position, Point)
 *   - 관계(Relation) 계층: 반드시 다른 정보 객체와 연결되어야 의미를 갖는 객체 (예: Line)
 *
 * GeoList가 Information 타입으로 노드를 저장하므로,
 * 모든 도메인 객체(정보 + 관계 공통)가 이 클래스를 상속받아 리스트에 동질적으로 담길 수 있다.
 */
public abstract class Information {
}
