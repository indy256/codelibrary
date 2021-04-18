package graphs.matchings;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V * E)
public class MaxBipartiteMatchingEV {
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

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(2).toArray(List[] ::new);
        g[0].add(0);
        g[0].add(1);
        g[1].add(1);
        System.out.println(2 == maxMatching(g, 2));
    }
}
