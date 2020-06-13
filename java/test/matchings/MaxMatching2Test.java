package test.matchings;

import graphs.matchings.MaxBipartiteMatchingV3;
import java.util.Random;

public class MaxMatching2Test {
    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n1 = rnd.nextInt(20) + 1;
            int n2 = rnd.nextInt(20) + 1;
            boolean[][] g = new boolean[n1][n2];
            for (int i = 0; i < n1; i++)
                for (int j = 0; j < n2; j++) g[i][j] = rnd.nextBoolean();
            int res1 = MaxBipartiteMatchingV3.maxMatching(g);
            int res2 = slowMinVertexCover(g);
            if (res1 != res2)
                throw new RuntimeException();
        }
    }

    static int slowMinVertexCover(boolean[][] g) {
        int n1 = g.length;
        int n2 = g[0].length;
        int[] mask = new int[n1];
        for (int i = 0; i < n1; i++)
            for (int j = 0; j < n2; j++)
                if (g[i][j])
                    mask[i] |= 1 << j;
        int res = n2;
        for (int m = 0; m < 1 << n2; m++) {
            int cur = Integer.bitCount(m);
            for (int i = 0; i < n1; i++)
                if ((mask[i] & m) != mask[i])
                    ++cur;
            res = Math.min(res, cur);
        }
        return res;
    }
}
