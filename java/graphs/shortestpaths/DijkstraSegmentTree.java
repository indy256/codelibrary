package graphs.shortestpaths;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
public class DijkstraSegmentTree {
    // calculate shortest paths in O(E*log(V)) time and O(V) memory
    public static void shortestPaths(List<Edge>[] graph, int s, long[] prio, int[] pred) {
        Arrays.fill(pred, -1);
        Arrays.fill(prio, Long.MAX_VALUE);
        prio[s] = 0;
        long[] t = new long[graph.length * 2];
        Arrays.fill(t, Long.MAX_VALUE);
        set(t, s, 0);
        while (true) {
            int cur = minIndex(t);
            if (t[cur + t.length / 2] == Long.MAX_VALUE)
                break;
            set(t, cur, Long.MAX_VALUE);
            for (Edge e : graph[cur]) {
                int v = e.t;
                long nprio = prio[cur] + e.cost;
                if (prio[v] > nprio) {
                    prio[v] = nprio;
                    pred[v] = cur;
                    set(t, v, nprio);
                }
            }
        }
    }

    public static class Edge {
        int t;
        int cost;

        public Edge(int t, int cost) {
            this.t = t;
            this.cost = cost;
        }
    }

    static void set(long[] t, int i, long value) {
        i += t.length / 2;
        if (t[i] < value && value != Long.MAX_VALUE)
            return;
        t[i] = value;
        for (; i > 1; i >>= 1) t[i >> 1] = Math.min(t[i], t[i ^ 1]);
    }

    static int minIndex(long[] t) {
        int res = 1;
        while (res < t.length / 2) res = res * 2 + (t[res * 2] > t[1] ? 1 : 0);
        return res - t.length / 2;
    }

    // Usage example
    public static void main(String[] args) {
        int[][] cost = {{0, 3, 2}, {0, 0, -2}, {0, 0, 0}};
        int n = cost.length;
        List<Edge>[] edges = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    edges[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        long[] dist = new long[n];
        int[] pred = new int[n];
        shortestPaths(edges, 0, dist, pred);
        System.out.println(0 == dist[0]);
        System.out.println(3 == dist[1]);
        System.out.println(1 == dist[2]);
        System.out.println(-1 == pred[0]);
        System.out.println(0 == pred[1]);
        System.out.println(1 == pred[2]);
    }
}
