package graphs.matchings;

import java.util.*;

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V^3)
public class MaxMatching2 {

    public static int maxMatching(boolean[][] graph) {
        int n1 = graph.length;
        int n2 = n1 == 0 ? 0 : graph[0].length;
        int[] matching = new int[n2];
        Arrays.fill(matching, -1);
        int matches = 0;
        for (int u = 0; u < n1; u++)
            if (findPath(graph, u, matching, new boolean[n1]))
                ++matches;
        return matches;
    }

    static boolean findPath(boolean[][] graph, int u1, int[] matching, boolean[] vis) {
        vis[u1] = true;
        for (int v = 0; v < matching.length; ++v) {
            int u2 = matching[v];
            if (graph[u1][v] && (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis))) {
                matching[v] = u1;
                return true;
            }
        }
        return false;
    }

    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n1 = rnd.nextInt(20) + 1;
            int n2 = rnd.nextInt(20) + 1;
            boolean[][] g = new boolean[n1][n2];
            for (int i = 0; i < n1; i++)
                for (int j = 0; j < n2; j++)
                    g[i][j] = rnd.nextBoolean();
            int res1 = maxMatching(g);
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
