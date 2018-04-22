package misc;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/2-satisfiability
public class Sat2 {

    static void dfs1(List<Integer>[] graph, boolean[] used, List<Integer> order, int u) {
        used[u] = true;
        for (int v : graph[u])
            if (!used[v])
                dfs1(graph, used, order, v);
        order.add(u);
    }

    static void dfs2(List<Integer>[] reverseGraph, int[] comp, int u, int color) {
        comp[u] = color;
        for (int v : reverseGraph[u])
            if (comp[v] == -1)
                dfs2(reverseGraph, comp, v, color);
    }

    public static boolean[] solve2Sat(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            if (!used[i])
                dfs1(graph, used, order, i);

        List<Integer>[] reverseGraph = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        for (int i = 0; i < n; i++)
            for (int j : graph[i])
                reverseGraph[j].add(i);

        int[] comp = new int[n];
        Arrays.fill(comp, -1);
        for (int i = 0, color = 0; i < n; ++i) {
            int u = order.get(n - i - 1);
            if (comp[u] == -1)
                dfs2(reverseGraph, comp, u, color++);
        }

        for (int i = 0; i < n; ++i)
            if (comp[i] == comp[i ^ 1])
                return null;

        boolean[] res = new boolean[n / 2];
        for (int i = 0; i < n; i += 2)
            res[i / 2] = comp[i] > comp[i ^ 1];
        return res;
    }

    public static void main(String[] args) {
        int n = 6;
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        // (a || b) && (b || !c)
        // !a => b
        // !b => a
        // !b => !c
        // c => b
        int a = 0, na = 1, b = 2, nb = 3, c = 4, nc = 5;
        g[na].add(b);
        g[nb].add(a);
        g[nb].add(nc);
        g[c].add(b);

        boolean[] solution = solve2Sat(g);
        System.out.println(Arrays.toString(solution));
    }
}
