package graphs.flows;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm in O(V * E^2)
public class MaxFlowEdmondsKarp {
    List<Edge>[] graph;

    public MaxFlowEdmondsKarp(int nodes) {
        graph = Stream.generate(ArrayList::new).limit(nodes).toArray(List[] ::new);
    }

    class Edge {
        int s, t, rev, cap, f;

        public Edge(int s, int t, int rev, int cap) {
            this.s = s;
            this.t = t;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public void addBidiEdge(int s, int t, int cap) {
        graph[s].add(new Edge(s, t, graph[t].size(), cap));
        graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
    }

    public int maxFlow(int s, int t) {
        int flow = 0;
        int[] q = new int[graph.length];
        while (true) {
            int qt = 0;
            q[qt++] = s;
            Edge[] pred = new Edge[graph.length];
            for (int qh = 0; qh < qt && pred[t] == null; qh++) {
                int cur = q[qh];
                for (Edge e : graph[cur]) {
                    if (pred[e.t] == null && e.cap > e.f) {
                        pred[e.t] = e;
                        q[qt++] = e.t;
                    }
                }
            }
            if (pred[t] == null)
                break;
            int df = Integer.MAX_VALUE;
            for (int u = t; u != s; u = pred[u].s) df = Math.min(df, pred[u].cap - pred[u].f);
            for (int u = t; u != s; u = pred[u].s) {
                pred[u].f += df;
                graph[pred[u].t].get(pred[u].rev).f -= df;
            }
            flow += df;
        }
        return flow;
    }

    // Usage example
    public static void main(String[] args) {
        MaxFlowEdmondsKarp flow = new MaxFlowEdmondsKarp(3);
        flow.addBidiEdge(0, 1, 3);
        flow.addBidiEdge(0, 2, 2);
        flow.addBidiEdge(1, 2, 2);
        System.out.println(4 == flow.maxFlow(0, 2));
    }
}
