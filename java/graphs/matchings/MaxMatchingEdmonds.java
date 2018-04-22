package graphs.matchings;

import java.util.*;
import java.util.stream.*;

// https://en.wikipedia.org/wiki/Blossom_algorithm in O(V^3)
public class MaxMatchingEdmonds {

    public static int maxMatching(List<Integer>[] graph) {
        int n = graph.length;
        int[] match = new int[n];
        Arrays.fill(match, -1);
        int[] p = new int[n];
        for (int i = 0; i < n; ++i) {
            if (match[i] == -1) {
                int v = findPath(graph, match, p, i);
                while (v != -1) {
                    int pv = p[v];
                    int ppv = match[pv];
                    match[v] = pv;
                    match[pv] = v;
                    v = ppv;
                }
            }
        }
        return (int) Arrays.stream(match).filter(x -> x != -1).count() / 2;
    }

    static int findPath(List<Integer>[] graph, int[] match, int[] p, int root) {
        Arrays.fill(p, -1);
        int n = graph.length;
        int[] base = IntStream.range(0, n).toArray();

        boolean[] used = new boolean[n];
        int[] q = new int[n];
        int qt = 0;
        used[root] = true;
        q[qt++] = root;
        for (int qh = 0; qh < qt; qh++) {
            int u = q[qh];
            for (int v : graph[u]) {
                if (base[u] == base[v] || match[u] == v) continue;
                if (v == root || match[v] != -1 && p[match[v]] != -1) {
                    int curbase = lca(match, base, p, u, v);
                    boolean[] blossom = new boolean[n];
                    markPath(match, base, blossom, p, u, curbase, v);
                    markPath(match, base, blossom, p, v, curbase, u);
                    for (int i = 0; i < n; ++i)
                        if (blossom[base[i]]) {
                            base[i] = curbase;
                            if (!used[i]) {
                                used[i] = true;
                                q[qt++] = i;
                            }
                        }
                } else if (p[v] == -1) {
                    p[v] = u;
                    if (match[v] == -1)
                        return v;
                    v = match[v];
                    used[v] = true;
                    q[qt++] = v;
                }
            }
        }
        return -1;
    }

    static void markPath(int[] match, int[] base, boolean[] blossom, int[] p, int u, int curbase, int child) {
        for (; base[u] != curbase; u = p[match[u]]) {
            blossom[base[u]] = blossom[base[match[u]]] = true;
            p[u] = child;
            child = match[u];
        }
    }

    static int lca(int[] match, int[] base, int[] p, int a, int b) {
        boolean[] used = new boolean[match.length];
        while (true) {
            a = base[a];
            used[a] = true;
            if (match[a] == -1) break;
            a = p[match[a]];
        }
        while (true) {
            b = base[b];
            if (used[b]) return b;
            b = p[match[b]];
        }
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(10) + 1;
            boolean[][] g = new boolean[n][n];
            List<Integer>[] graph = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    g[i][j] = g[j][i] = rnd.nextBoolean();
                    if (g[i][j]) {
                        graph[i].add(j);
                        graph[j].add(i);
                    }
                }
            }
            int res1 = maxMatching(graph);
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
