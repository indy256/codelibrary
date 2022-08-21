package graphs.shortestpaths;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
public class DijkstraDense {
    // calculate shortest paths in O(V^2)
    public static void shortestPaths(List<Edge>[] graph, int s, int[] prio, int[] pred) {
        int n = graph.length;
        Arrays.fill(pred, -1);
        Arrays.fill(prio, Integer.MAX_VALUE);
        prio[s] = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || prio[u] > prio[j]))
                    u = j;
            }
            if (prio[u] == Integer.MAX_VALUE)
                break;
            visited[u] = true;

            for (Edge e : graph[u]) {
                int v = e.t;
                int nprio = prio[u] + e.cost;
                if (prio[v] > nprio) {
                    prio[v] = nprio;
                    pred[v] = u;
                }
            }
        }
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
        int[][] cost = {{0, 3, 2}, {0, 0, -2}, {0, 0, 0}};
        int n = cost.length;
        List<Edge>[] graph = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    graph[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        int[] dist = new int[n];
        int[] pred = new int[n];
        shortestPaths(graph, 0, dist, pred);
        System.out.println(0 == dist[0]);
        System.out.println(3 == dist[1]);
        System.out.println(1 == dist[2]);
        System.out.println(-1 == pred[0]);
        System.out.println(0 == pred[1]);
        System.out.println(1 == pred[2]);
    }
}
