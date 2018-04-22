package graphs.shortestpaths;

import java.util.*;
import java.util.stream.Stream;

public class DijkstraCustomHeap {

    public static void shortestPaths(List<Edge>[] edges, int s, long[] prio, int[] pred) {
        Arrays.fill(pred, -1);
        Arrays.fill(prio, Long.MAX_VALUE);
        prio[s] = 0;
        BinaryHeap h = new BinaryHeap(edges.length);
        h.add(s, 0);
        while (h.size != 0) {
            int u = h.remove();
            for (Edge e : edges[u]) {
                int v = e.t;
                long nprio = prio[u] + e.cost;
                if (prio[v] > nprio) {
                    if (prio[v] == Long.MAX_VALUE)
                        h.add(v, nprio);
                    else
                        h.increasePriority(v, nprio);
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

    static class BinaryHeap {
        long[] heap;
        int[] pos2Id;
        int[] id2Pos;
        int size;

        public BinaryHeap(int n) {
            heap = new long[n];
            pos2Id = new int[n];
            id2Pos = new int[n];
        }

        public int remove() {
            int removedId = pos2Id[0];
            heap[0] = heap[--size];
            pos2Id[0] = pos2Id[size];
            id2Pos[pos2Id[0]] = 0;
            down(0);
            return removedId;
        }

        public void add(int id, long value) {
            heap[size] = value;
            pos2Id[size] = id;
            id2Pos[id] = size;
            up(size++);
        }

        public void increasePriority(int id, long value) {
            heap[id2Pos[id]] = value;
            up(id2Pos[id]);
        }

        void up(int pos) {
            while (pos > 0) {
                int parent = (pos - 1) / 2;
                if (heap[pos] >= heap[parent])
                    break;
                swap(pos, parent);
                pos = parent;
            }
        }

        void down(int pos) {
            while (true) {
                int child = 2 * pos + 1;
                if (child >= size)
                    break;
                if (child + 1 < size && heap[child + 1] < heap[child])
                    ++child;
                if (heap[pos] <= heap[child])
                    break;
                swap(pos, child);
                pos = child;
            }
        }

        void swap(int i, int j) {
            long tt = heap[i];
            heap[i] = heap[j];
            heap[j] = tt;
            int t = pos2Id[i];
            pos2Id[i] = pos2Id[j];
            pos2Id[j] = t;
            id2Pos[pos2Id[i]] = i;
            id2Pos[pos2Id[j]] = j;
        }
    }

    // Usage example
    public static void main(String[] args) {
        int[][] cost = {{0, 3, 2}, {0, 0, -2}, {0, 0, 0}};
        int n = cost.length;
        List<Edge>[] edges = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    edges[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        long[] dist = new long[n];
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
