package graphs.matchings;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Hopcroft–Karp_algorithm
// time complexity: O(E * sqrt(V))
public class MaxBipartiteMatchingHopcroftKarpEsqrtV {
    public static int maxMatching(List<Integer>[] graph, int n2) {
        int n1 = graph.length;
        int[] dist = new int[n1];
        int[] matching = new int[n2];
        Arrays.fill(matching, -1);
        boolean[] used = new boolean[n1];
        for (int res = 0;;) {
            bfs(graph, used, matching, dist);
            boolean[] vis = new boolean[n1];
            int f = 0;
            for (int u = 0; u < n1; ++u)
                if (!used[u] && dfs(graph, vis, used, matching, dist, u))
                    ++f;
            if (f == 0)
                return res;
            res += f;
        }
    }

    static void bfs(List<Integer>[] graph, boolean[] used, int[] matching, int[] dist) {
        Arrays.fill(dist, -1);
        int n1 = graph.length;
        int[] Q = new int[n1];
        int sizeQ = 0;
        for (int u = 0; u < n1; ++u) {
            if (!used[u]) {
                Q[sizeQ++] = u;
                dist[u] = 0;
            }
        }
        for (int i = 0; i < sizeQ; i++) {
            int u1 = Q[i];
            for (int v : graph[u1]) {
                int u2 = matching[v];
                if (u2 >= 0 && dist[u2] < 0) {
                    dist[u2] = dist[u1] + 1;
                    Q[sizeQ++] = u2;
                }
            }
        }
    }

    static boolean dfs(List<Integer>[] graph, boolean[] vis, boolean[] used, int[] matching, int[] dist, int u1) {
        vis[u1] = true;
        for (int v : graph[u1]) {
            int u2 = matching[v];
            if (u2 < 0 || !vis[u2] && dist[u2] == dist[u1] + 1 && dfs(graph, vis, used, matching, dist, u2)) {
                matching[v] = u1;
                used[u1] = true;
                return true;
            }
        }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] graph = Stream.generate(ArrayList::new).limit(2).toArray(List[] ::new);
        graph[0].add(0);
        graph[0].add(1);
        graph[1].add(1);
        System.out.println(2 == maxMatching(graph, 2));
    }
}
