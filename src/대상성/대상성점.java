package 대상성;

import 정보.위치정보;

public class 대상성점 implements 대상성 {
    private 위치정보 위치정보;
    
    public 대상성점() {
        this.위치정보 = new 위치정보();
    }
    public 대상성점(위치정보 위치정보) {
        this.위치정보 = 위치정보;
    }
    public boolean 유효성체크() { return true; }
    public 위치정보 get위치정보() { return 위치정보; }

}
