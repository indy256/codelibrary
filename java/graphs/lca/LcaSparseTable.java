package graphs.lca;

import java.util.*;
import java.util.stream.Stream;

// Answering LCA queries in O(1) with O(n*log(n)) preprocessing
public class LcaSparseTable {

    int len;
    int[][] up;
    int[] tin;
    int[] tout;
    int time;

    void dfs(List<Integer>[] tree, int u, int p) {
        tin[u] = time++;
        up[0][u] = p;
        for (int i = 1; i < len; i++)
            up[i][u] = up[i - 1][up[i - 1][u]];
        for (int v : tree[u])
            if (v != p)
                dfs(tree, v, u);
        tout[u] = time++;
    }

    public LcaSparseTable(List<Integer>[] tree, int root) {
        int n = tree.length;
        len = 1;
        while ((1 << len) <= n) ++len;
        up = new int[len][n];
        tin = new int[n];
        tout = new int[n];
        dfs(tree, root, root);
    }

    boolean isParent(int parent, int child) {
        return tin[parent] <= tin[child] && tout[child] <= tout[parent];
    }

    public int lca(int a, int b) {
        if (isParent(a, b))
            return a;
        if (isParent(b, a))
            return b;
        for (int i = len - 1; i >= 0; i--)
            if (!isParent(up[i][a], b))
                a = up[i][a];
        return up[0][a];
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(5).toArray(List[]::new);
        tree[0].add(1);
        tree[1].add(0);
        tree[1].add(2);
        tree[2].add(1);
        tree[3].add(1);
        tree[1].add(3);
        tree[0].add(4);
        tree[4].add(0);

        LcaSparseTable t = new LcaSparseTable(tree, 0);
        System.out.println(1 == t.lca(3, 2));
        System.out.println(0 == t.lca(2, 4));
    }
}
