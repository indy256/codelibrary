package graphs.dfs;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
public class SccKosaraju {

    public static List<List<Integer>> scc(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (!used[i])
                dfs(graph, used, order, i);

        List<Integer>[] reverseGraph = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        for (int i = 0; i < n; i++)
            for (int j : graph[i])
                reverseGraph[j].add(i);

        List<List<Integer>> components = new ArrayList<>();
        Arrays.fill(used, false);
        Collections.reverse(order);

        for (int u : order)
            if (!used[u]) {
                List<Integer> component = new ArrayList<>();
                dfs(reverseGraph, used, component, u);
                components.add(component);
            }

        return components;
    }

    static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
        used[u] = true;
        for (int v : graph[u])
            if (!used[v])
                dfs(graph, used, res, v);
        res.add(u);
    }

    // DAG of strongly connected components
    public static List<Integer>[] sccGraph(List<Integer>[] graph, List<List<Integer>> components) {
        int[] comp = new int[graph.length];
        for (int i = 0; i < components.size(); i++)
            for (int u : components.get(i))
                comp[u] = i;
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(components.size()).toArray(List[]::new);
        Set<Long> edges = new HashSet<>();
        for (int u = 0; u < graph.length; u++)
            for (int v : graph[u])
                if (comp[u] != comp[v] && edges.add(((long) comp[u] << 32) + comp[v]))
                    g[comp[u]].add(comp[v]);
        return g;
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(3).toArray(List[]::new);
        g[2].add(0);
        g[2].add(1);
        g[0].add(1);
        g[1].add(0);

        List<List<Integer>> components = scc(g);
        System.out.println(components);
        System.out.println(Arrays.toString(sccGraph(g, components)));
    }
}
