package graphs.shortestpaths;

import java.util.*;
import java.util.stream.Stream;

public class BellmanFord {

    static final int INF = Integer.MAX_VALUE / 2;

    public static class Edge {
        final int v, cost;

        public Edge(int v, int cost) {
            this.v = v;
            this.cost = cost;
        }
    }

    public static boolean bellmanFord(List<Edge>[] graph, int s, int[] dist, int[] pred) {
        Arrays.fill(pred, -1);
        Arrays.fill(dist, INF);
        dist[s] = 0;
        int n = graph.length;
        for (int step = 0; step < n; step++) {
            boolean updated = false;
            for (int u = 0; u < n; u++) {
                if (dist[u] == INF) continue;
                for (Edge e : graph[u]) {
                    if (dist[e.v] > dist[u] + e.cost) {
                        dist[e.v] = dist[u] + e.cost;
                        dist[e.v] = Math.max(dist[e.v], -INF);
                        pred[e.v] = u;
                        updated = true;
                    }
                }
            }
            if (!updated)
                return true;
        }
        // a negative cycle exists
        return false;
    }

    public static int[] findNegativeCycle(List<Edge>[] graph) {
        int n = graph.length;
        int[] pred = new int[n];
        Arrays.fill(pred, -1);
        int[] dist = new int[n];
        int last = -1;
        for (int step = 0; step < n; step++) {
            last = -1;
            for (int u = 0; u < n; u++) {
                if (dist[u] == INF) continue;
                for (Edge e : graph[u]) {
                    if (dist[e.v] > dist[u] + e.cost) {
                        dist[e.v] = dist[u] + e.cost;
                        dist[e.v] = Math.max(dist[e.v], -INF);
                        pred[e.v] = u;
                        last = e.v;
                    }
                }
            }
            if (last == -1)
                return null;
        }
        for (int i = 0; i < n; i++) {
            last = pred[last];
        }
        int[] p = new int[n];
        int cnt = 0;
        for (int u = last; u != last || cnt == 0; u = pred[u]) {
            p[cnt++] = u;
        }
        int[] cycle = new int[cnt];
        for (int i = 0; i < cycle.length; i++) {
            cycle[i] = p[--cnt];
        }
        return cycle;
    }

    // Usage example
    public static void main(String[] args) {
        List<Edge>[] graph = Stream.generate(ArrayList::new).limit(4).toArray(List[]::new);
        graph[0].add(new Edge(1, 1));
        graph[1].add(new Edge(0, 1));
        graph[1].add(new Edge(2, 1));
        graph[2].add(new Edge(3, -10));
        graph[3].add(new Edge(1, 1));
        int[] cycle = findNegativeCycle(graph);
        System.out.println(Arrays.toString(cycle));
    }
}
