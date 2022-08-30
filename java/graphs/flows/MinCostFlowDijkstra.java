package graphs.flows;

import java.util.*;
import java.util.stream.Stream;
import structures.BinaryHeapIndexed;
import structures.RadixHeapIndexed;

// https://cp-algorithms.com/graph/min_cost_flow.html in O(E * V + min(E * logV * FLOW, V^2 * FLOW))
// negative-cost edges are allowed
// negative-cost cycles are not allowed
public class MinCostFlowDijkstra {
    List<Edge>[] graph;

    public MinCostFlowDijkstra(int nodes) {
        graph = Stream.generate(ArrayList::new).limit(nodes).toArray(List[] ::new);
    }

    static class Edge {
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

    void bellmanFord(int s, int[] dist) {
        int n = graph.length;
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        boolean[] inqueue = new boolean[n];
        int[] q = new int[n];
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; qh != qt; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (int i = 0; i < graph[u].size(); i++) {
                Edge e = graph[u].get(i);
                if (e.cap <= e.f)
                    continue;
                int v = e.to;
                int ndist = dist[u] + e.cost;
                if (dist[v] > ndist) {
                    dist[v] = ndist;
                    if (!inqueue[v]) {
                        inqueue[v] = true;
                        q[qt++ % n] = v;
                    }
                }
            }
        }
    }

    void dijkstraSparse(
        int s, int t, int[] pot, int[] dist, boolean[] finished, int[] curflow, int[] prevnode, int[] prevedge) {
        BinaryHeapIndexed h = new BinaryHeapIndexed(graph.length);
        // RadixHeapIndexed h = new RadixHeapIndexed(graph.length);
        h.add(s, 0);
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        Arrays.fill(finished, false);
        curflow[s] = Integer.MAX_VALUE;
        while (!finished[t] && h.size != 0) {
            int u = h.removeMin();
            finished[u] = true;
            for (int i = 0; i < graph[u].size(); i++) {
                Edge e = graph[u].get(i);
                if (e.f >= e.cap)
                    continue;
                int v = e.to;
                int nprio = dist[u] + e.cost + pot[u] - pot[v];
                if (dist[v] > nprio) {
                    if (dist[v] == Integer.MAX_VALUE)
                        h.add(v, nprio);
                    else
                        h.changeValue(v, nprio);
                    dist[v] = nprio;
                    prevnode[v] = u;
                    prevedge[v] = i;
                    curflow[v] = Math.min(curflow[u], e.cap - e.f);
                }
            }
        }
    }

    void dijkstraDense(
        int s, int t, int[] pot, int[] dist, boolean[] finished, int[] curflow, int[] prevnode, int[] prevedge) {
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        int n = graph.length;
        Arrays.fill(finished, false);
        curflow[s] = Integer.MAX_VALUE;
        for (int i = 0; i < n && !finished[t]; i++) {
            int u = -1;
            for (int j = 0; j < n; j++)
                if (!finished[j] && (u == -1 || dist[u] > dist[j]))
                    u = j;
            if (dist[u] == Integer.MAX_VALUE)
                break;
            finished[u] = true;
            for (int k = 0; k < graph[u].size(); k++) {
                Edge e = graph[u].get(k);
                if (e.f >= e.cap)
                    continue;
                int v = e.to;
                int nprio = dist[u] + e.cost + pot[u] - pot[v];
                if (dist[v] > nprio) {
                    dist[v] = nprio;
                    prevnode[v] = u;
                    prevedge[v] = k;
                    curflow[v] = Math.min(curflow[u], e.cap - e.f);
                }
            }
        }
    }

    public int[] minCostFlow(int s, int t, int maxf) {
        int n = graph.length;
        int[] pot = new int[n];
        int[] dist = new int[n];
        boolean[] finished = new boolean[n];
        int[] curflow = new int[n];
        int[] prevedge = new int[n];
        int[] prevnode = new int[n];

        bellmanFord(s, pot); // this can be commented out if edges costs are non-negative
        int flow = 0;
        int flowCost = 0;
        while (flow < maxf) {
            dijkstraSparse(s, t, pot, dist, finished, curflow, prevnode, prevedge); // O(E*logV)
            // dijkstraDense(s, t, pot, dist, finished, curflow, prevnode, prevedge); // O(V^2)
            if (dist[t] == Integer.MAX_VALUE)
                break;
            for (int i = 0; i < n; i++)
                if (finished[i])
                    pot[i] += dist[i] - dist[t];
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
        MinCostFlowDijkstra mcf = new MinCostFlowDijkstra(3);
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
