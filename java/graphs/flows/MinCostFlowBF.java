package graphs.flows;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Minimum-cost_flow_problem in O(E * V * FLOW)
// negative-cost edges are allowed
// negative-cost cycles are not allowed
public class MinCostFlowBF {
    List<Edge>[] graph;

    public MinCostFlowBF(int nodes) {
        graph = Stream.generate(ArrayList::new).limit(nodes).toArray(List[] ::new);
    }

    class Edge {
        int to, rev, cap, f, cost;

        Edge(int to, int rev, int cap, int cost) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
            this.cost = cost;
        }
    }

    public void addEdge(int s, int t, int cap, int cost) {
        graph[s].add(new Edge(t, graph[t].size(), cap, cost));
        graph[t].add(new Edge(s, graph[s].size() - 1, 0, -cost));
    }

    void bellmanFord(int s, int[] dist, int[] prevnode, int[] prevedge, int[] curflow) {
        int n = graph.length;
        Arrays.fill(dist, 0, n, Integer.MAX_VALUE);
        dist[s] = 0;
        curflow[s] = Integer.MAX_VALUE;
        boolean[] inqueue = new boolean[n];
        int[] q = new int[n];
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; qh != qt; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (int i = 0; i < graph[u].size(); i++) {
                Edge e = graph[u].get(i);
                if (e.f >= e.cap)
                    continue;
                int v = e.to;
                int ndist = dist[u] + e.cost;
                if (dist[v] > ndist) {
                    dist[v] = ndist;
                    prevnode[v] = u;
                    prevedge[v] = i;
                    curflow[v] = Math.min(curflow[u], e.cap - e.f);
                    if (!inqueue[v]) {
                        inqueue[v] = true;
                        q[qt++ % n] = v;
                    }
                }
            }
        }
    }

    public int[] minCostFlow(int s, int t, int maxf) {
        int n = graph.length;
        int[] dist = new int[n];
        int[] curflow = new int[n];
        int[] prevedge = new int[n];
        int[] prevnode = new int[n];

        int flow = 0;
        int flowCost = 0;
        while (flow < maxf) {
            bellmanFord(s, dist, prevnode, prevedge, curflow);
            if (dist[t] == Integer.MAX_VALUE)
                break;
            int df = Math.min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                Edge e = graph[prevnode[v]].get(prevedge[v]);
                e.f += df;
                graph[v].get(e.rev).f -= df;
                flowCost += df * e.cost;
            }
        }
        return new int[] {flow, flowCost};
    }

    // Usage example
    public static void main(String[] args) {
        MinCostFlowBF mcf = new MinCostFlowBF(3);
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
