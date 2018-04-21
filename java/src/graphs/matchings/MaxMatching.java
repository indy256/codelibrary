package graphs.matchings;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V * E)
public class MaxMatching {

    public static int maxMatching(List<Integer>[] graph, int n2) {
        int n1 = graph.length;
        int[] matching = new int[n2];
        Arrays.fill(matching, -1);
        int matches = 0;
        for (int u = 0; u < n1; u++) {
            if (findPath(graph, u, matching, new boolean[n1]))
                ++matches;
        }
        return matches;
    }

    static boolean findPath(List<Integer>[] graph, int u1, int[] matching, boolean[] vis) {
        vis[u1] = true;
        for (int v : graph[u1]) {
            int u2 = matching[v];
            if (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis)) {
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
            List<Integer>[] g = Stream.generate(ArrayList::new).limit(n1).toArray(List[]::new);
            for (int i = 0; i < n1; i++)
                for (int j = 0; j < n2; j++)
                    g[i].add(j);
            int res1 = maxMatching(g, n2);
            int res2 = slowMinVertexCover(g, n2);
            if (res1 != res2)
                throw new RuntimeException();
        }
    }

    static int slowMinVertexCover(List<Integer>[] g, int n2) {
        int n1 = g.length;
        int[] mask = new int[n1];
        for (int i = 0; i < n1; i++)
            for (int j : g[i])
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
