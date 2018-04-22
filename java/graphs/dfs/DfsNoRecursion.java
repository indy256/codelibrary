package graphs.dfs;

import java.util.*;
import java.util.stream.Stream;

public class DfsNoRecursion {

    public static void dfs(List<Integer>[] graph, int root) {
        int n = graph.length;
        int[] curEdge = new int[n];
        int[] stack = new int[n];
        stack[0] = root;
        for (int top = 0; top >= 0; ) {
            int u = stack[top];
            if (curEdge[u] == 0) {
                System.out.println(u);
            }
            if (curEdge[u] < graph[u].size()) {
                int v = graph[u].get(curEdge[u]++);
                if (curEdge[v] == 0) {
                    stack[++top] = v;
                }
            } else {
                --top;
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] g = Stream.generate(ArrayList::new).limit(3).toArray(List[]::new);

        g[0].add(1);
        g[1].add(0);
        g[0].add(2);

        dfs(g, 0);
        System.out.println();
        dfs(g, 1);
        System.out.println();
        dfs(g, 2);
    }
}
