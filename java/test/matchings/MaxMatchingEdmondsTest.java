package test.matchings;

import graphs.matchings.MaxGeneralMatchingV3;
import java.util.*;
import java.util.stream.Stream;

public class MaxMatchingEdmondsTest {
    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(10) + 1;
            boolean[][] g = new boolean[n][n];
            List<Integer>[] graph = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    g[i][j] = g[j][i] = rnd.nextBoolean();
                    if (g[i][j]) {
                        graph[i].add(j);
                        graph[j].add(i);
                    }
                }
            }
            int res1 = MaxGeneralMatchingV3.maxMatching(graph);
            int res2 = maxMatchingSlow(g);
            if (res1 != res2) {
                System.err.println(res1 + " " + res2);
            }
        }
    }

    static int maxMatchingSlow(boolean[][] g) {
        int n = g.length;
        int[] dp = new int[1 << n];
        for (int mask = 0; mask < dp.length; mask++)
            for (int i = 0; i < n; i++)
                if ((mask & (1 << i)) == 0) {
                    for (int j = i + 1; j < n; j++)
                        if ((mask & (1 << j)) == 0 && g[i][j])
                            dp[mask | (1 << i) | (1 << j)] = Math.max(dp[mask | (1 << i) | (1 << j)], dp[mask] + 1);
                    break;
                }
        return dp[dp.length - 1];
    }
}
