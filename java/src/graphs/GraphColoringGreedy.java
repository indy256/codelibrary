package graphs;

import java.util.*;
import java.util.stream.Stream;

public class GraphColoringGreedy {

    // similar to DSatur coloring
    public static int[] color(List<Integer>[] graph) {
        int n = graph.length;
        BitSet[] used = new BitSet[n];
        int[] colors = new int[n];
        PriorityQueue<Long> q = new PriorityQueue<>(n);
        for (int i = 0; i < n; i++) {
            used[i] = new BitSet();
            colors[i] = -1;
            q.add((long) i);
        }
        for (int i = 0; i < n; i++) {
            int bestu;
            while (true) {
                bestu = q.remove().intValue();
                if (colors[bestu] == -1)
                    break;
            }
            int c = used[bestu].nextClearBit(0);
            colors[bestu] = c;
            for (int v : graph[bestu]) {
                if (!used[v].get(c)) {
                    used[v].set(c);
                    if (colors[v] == -1)
                        q.add(v - ((long) used[v].cardinality() << 32));
                }
            }
        }
        return colors;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 5;
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i].add((i + 1) % n);
                g[(i + 1) % n].add(i);
            }
        }
        System.out.println(Arrays.toString(color(g)));
    }
}
