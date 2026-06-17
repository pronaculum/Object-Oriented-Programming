package 관계;

import 정보.점;
import 기능.리스트;
import 정보.선정보;

public class 선 extends 관계 {
    // 필드
    // 정보
    private 선정보 선정보;
    // 관계
    private 리스트 관계리스트;

    // 생성자
    public 선() {}  // 점 없이 생성될 수 없으나, 기본 생성자를 제공하여 유연성을 높입니다.
    public 선(선정보 선정보, 점 점1, 점 점2) {
        this.선정보 = 선정보; // 선정보를 생성자에서 설정할 수 있도록 합니다.
        this.관계리스트 = new 리스트();
        리스트.add(this.관계리스트, 0, 점1); // 선은 항상 점1과의 관계를 담고 있습니다.
        리스트.add(this.관계리스트, 1, 점2); // 선은 항상 점2와의 관계를 담고 있습니다.
    }

    // Getter 및 Setter 메서드
    public 선정보 get선정보() { return 선정보; }
    public 리스트 get관계리스트() { return 관계리스트; }
}