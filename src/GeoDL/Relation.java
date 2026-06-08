package GeoDL;

/**
 * 모든 기하학적 관계(Relation)의 최상위 추상 클래스.
 *
 * 설계 철학:
 *   - 관계는 홀로 존재할 수 없다 — 반드시 정보(Information) 객체들과의 연결을 통해서만 의미를 갖는다.
 *   - Line, Intersection 등 관계 계층의 모든 클래스는 이 클래스를 상속받아
 *     GeoList에 Information 타입으로 동질적으로 담길 수 있다.
 *   - "선(Line)은 점과 점의 관계"라는 도메인 비전을 코드 계층 구조로 표현한 결과물.
 *
 * 기존 한글 패키지의 '관계.관계' 클래스에서 이관 및 영문화.
 */
public abstract class Relation extends Information {
}
