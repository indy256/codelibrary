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
            if (v == p) continue;
            dfs1(tree, v, u);
            if (Integer.lowestOneBit(I[v]) > Integer.lowestOneBit(I[u])) {
                I[u] = I[v];
            }
        }
        head[I[u]] = u;
    }

    void dfs2(List<Integer>[] tree, int u, int p, int up) {
        A[u] = up | Integer.lowestOneBit(I[u]);
        for (int v : tree[u]) {
            if (v == p) continue;
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
        return parent[head[I[x] & (~hw + 1) | hw]];
    }

    public int lca(int x, int y) {
        int hb = I[x] == I[y] ? Integer.lowestOneBit(I[x]) : Integer.highestOneBit(I[x] ^ I[y]);
        int hz = Integer.lowestOneBit(A[x] & A[y] & (~hb + 1));
        int ex = enterIntoStrip(x, hz);
        int ey = enterIntoStrip(y, hz);
        return preOrder[ex] < preOrder[ey] ? ex : ey;
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            List<Integer>[] tree = getRandomTree(n, rnd);
            int[] depth = new int[n];
            Arrays.fill(depth, -1);
            int root = rnd.nextInt(n);
            calcDepth(tree, depth, root, 0);
            LcaSchieberVishkin q = new LcaSchieberVishkin(tree, root);
            for (int i = 0; i < 1000; i++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                List<Integer> path = new ArrayList<>();
                getPathFromAtoB(tree, a, b, -1, path);
                int res1 = q.lca(a, b);
                int res2 = a;
                for (int u : path)
                    if (depth[res2] > depth[u])
                        res2 = u;
                if (res1 != res2)
                    throw new RuntimeException();
            }
        }
        System.out.println("Test passed");
    }

    static List<Integer>[] getRandomTree(int n, Random rnd) {
        List<Integer>[] t = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        int[] p = new int[n];
        for (int i = 0, j; i < n; j = rnd.nextInt(i + 1), p[i] = p[j], p[j] = i, i++) ; // random permutation
        for (int i = 1; i < n; i++) {
            int parent = p[rnd.nextInt(i)];
            t[parent].add(p[i]);
            t[p[i]].add(parent);
        }
        return t;
    }

    static void calcDepth(List<Integer>[] tree, int[] depth, int u, int d) {
        depth[u] = d;
        for (int v : tree[u])
            if (depth[v] == -1)
                calcDepth(tree, depth, v, d + 1);
    }

    static boolean getPathFromAtoB(List<Integer>[] tree, int a, int b, int p, List<Integer> path) {
        path.add(a);
        if (a == b)
            return true;
        for (int u : tree[a])
            if (u != p && getPathFromAtoB(tree, u, b, a, path))
                return true;
        path.remove(path.size() - 1);
        return false;
    }
}
