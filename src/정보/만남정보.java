package 정보;

public class 만남정보 extends 정보 {
    // 필드
    private boolean 만남여부;

    // 생성자
    public 만남정보() { this.만남여부 = false; }
    public 만남정보(boolean 만남여부) { this.만남여부 = 만남여부; }

    // Getter 및 Setter 메서드
    public boolean get만남여부() { return 만남여부; }
    public void set만남여부(boolean 만남여부) { this.만남여부 = 만남여부; }
}