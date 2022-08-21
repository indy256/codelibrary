package graphs.flows;

import java.util.Arrays;

public class MinCostFlowSimple {
    int[][] capacity;
    int[][] cost;

    public MinCostFlowSimple(int n) {
        capacity = new int[n][n];
        cost = new int[n][n];
    }

    public int[] minCostFlow(int s, int t, int maxf) {
        int n = capacity.length;
        int[] d = new int[n];
        int[] p = new int[n];
        int flow = 0, flowCost = 0;
        for (; flow < maxf; ++flow) {
            Arrays.fill(d, Integer.MAX_VALUE);
            d[s] = 0;
            for (int i = 0; i < n - 1; i++)
                for (int j = 0; j < n; j++)
                    for (int k = 0; k < n; k++)
                        if (capacity[j][k] > 0 && d[j] < Integer.MAX_VALUE && d[k] > d[j] + cost[j][k]) {
                            d[k] = d[j] + cost[j][k];
                            p[k] = j;
                        }
            if (d[t] == Integer.MAX_VALUE)
                break;
            flowCost += d[t];
            for (int v = t; v != s; v = p[v]) {
                --capacity[p[v]][v];
                ++capacity[v][p[v]];
            }
        }
        return new int[] {flow, flowCost};
    }

    public void addEdge(int u, int v, int edgeCapacity, int edgeCost) {
        capacity[u][v] = edgeCapacity;
        cost[u][v] = edgeCost;
        cost[v][u] = -edgeCost;
    }

    // Usage example
    public static void main(String[] args) {
        MinCostFlowSimple mcf = new MinCostFlowSimple(3);
        mcf.addEdge(0, 1, 3, 1);
        mcf.addEdge(0, 2, 2, 1);
        mcf.addEdge(1, 2, 2, 1);
        int[] res = mcf.minCostFlow(0, 2, Integer.MAX_VALUE);
        int flow = res[0];
        int flowCost = res[1];
        System.out.println(4 == flow);
        System.out.println(6 == flowCost);
    }
}
