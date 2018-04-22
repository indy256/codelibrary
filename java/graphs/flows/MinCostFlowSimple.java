package graphs.flows;

import java.util.Arrays;

public class MinCostFlowSimple {

    public static int[] minCostFlow(int[][] cap, int[][] cost, int s, int t) {
        int n = cap.length;
        int[] d = new int[n];
        int[] p = new int[n];
        for (int flow = 0, flowCost = 0; ; ++flow) {
            Arrays.fill(d, Integer.MAX_VALUE);
            d[s] = 0;
            for (int i = 0; i < n - 1; i++)
                for (int j = 0; j < n; j++)
                    for (int k = 0; k < n; k++)
                        if (cap[j][k] > 0 && d[j] < Integer.MAX_VALUE && d[k] > d[j] + cost[j][k]) {
                            d[k] = d[j] + cost[j][k];
                            p[k] = j;
                        }
            if (d[t] == Integer.MAX_VALUE)
                return new int[]{flowCost, flow};
            flowCost += d[t];
            for (int v = t; v != s; v = p[v]) {
                --cap[p[v]][v];
                ++cap[v][p[v]];
            }
        }
    }

    public static void addEdge(int[][] cap, int[][] cost, int u, int v, int edgeCapacity, int edgeCost) {
        cap[u][v] = edgeCapacity;
        cost[u][v] = edgeCost;
        cost[v][u] = -edgeCost;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 3;
        int[][] capacity = new int[n][n];
        int[][] cost = new int[n][n];
        addEdge(capacity, cost, 0, 1, 3, 1);
        addEdge(capacity, cost, 0, 2, 2, 1);
        addEdge(capacity, cost, 1, 2, 2, 1);
        int[] costFlow = minCostFlow(capacity, cost, 0, 2);
        System.out.println(6 == costFlow[0]);
        System.out.println(4 == costFlow[1]);
    }
}
