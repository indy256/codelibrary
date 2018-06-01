package graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GraphCycleDetection {

    public static int[] findCycle(List<Integer>[] graph) {
        int n = graph.length;
        int[] color = new int[n];
        int[] next = new int[n];
        for (int u = 0; u < n; u++) {
            int cycleStart = dfs(graph, u, color, next);
            if (cycleStart != -1) {
                List<Integer> cycle = new ArrayList<>();
                cycle.add(cycleStart);
                for (int i = next[cycleStart]; i != cycleStart; i = next[i]) {
                    cycle.add(i);
                }
                cycle.add(cycleStart);
                return cycle.stream().mapToInt(Integer::intValue).toArray();
            }
        }
        return null;
    }

    static int dfs(List<Integer>[] graph, int u, int[] color, int[] next) {
        color[u] = 1;
        for (int v : graph[u]) {
            next[u] = v;
            if (color[v] == 0) {
                int cycleStart = dfs(graph, v, color, next);
                if (cycleStart != -1) {
                    return cycleStart;
                }
            } else if (color[v] == 1) {
                return v;
            }
        }
        color[u] = 2;
        return -1;
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] graph = Stream.generate(ArrayList::new).limit(3).toArray(List[]::new);
        graph[0].add(1);
        graph[1].add(2);
        graph[2].add(0);
        int[] cycle = findCycle(graph);
        System.out.println(Arrays.toString(cycle));
    }
}
