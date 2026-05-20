package 정보;
import 기능.리스트;
import 대상성.대상성;

public abstract class 점 extends 정보 {
    // 필드
    // 정보
    private 위치정보 위치정보;
    // 관계
    private 리스트 관계리스트;

    // 생성자
    public 점() {
        위치정보 = null;
        관계리스트 = new 리스트();
    }
    public 점(위치정보 위치정보) {
        this.위치정보 = 위치정보;
        this.관계리스트 = new 리스트();
    }

    // Getter 및 Setter 메서드
    public 위치정보 get위치정보() { return 위치정보; }
    public 리스트 get관계리스트() { return 관계리스트; }
    public void set위치정보(위치정보 위치정보) { this.위치정보 = 위치정보; }

    public boolean ask(대상성 대상성) { return (위치정보 != null) && (대상성 instanceof 점); }
}