package structures;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

// Heavy-light decomposition with path queries. Query complexity is O(log^2(n)).
// Based on the code from http://codeforces.com/blog/entry/22072
public class HeavyLight {

    List<Integer>[] tree;
    boolean valuesOnVertices; // true - values on vertices, false - values on edges
    public SegmentTree segmentTree;
    int[] parent;
    int[] heavy;
    int[] depth;
    int[] pathRoot;
    int[] pos;

    public HeavyLight(List<Integer>[] tree, boolean valuesOnVertices) {
        this.tree = tree;
        this.valuesOnVertices = valuesOnVertices;
        int n = tree.length;
        segmentTree = new SegmentTree(n);

        parent = new int[n];
        heavy = new int[n];
        depth = new int[n];
        pathRoot = new int[n];
        pos = new int[n];

        Arrays.fill(heavy, -1);
        parent[0] = -1;
        depth[0] = 0;
        dfs(0);
        for (int u = 0, p = 0; u < n; u++) {
            if (parent[u] == -1 || heavy[parent[u]] != u) {
                for (int v = u; v != -1; v = heavy[v]) {
                    pathRoot[v] = u;
                    pos[v] = p++;
                }
            }
        }
    }

    int dfs(int u) {
        int size = 1;
        int maxSubtree = 0;
        for (int v : tree[u]) {
            if (v == parent[u])
                continue;
            parent[v] = u;
            depth[v] = depth[u] + 1;
            int subtree = dfs(v);
            if (maxSubtree < subtree) {
                maxSubtree = subtree;
                heavy[u] = v;
            }
            size += subtree;
        }
        return size;
    }

    public long get(int u, int v) {
        long[] res = new long[]{0};
        processPath(u, v, (a, b) -> res[0] += segmentTree.get(a, b).sum);
        return res[0];
    }

    public void modify(int u, int v, long delta) {
        processPath(u, v, (a, b) -> segmentTree.modify(a, b, delta));
    }

    void processPath(int u, int v, BiConsumer<Integer, Integer> op) {
        for (; pathRoot[u] != pathRoot[v]; v = parent[pathRoot[v]]) {
            if (depth[pathRoot[u]] > depth[pathRoot[v]]) {
                int t = u;
                u = v;
                v = t;
            }
            op.accept(pos[pathRoot[v]], pos[v]);
        }
        if (!valuesOnVertices && u == v) return;
        op.accept(Math.min(pos[u], pos[v]) + (valuesOnVertices ? 0 : 1), Math.max(pos[u], pos[v]));
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(5).toArray(List[]::new);
        tree[0].add(1);
        tree[1].add(0);

        tree[0].add(2);
        tree[2].add(0);

        tree[1].add(3);
        tree[3].add(1);

        tree[1].add(4);
        tree[4].add(1);

        HeavyLight hlV = new HeavyLight(tree, true);
        hlV.modify(3, 2, 1);
        hlV.modify(1, 0, -1);
        System.out.println(1 == hlV.get(4, 2));

        HeavyLight hlE = new HeavyLight(tree, false);
        hlE.modify(3, 2, 1);
        hlE.modify(1, 0, -1);
        System.out.println(1 == hlE.get(4, 2));
    }
}
