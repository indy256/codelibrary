package geometry;

public class PointInPolygon {
    public static int pointInPolygon(int qx, int qy, int[] x, int[] y) {
        int n = x.length;
        int cnt = 0;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (y[i] == qy && (x[i] == qx || y[j] == qy && (x[i] <= qx || x[j] <= qx) && (x[i] >= qx || x[j] >= qx)))
                return 0; // boundary
            if ((y[i] > qy) != (y[j] > qy)) {
                long det = ((long) x[i] - qx) * ((long) y[j] - qy) - ((long) x[j] - qx) * ((long) y[i] - qy);
                if (det == 0)
                    return 0; // boundary
                if ((det > 0) != (y[j] > y[i]))
                    ++cnt;
            }
        }
        return cnt % 2 == 0 ? -1 /* exterior */ : 1 /* interior */;
    }

    // Usage example
    public static void main(String[] args) {
        int[] x = {0, 0, 2, 2};
        int[] y = {0, 2, 2, 0};
        System.out.println(1 == pointInPolygon(1, 1, x, y));
        System.out.println(0 == pointInPolygon(0, 0, x, y));
        System.out.println(-1 == pointInPolygon(0, 3, x, y));
    }
}
