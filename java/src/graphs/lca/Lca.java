package graphs.lca;

import java.util.*;
import java.util.stream.Stream;

// Answering LCA queries in O(log(n)) with O(n) preprocessing
public class Lca {

    int[] depth;
    int[] dfs_order;
    int cnt;
    int[] first;
    int[] minPos;
    int n;

    void dfs(List<Integer>[] tree, int u, int d) {
        depth[u] = d;
        dfs_order[cnt++] = u;
        for (int v : tree[u])
            if (depth[v] == -1) {
                dfs(tree, v, d + 1);
                dfs_order[cnt++] = u;
            }
    }

    void buildTree(int node, int left, int right) {
        if (left == right) {
            minPos[node] = dfs_order[left];
            return;
        }
        int mid = (left + right) >> 1;
        buildTree(2 * node + 1, left, mid);
        buildTree(2 * node + 2, mid + 1, right);
        minPos[node] = depth[minPos[2 * node + 1]] < depth[minPos[2 * node + 2]] ? minPos[2 * node + 1] : minPos[2 * node + 2];
    }

    public Lca(List<Integer>[] tree, int root) {
        int nodes = tree.length;
        depth = new int[nodes];
        Arrays.fill(depth, -1);

        n = 2 * nodes - 1;
        dfs_order = new int[n];
        cnt = 0;
        dfs(tree, root, 0);

        minPos = new int[4 * n];
        buildTree(0, 0, n - 1);

        first = new int[nodes];
        Arrays.fill(first, -1);
        for (int i = 0; i < dfs_order.length; i++)
            if (first[dfs_order[i]] == -1)
                first[dfs_order[i]] = i;
    }

    public int lca(int a, int b) {
        return minPos(Math.min(first[a], first[b]), Math.max(first[a], first[b]), 0, 0, n - 1);
    }

    int minPos(int a, int b, int node, int left, int right) {
        if (a == left && right == b)
            return minPos[node];
        int mid = (left + right) >> 1;
        if (a <= mid && b > mid) {
            int p1 = minPos(a, Math.min(b, mid), 2 * node + 1, left, mid);
            int p2 = minPos(Math.max(a, mid + 1), b, 2 * node + 2, mid + 1, right);
            return depth[p1] < depth[p2] ? p1 : p2;
        } else if (a <= mid) {
            return minPos(a, Math.min(b, mid), 2 * node + 1, left, mid);
        } else if (b > mid) {
            return minPos(Math.max(a, mid + 1), b, 2 * node + 2, mid + 1, right);
        } else {
            throw new RuntimeException();
        }
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
            Lca q = new Lca(tree, root);
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
