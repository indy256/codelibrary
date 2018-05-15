package graphs.spanningtree;

import java.util.*;

// https://en.wikipedia.org/wiki/Prim%27s_algorithm in O(E*log(V))
public class PrimHeap {

    public static long mst(List<Edge>[] edges, int[] pred) {
        int n = edges.length;
        Arrays.fill(pred, -1);
        boolean[] used = new boolean[n];
        int[] prio = new int[n];
        Arrays.fill(prio, Integer.MAX_VALUE);
        prio[0] = 0;
        PriorityQueue<Long> q = new PriorityQueue<>();
        q.add(0L);
        long res = 0;

        while (!q.isEmpty()) {
            long cur = q.poll();
            int u = (int) cur;
            if (used[u])
                continue;
            used[u] = true;
            res += cur >>> 32;
            for (Edge e : edges[u]) {
                int v = e.t;
                if (!used[v] && prio[v] > e.cost) {
                    prio[v] = e.cost;
                    pred[v] = u;
                    q.add(((long) prio[v] << 32) + v);
                }
            }
        }
        return res;
    }

    static class Edge {
        int t, cost;

        public Edge(int t, int cost) {
            this.t = t;
            this.cost = cost;
        }
    }

    // Usage example
    public static void main(String[] args) {
        int[][] cost = {{0, 1, 2}, {1, 0, 3}, {2, 3, 0}};
        int n = cost.length;
        List<Edge>[] edges = new List[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    edges[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        int[] pred = new int[n];
        System.out.println(mst(edges, pred));
    }
}
