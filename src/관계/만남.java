package 관계;

import 기능.리스트;
import 대상성.대상성;
import 정보.만남정보;
import 정보.위치정보;
import 정보.점;

public class 만남 extends 관계 {
    // 필드
    // 정보
    private 만남정보 만남정보;
    private 위치정보 접점;
    private 위치정보 최적점1;
    private 위치정보 최적점2;
    // 관계
    private 리스트 관계리스트;

    // 생성자
    public 만남() {
        this.만남정보 = null;
        this.접점 = null;
        this.최적점1 = null;
        this.최적점2 = null;
        this.관계리스트 = new 리스트();
    }
    public 만남(대상성 대상성1, 대상성 대상성2) {
        this(); // 기본 생성자를 호출하여 필드를 초기화합니다.
        if (대상성1 == null || 대상성2 == null) {
            throw new IllegalArgumentException("대상은 null일 수 없습니다.");
        }
        else if (대상성1 instanceof 점 && 대상성2 instanceof 점) {
            만남로직.점점만남(this, (점) 대상성1, (점) 대상성2);
        }
        else if (대상성1 instanceof 점 && 대상성2 instanceof 선분) {
            만남로직.점선분만남(this, (점) 대상성1, (선분) 대상성2);
        }
        else if (대상성1 instanceof 점 && 대상성2 instanceof 직선) {
            만남로직.점직선만남(this, (점) 대상성1, (직선) 대상성2);
        }
        else {
            throw new IllegalArgumentException("지원되지 않는 대상성 유형입니다.");
        }
    }

    // Getter 및 Setter 메서드
    public 만남정보 get만남정보() { return this.만남정보; }
    public 위치정보 get접점() { return this.접점; }
    public 위치정보 get최적점1() { return this.최적점1; }
    public 위치정보 get최적점2() { return this.최적점2; }
    public 리스트 get관계리스트() { return this.관계리스트; }
    public void set만남정보(만남정보 만남정보) { this.만남정보 = 만남정보; }
    public void set접점(위치정보 접점) { this.접점 = 접점; }
    public void set최적점1(위치정보 최적점1) { this.최적점1 = 최적점1; }
    public void set최적점2(위치정보 최적점2) { this.최적점2 = 최적점2; }
}