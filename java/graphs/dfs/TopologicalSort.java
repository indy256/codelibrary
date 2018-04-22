package graphs.dfs;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Topological_sorting
public class TopologicalSort {

    static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> order, int u) {
        used[u] = true;
        for (int v : graph[u])
            if (!used[v])
                dfs(graph, used, order, v);
        order.add(u);
    }

    public static List<Integer> topologicalSort(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (!used[i])
                dfs(graph, used, order, i);
        Collections.reverse(order);
        return order;
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(3).toArray(List[]::new);
        g[2].add(0);
        g[2].add(1);
        g[0].add(1);

        List<Integer> order = topologicalSort(g);
        System.out.println(order);
    }
}
