package graphs.flows;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)
public class MaxFlowDinic {
    List<Edge>[] graph;
    int[] dist;

    public MaxFlowDinic(int nodes) {
        graph = Stream.generate(ArrayList::new).limit(nodes).toArray(List[] ::new);
        dist = new int[nodes];
    }

    class Edge {
        int t, rev, cap, f;

        public Edge(int t, int rev, int cap) {
            this.t = t;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public void addBidiEdge(int s, int t, int cap) {
        graph[s].add(new Edge(t, graph[t].size(), cap));
        graph[t].add(new Edge(s, graph[s].size() - 1, 0));
    }

    boolean dinicBfs(int src, int dest) {
        Arrays.fill(dist, -1);
        dist[src] = 0;
        int[] q = new int[graph.length];
        int sizeQ = 0;
        q[sizeQ++] = src;
        for (int i = 0; i < sizeQ; i++) {
            int u = q[i];
            for (Edge e : graph[u]) {
                if (dist[e.t] < 0 && e.f < e.cap) {
                    dist[e.t] = dist[u] + 1;
                    q[sizeQ++] = e.t;
                }
            }
        }
        return dist[dest] >= 0;
    }

    int dinicDfs(int[] ptr, int dest, int u, int f) {
        if (u == dest)
            return f;
        for (; ptr[u] < graph[u].size(); ++ptr[u]) {
            Edge e = graph[u].get(ptr[u]);
            if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
                int df = dinicDfs(ptr, dest, e.t, Math.min(f, e.cap - e.f));
                if (df > 0) {
                    e.f += df;
                    graph[e.t].get(e.rev).f -= df;
                    return df;
                }
            }
        }
        return 0;
    }

    public int maxFlow(int src, int dest) {
        int flow = 0;
        while (dinicBfs(src, dest)) {
            int[] ptr = new int[graph.length];
            for (int df; (df = dinicDfs(ptr, dest, src, Integer.MAX_VALUE)) != 0; flow += df)
                ;
        }
        return flow;
    }

    // invoke after maxFlow()
    public boolean[] minCut() {
        boolean[] cut = new boolean[graph.length];
        for (int i = 0; i < cut.length; ++i) cut[i] = dist[i] != -1;
        return cut;
    }

    void clearFlow() {
        for (List<Edge> edges : graph)
            for (Edge edge : edges) edge.f = 0;
    }

    // Usage example
    public static void main(String[] args) {
        MaxFlowDinic flow = new MaxFlowDinic(3);
        flow.addBidiEdge(0, 1, 3);
        flow.addBidiEdge(0, 2, 2);
        flow.addBidiEdge(1, 2, 2);
        System.out.println(4 == flow.maxFlow(0, 2));
    }
}
