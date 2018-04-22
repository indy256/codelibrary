package structures;

import java.util.Random;

public class SegmentTree3D {
    public static int max(int[][][] t, int x1, int y1, int z1, int x2, int y2, int z2) {
        int n = t.length >> 1;
        x1 += n;
        x2 += n;
        int m = t[0].length >> 1;
        y1 += m;
        y2 += m;
        int k = t[0][0].length >> 1;
        z1 += k;
        z2 += k;
        int res = Integer.MIN_VALUE;
        for (int lx = x1, rx = x2; lx <= rx; lx = (lx + 1) >> 1, rx = (rx - 1) >> 1)
            for (int ly = y1, ry = y2; ly <= ry; ly = (ly + 1) >> 1, ry = (ry - 1) >> 1)
                for (int lz = z1, rz = z2; lz <= rz; lz = (lz + 1) >> 1, rz = (rz - 1) >> 1) {
                    if ((lx & 1) != 0 && (ly & 1) != 0 && (lz & 1) != 0) res = Math.max(res, t[lx][ly][lz]);
                    if ((lx & 1) != 0 && (ly & 1) != 0 && (rz & 1) == 0) res = Math.max(res, t[lx][ly][rz]);
                    if ((lx & 1) != 0 && (ry & 1) == 0 && (lz & 1) != 0) res = Math.max(res, t[lx][ry][lz]);
                    if ((lx & 1) != 0 && (ry & 1) == 0 && (rz & 1) == 0) res = Math.max(res, t[lx][ry][rz]);
                    if ((rx & 1) == 0 && (ly & 1) != 0 && (lz & 1) != 0) res = Math.max(res, t[rx][ly][lz]);
                    if ((rx & 1) == 0 && (ly & 1) != 0 && (rz & 1) == 0) res = Math.max(res, t[rx][ly][rz]);
                    if ((rx & 1) == 0 && (ry & 1) == 0 && (lz & 1) != 0) res = Math.max(res, t[rx][ry][lz]);
                    if ((rx & 1) == 0 && (ry & 1) == 0 && (rz & 1) == 0) res = Math.max(res, t[rx][ry][rz]);
                }
        return res;
    }

    public static void add(int[][][] t, int x, int y, int z, int value) {
        x += t.length >> 1;
        y += t[0].length >> 1;
        z += t[0][0].length >> 1;
        t[x][y][z] += value;
        for (int tx = x; tx > 0; tx >>= 1)
            for (int ty = y; ty > 0; ty >>= 1)
                for (int tz = z; tz > 0; tz >>= 1) {
                    if (tx > 1) t[tx >> 1][ty][tz] = Math.max(t[tx][ty][tz], t[tx ^ 1][ty][tz]);
                    if (ty > 1) t[tx][ty >> 1][tz] = Math.max(t[tx][ty][tz], t[tx][ty ^ 1][tz]);
                    if (tz > 1) t[tx][ty][tz >> 1] = Math.max(t[tx][ty][tz], t[tx][ty][tz ^ 1]);
                }
//		for (int tz = z; tz > 1; tz >>= 1)
//			t[x][y][tz >> 1] = Math.max(t[x][y][tz], t[x][y][tz ^ 1]);
//		for (int ty = y; ty > 1; ty >>= 1)
//			for (int tz = z; tz > 0; tz >>= 1)
//				t[x][ty>>1][tz] = Math.max(t[x][ty][tz], t[x][ty^1][tz]);
//		for (int tx = x; tx > 1; tx >>= 1)
//			for (int ty = y; ty > 0; ty >>= 1)
//				for (int tz = z; tz > 0; tz >>= 1)
//					t[tx >> 1][ty][tz] = Math.max(t[tx][ty][tz], t[tx ^ 1][ty][tz]);
    }

    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step1 = 0; step1 < 1000; step1++) {
            int sx = rnd.nextInt(10) + 1;
            int sy = rnd.nextInt(10) + 1;
            int sz = rnd.nextInt(10) + 1;
            int[][][] t = new int[sx * 2][sy * 2][sz * 2];
            int[][][] tt = new int[sx][sy][sz];
            for (int step2 = 0; step2 < 1000; step2++) {
                if (rnd.nextBoolean()) {
                    int x = rnd.nextInt(sx);
                    int y = rnd.nextInt(sy);
                    int z = rnd.nextInt(sz);
                    int v = rnd.nextInt(10) - 5;
                    add(t, x, y, z, v);
                    tt[x][y][z] += v;
                } else {
                    int x2 = rnd.nextInt(sx);
                    int x1 = rnd.nextInt(x2 + 1);
                    int y2 = rnd.nextInt(sy);
                    int y1 = rnd.nextInt(y2 + 1);
                    int z2 = rnd.nextInt(sz);
                    int z1 = rnd.nextInt(z2 + 1);
                    int res1 = max(t, x1, y1, z1, x2, y2, z2);
                    int res2 = Integer.MIN_VALUE;
                    for (int x = x1; x <= x2; x++)
                        for (int y = y1; y <= y2; y++)
                            for (int z = z1; z <= z2; z++)
                                res2 = Math.max(res2, tt[x][y][z]);
                    if (res1 != res2)
                        throw new RuntimeException();
                }
            }
        }
    }
}
