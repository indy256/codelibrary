package graphs.dfs;

import java.util.*;
import java.util.stream.Stream;

public class EulerCycle {

    public static List<Integer> eulerCycleUndirected(List<Integer>[] graph, int u) {
        Set<Long> usedEdges = new HashSet<>();
        int n = graph.length;
        int[] curEdge = new int[n];
        List<Integer> res = new ArrayList<>();
        dfs(graph, curEdge, usedEdges, res, u);
        Collections.reverse(res);
        return res;
    }

    static void dfs(List<Integer>[] graph, int[] curEdge, Set<Long> usedEdges, List<Integer> res, int u) {
        while (curEdge[u] < graph[u].size()) {
            int v = graph[u].get(curEdge[u]++);
            if (usedEdges.add(((long) Math.min(u, v) << 32) + Math.max(u, v)))
                dfs(graph, curEdge, usedEdges, res, v);
        }
        res.add(u);
    }

    public static List<Integer> eulerCycleUndirected2(List<Integer>[] graph, int u) {
        int[] curEdge = new int[graph.length];
        List<Integer> res = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        Set<Long> usedEdges = new HashSet<>();
        stack.add(u);
        while (!stack.isEmpty()) {
            u = stack.pop();
            while (curEdge[u] < graph[u].size()) {
                int v = graph[u].get(curEdge[u]++);
                if (usedEdges.add((((long) Math.min(u, v) << 32) + Math.max(u, v)))) {
                    stack.push(u);
                    u = v;
                }
            }
            res.add(u);
        }
        Collections.reverse(res);
        return res;
    }

    public static List<Integer> eulerCycleDirected(List<Integer>[] graph, int u) {
        int n = graph.length;
        int[] curEdge = new int[n];
        List<Integer> res = new ArrayList<>();
        dfs(graph, curEdge, res, u);
        Collections.reverse(res);
        return res;
    }

    static void dfs(List<Integer>[] graph, int[] curEdge, List<Integer> res, int u) {
        while (curEdge[u] < graph[u].size()) {
            dfs(graph, curEdge, res, graph[u].get(curEdge[u]++));
        }
        res.add(u);
    }

    public static List<Integer> eulerCycleDirected2(List<Integer>[] graph, int v) {
        int[] curEdge = new int[graph.length];
        List<Integer> res = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        stack.add(v);
        while (!stack.isEmpty()) {
            v = stack.pop();
            while (curEdge[v] < graph[v].size()) {
                stack.push(v);
                v = graph[v].get(curEdge[v]++);
            }
            res.add(v);
        }
        Collections.reverse(res);
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 5;
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        g[0].add(1);
        g[1].add(2);
        g[2].add(0);

        g[1].add(3);
        g[3].add(4);
        g[4].add(1);

        System.out.println(eulerCycleDirected(g, 0));
        System.out.println(eulerCycleDirected2(g, 0));

        n = 5;
        g = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        g[0].add(1);
        g[1].add(0);
        g[1].add(2);
        g[2].add(1);
        g[2].add(3);
        g[3].add(2);
        g[0].add(3);
        g[3].add(0);

        g[0].add(4);
        g[4].add(0);
        g[1].add(4);
        g[4].add(1);

        g[0].add(2);
        g[2].add(0);
        g[1].add(3);
        g[3].add(1);

        System.out.println(eulerCycleUndirected(g, 2));
        System.out.println(eulerCycleUndirected2(g, 2));
    }
}
