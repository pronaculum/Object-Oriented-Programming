package 관계;

import 기능.리스트;
import 정보.만남정보;
import 정보.위치정보;
import 정보.점;

public class 만남로직 {
    private static final double EPSILON = 1e-9;
    public static void 점점만남(만남 만남, 점 점1, 점 점2) {
        double x1 = 점1.get위치정보().getX(); double y1 = 점1.get위치정보().getY();
        double x2 = 점2.get위치정보().getX(); double y2 = 점2.get위치정보().getY();
        if (Math.abs(x1 - x2) < EPSILON && Math.abs(y1 - y2) < EPSILON) {
            만남.set접점(점1.get위치정보());
            만남.set만남정보(new 만남정보(true));
        }
        else {
            만남.set최적점1(점1.get위치정보());
            만남.set최적점2(점2.get위치정보());
            만남.set만남정보(new 만남정보(false));
        }
    }
    public static void 점선분만남(만남 만남, 점 점, 선분 선분) {
        점 점1 = (점) 리스트.get_entry(선분.get관계리스트(), 0);
        점 점2 = (점) 리스트.get_entry(선분.get관계리스트(), 1);

        double x  = 점.get위치정보().getX();  double y  = 점.get위치정보().getY();
        double x1 = 점1.get위치정보().getX(); double y1 = 점1.get위치정보().getY();
        double x2 = 점2.get위치정보().getX(); double y2 = 점2.get위치정보().getY();

        double delta_x = x2 - x1;              double delta_y = y2 - y1;
        double t = ((x - x1) * delta_x + (y - y1) * delta_y) / (delta_x * delta_x + delta_y * delta_y);

        if (Math.abs((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1)) < EPSILON) {
            // 점이 선분과 만납니다.
            만남.set접점(점.get위치정보());
            만남.set만남정보(new 만남정보(true));
        }
        else {
            // 점이 선분과 만나지 않습니다.
            // 점이 선분과 만나지 않는 경우, 최적점1은 점의 정보로 설정합니다.
            만남.set최적점1(점.get위치정보());
            만남.set만남정보(new 만남정보(false));

            // 점이 선분과 만나지 않는 경우, 최적점2는 점1, 점2 또는 선분 위의 투영된 점으로 설정합니다.
            if (t < 0) {
                만남.set최적점2(점1.get위치정보());
            }
            else if (t > 1) {
                만남.set최적점2(점2.get위치정보());
            }
            else {
                만남.set최적점2(new 위치정보(x1 + t * delta_x, y1 + t * delta_y));
            }
        }
    }
    public static void 점직선만남(만남 만남, 점 점, 직선 직선) {
        점 P = (점) 리스트.get_entry(직선.get관계리스트(), 0);
        점 D = (점) 리스트.get_entry(직선.get관계리스트(), 1);
        
        double x  = 점.get위치정보().getX(); double y  = 점.get위치정보().getY();
        double xp =  P.get위치정보().getX(); double yp =  P.get위치정보().getY();
        double xd =  D.get위치정보().getX(); double yd =  D.get위치정보().getY();

        if (Math.abs((x - xp) * yd - (y - yp) * xd) < EPSILON) {
            // 점이 직선과 만납니다.
            만남.set접점(점.get위치정보());
            만남.set만남정보(new 만남정보(true));
        }
        else {
            // 점이 직선과 만나지 않습니다.
            // 점이 직선과 만나지 않는 경우, 최적점1은 점의 정보로 설정합니다.
            만남.set최적점1(점.get위치정보());
            만남.set만남정보(new 만남정보(false));

            // 점이 직선과 만나지 않는 경우, 최적점2는 직선 위의 투영된 점으로 설정합니다.
            double t = (x - xp) *xd + (y - yp) * yd;
            만남.set최적점2(new 위치정보(xp + t * xd, yp + t * yd));
        }

    }
}
