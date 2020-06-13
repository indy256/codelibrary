package graphs.lca;

import java.util.*;
import java.util.stream.Stream;

// Answering LCA queries in O(1) with O(n) preprocessing
public class LcaSchieberVishkin {
    int[] parent;
    int[] preOrder;
    int[] I;
    int[] head;
    int[] A;
    int time;

    void dfs1(List<Integer>[] tree, int u, int p) {
        parent[u] = p;
        I[u] = preOrder[u] = time++;
        for (int v : tree[u]) {
            if (v == p)
                continue;
            dfs1(tree, v, u);
            if (Integer.lowestOneBit(I[u]) < Integer.lowestOneBit(I[v])) {
                I[u] = I[v];
            }
        }
        head[I[u]] = u;
    }

    void dfs2(List<Integer>[] tree, int u, int p, int up) {
        A[u] = up | Integer.lowestOneBit(I[u]);
        for (int v : tree[u]) {
            if (v == p)
                continue;
            dfs2(tree, v, u, A[u]);
        }
    }

    public LcaSchieberVishkin(List<Integer>[] tree, int root) {
        int n = tree.length;
        preOrder = new int[n];
        I = new int[n];
        head = new int[n];
        A = new int[n];
        parent = new int[n];

        dfs1(tree, root, -1);
        dfs2(tree, root, -1, 0);
    }

    int enterIntoStrip(int x, int hz) {
        if (Integer.lowestOneBit(I[x]) == hz)
            return x;
        int hw = Integer.highestOneBit(A[x] & (hz - 1));
        return parent[head[I[x] & -hw | hw]];
    }

    public int lca(int x, int y) {
        int hb = I[x] == I[y] ? Integer.lowestOneBit(I[x]) : Integer.highestOneBit(I[x] ^ I[y]);
        int hz = Integer.lowestOneBit(A[x] & A[y] & -hb);
        int ex = enterIntoStrip(x, hz);
        int ey = enterIntoStrip(y, hz);
        return preOrder[ex] < preOrder[ey] ? ex : ey;
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(5).toArray(List[] ::new);
        tree[0].add(1);
        tree[0].add(2);
        tree[1].add(3);
        tree[1].add(4);
        LcaSchieberVishkin lca = new LcaSchieberVishkin(tree, 0);
        System.out.println(0 == lca.lca(1, 2));
        System.out.println(1 == lca.lca(3, 4));
        System.out.println(0 == lca.lca(4, 2));
    }
}
