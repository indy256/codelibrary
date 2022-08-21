package graphs.flows;

import java.util.*;

public class MinCostFlowTest {
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int test = 0; test < 10_000; test++) {
            int n = rnd.nextInt(10) + 2;
            Set<Integer> edges = new HashSet<>();
            int[] path = getRandomPermutation(n, rnd);
            for (int i = 0; i + 1 < n; i++) {
                edges.add((path[i] << 16) + path[i + 1]);
            }
            int moreEdges = rnd.nextInt((n - 1) * (n - 2) / 2 + 1);
            for (int i = 0; i < moreEdges; i++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                if (u == v || edges.contains((u << 16) + v) || edges.contains((v << 16) + u)) {
                    --i;
                } else {
                    edges.add((u << 16) + v);
                }
            }

            MinCostFlowBF f1 = new MinCostFlowBF(n);
            MinCostFlowDijkstra f2 = new MinCostFlowDijkstra(n);
            MinCostFlowSimple f3 = new MinCostFlowSimple(n);
            for (int edge : edges) {
                int u = edge >>> 16;
                int v = edge & 0xFFFF;
                int capacity = rnd.nextInt(10);
                int cost = rnd.nextInt(10);
                f1.addEdge(u, v, capacity, cost);
                f2.addEdge(u, v, capacity, cost);
                f3.addEdge(u, v, capacity, cost);
            }
            int maxf = Integer.MAX_VALUE;
            int s = path[0];
            int t = path[n - 1];
            int[] res1 = f1.minCostFlow(s, t, maxf);
            int[] res2 = f2.minCostFlow(s, t, maxf);
            int[] res3 = f3.minCostFlow(s, t, maxf);

            if (!Arrays.equals(res1, res2) || !Arrays.equals(res1, res3))
                throw new RuntimeException();
        }
    }

    static int[] getRandomPermutation(int n, Random rnd) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int j = rnd.nextInt(i + 1);
            res[i] = res[j];
            res[j] = i;
        }
        return res;
    }
}
