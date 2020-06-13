package graphs.matchings;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Blossom_algorithm in O(V^3)
public class MaxGeneralMatchingV3 {
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
        int[] bases = IntStream.range(0, n).toArray();

        boolean[] used = new boolean[n];
        int[] q = new int[n];
        int qt = 0;
        used[root] = true;
        q[qt++] = root;
        for (int qh = 0; qh < qt; qh++) {
            int u = q[qh];
            for (int v : graph[u]) {
                if (bases[u] == bases[v] || match[u] == v)
                    continue;
                if (v == root || match[v] != -1 && p[match[v]] != -1) {
                    int base = lca(match, bases, p, u, v);
                    boolean[] blossom = new boolean[n];
                    markPath(match, bases, blossom, p, u, base, v);
                    markPath(match, bases, blossom, p, v, base, u);
                    for (int i = 0; i < n; ++i)
                        if (blossom[bases[i]]) {
                            bases[i] = base;
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

    static void markPath(int[] match, int[] bases, boolean[] blossom, int[] p, int u, int base, int child) {
        for (; bases[u] != base; u = p[match[u]]) {
            blossom[bases[u]] = blossom[bases[match[u]]] = true;
            p[u] = child;
            child = match[u];
        }
    }

    static int lca(int[] match, int[] base, int[] p, int a, int b) {
        boolean[] used = new boolean[match.length];
        while (true) {
            a = base[a];
            used[a] = true;
            if (match[a] == -1)
                break;
            a = p[match[a]];
        }
        while (true) {
            b = base[b];
            if (used[b])
                return b;
            b = p[match[b]];
        }
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] graph = Stream.generate(ArrayList::new).limit(4).toArray(List[] ::new);
        graph[0].add(1);
        graph[1].add(0);
        graph[2].add(1);
        graph[1].add(2);
        graph[2].add(0);
        graph[0].add(2);
        graph[3].add(0);
        graph[0].add(3);
        System.out.println(2 == maxMatching(graph));
    }
}
