package structures;

import java.util.Random;

public class SegmentTree2D {
    public static int max(int[][] t, int x1, int y1, int x2, int y2) {
        int n = t.length >> 1;
        x1 += n;
        x2 += n;
        int m = t[0].length >> 1;
        y1 += m;
        y2 += m;
        int res = Integer.MIN_VALUE;
        for (int lx = x1, rx = x2; lx <= rx; lx = (lx + 1) >> 1, rx = (rx - 1) >> 1)
            for (int ly = y1, ry = y2; ly <= ry; ly = (ly + 1) >> 1, ry = (ry - 1) >> 1) {
                if ((lx & 1) != 0 && (ly & 1) != 0) res = Math.max(res, t[lx][ly]);
                if ((lx & 1) != 0 && (ry & 1) == 0) res = Math.max(res, t[lx][ry]);
                if ((rx & 1) == 0 && (ly & 1) != 0) res = Math.max(res, t[rx][ly]);
                if ((rx & 1) == 0 && (ry & 1) == 0) res = Math.max(res, t[rx][ry]);
            }
        return res;
    }

    public static void add(int[][] t, int x, int y, int value) {
        x += t.length >> 1;
        y += t[0].length >> 1;
        t[x][y] += value;
        for (int tx = x; tx > 0; tx >>= 1)
            for (int ty = y; ty > 0; ty >>= 1) {
                if (tx > 1) t[tx >> 1][ty] = Math.max(t[tx][ty], t[tx ^ 1][ty]);
                if (ty > 1) t[tx][ty >> 1] = Math.max(t[tx][ty], t[tx][ty ^ 1]);
            }

//		for (int ty = y; ty > 1; ty >>= 1)
//			t[x][ty >> 1] = Math.max(t[x][ty], t[x][ty ^ 1]);
//		for (int tx = x; tx > 1; tx >>= 1)
//			for (int ty = y; ty > 0; ty >>= 1)
//				t[tx >> 1][ty] = Math.max(t[tx][ty], t[tx ^ 1][ty]);

//		for (int lx=x; lx> 0; lx >>= 1) {
//			if (lx > 1)
//				t[lx >> 1][y] = Math.max(t[lx][y], t[lx ^ 1][y]);
//			for (int ly = y; ly > 1; ly >>= 1)
//				t[lx][ly >> 1] = Math.max(t[lx][ly], t[lx][ly ^ 1]);
//		}
    }

    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step1 = 0; step1 < 1000; step1++) {
            int sx = rnd.nextInt(10) + 1;
            int sy = rnd.nextInt(10) + 1;
            int[][] t = new int[sx * 2][sy * 2];
            int[][] tt = new int[sx][sy];
            for (int step2 = 0; step2 < 1000; step2++) {
                if (rnd.nextBoolean()) {
                    int x = rnd.nextInt(sx);
                    int y = rnd.nextInt(sy);
                    int v = rnd.nextInt(10) - 5;
                    add(t, x, y, v);
                    tt[x][y] += v;
                } else {
                    int x2 = rnd.nextInt(sx);
                    int x1 = rnd.nextInt(x2 + 1);
                    int y2 = rnd.nextInt(sy);
                    int y1 = rnd.nextInt(y2 + 1);
                    int res1 = max(t, x1, y1, x2, y2);
                    int res2 = Integer.MIN_VALUE;
                    for (int x = x1; x <= x2; x++)
                        for (int y = y1; y <= y2; y++)
                            res2 = Math.max(res2, tt[x][y]);
                    if (res1 != res2)
                        throw new RuntimeException();
                }
            }
        }
    }
}
