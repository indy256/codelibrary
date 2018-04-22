package misc;

import java.util.*;

public class MaximumZeroSubmatrix {

    public static int maximumZeroSubmatrix(int[][] a) {
        int R = a.length;
        int C = a[0].length;

        int res = 0;
        int[] d = new int[C];
        Arrays.fill(d, -1);
        int[] d1 = new int[C];
        int[] d2 = new int[C];
        int[] st = new int[C];
        for (int r = 0; r < R; ++r) {
            for (int c = 0; c < C; ++c)
                if (a[r][c] == 1)
                    d[c] = r;
            int size = 0;
            for (int c = 0; c < C; ++c) {
                while (size > 0 && d[st[size - 1]] <= d[c]) --size;
                d1[c] = size == 0 ? -1 : st[size - 1];
                st[size++] = c;
            }
            size = 0;
            for (int c = C - 1; c >= 0; --c) {
                while (size > 0 && d[st[size - 1]] <= d[c]) --size;
                d2[c] = size == 0 ? C : st[size - 1];
                st[size++] = c;
            }
            for (int j = 0; j < C; ++j)
                res = Math.max(res, (r - d[j]) * (d2[j] - d1[j] - 1));
        }
        return res;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int R = rnd.nextInt(10) + 1;
            int C = rnd.nextInt(10) + 1;
            int[][] a = new int[R][C];
            for (int r = 0; r < R; r++)
                for (int c = 0; c < C; c++)
                    a[r][c] = rnd.nextInt(2);
            int res1 = maximumZeroSubmatrix(a);
            int res2 = slowMaximumZeroSubmatrix(a);
            if (res1 != res2)
                throw new RuntimeException(res1 + " " + res2);
        }
    }

    static int slowMaximumZeroSubmatrix(int[][] a) {
        int res = 0;
        int R = a.length;
        int C = a[0].length;
        for (int r2 = 0; r2 < R; r2++)
            for (int c2 = 0; c2 < C; c2++)
                for (int r1 = 0; r1 <= r2; r1++)
                    m1:for (int c1 = 0; c1 <= c2; c1++) {
                        for (int r = r1; r <= r2; r++)
                            for (int c = c1; c <= c2; c++)
                                if (a[r][c] != 0)
                                    continue m1;
                        res = Math.max(res, (r2 - r1 + 1) * (c2 - c1 + 1));
                    }
        return res;
    }
}
