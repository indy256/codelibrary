package graphs.shortestpaths;

import java.util.*;
import java.util.stream.Stream;
import structures.BinaryHeapIndexed;

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
public class DijkstraCustomHeap {
    // calculate shortest paths in O(E*log(V)) time and O(V) memory
    public static void shortestPaths(List<Edge>[] graph, int s, int[] prio, int[] pred) {
        Arrays.fill(pred, -1);
        Arrays.fill(prio, Integer.MAX_VALUE);
        prio[s] = 0;
        BinaryHeapIndexed h = new BinaryHeapIndexed(graph.length);
        h.add(s, 0);
        while (h.size != 0) {
            int u = h.removeMin();
            for (Edge e : graph[u]) {
                int v = e.t;
                int nprio = prio[u] + e.cost;
                if (prio[v] > nprio) {
                    if (prio[v] == Integer.MAX_VALUE)
                        h.add(v, nprio);
                    else
                        h.changeValue(v, nprio);
                    prio[v] = nprio;
                    pred[v] = u;
                }
            }
        }
    }

    public static class Edge {
        int t;
        int cost;

        public Edge(int t, int cost) {
            this.t = t;
            this.cost = cost;
        }
    }

    // Usage example
    public static void main(String[] args) {
        int[][] cost = {{0, 3, 2}, {0, 0, -2}, {0, 0, 0}};
        int n = cost.length;
        List<Edge>[] edges = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    edges[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        int[] dist = new int[n];
        int[] pred = new int[n];
        shortestPaths(edges, 0, dist, pred);
        System.out.println(0 == dist[0]);
        System.out.println(3 == dist[1]);
        System.out.println(1 == dist[2]);
        System.out.println(-1 == pred[0]);
        System.out.println(0 == pred[1]);
        System.out.println(1 == pred[2]);
    }
}
