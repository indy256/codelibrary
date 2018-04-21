package combinatorics;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Pr?fer_sequence
public class RandomGraph {

    public static List<Integer>[] getRandomTree2(int n, Random rnd) {
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

    public static List<Integer>[] pruferCode2Tree(int[] pruferCode) {
        int n = pruferCode.length + 2;
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        int[] degree = new int[n];
        Arrays.fill(degree, 1);
        for (int v : pruferCode)
            ++degree[v];
        int ptr = 0;
        while (degree[ptr] != 1)
            ++ptr;
        int leaf = ptr;
        for (int v : pruferCode) {
            tree[leaf].add(v);
            tree[v].add(leaf);
            --degree[leaf];
            --degree[v];
            if (degree[v] == 1 && v < ptr) {
                leaf = v;
            } else {
                for (++ptr; ptr < n && degree[ptr] != 1; ++ptr) ;
                leaf = ptr;
            }
        }
        for (int v = 0; v < n - 1; v++) {
            if (degree[v] == 1) {
                tree[v].add(n - 1);
                tree[n - 1].add(v);
            }
        }
        return tree;
    }

    public static int[] tree2PruferCode(List<Integer>[] tree) {
        int n = tree.length;
        int[] parent = new int[n];
        parent[n - 1] = -1;
        pruferDfs(tree, parent, n - 1);
        int[] degree = new int[n];
        int ptr = -1;
        for (int i = 0; i < n; ++i) {
            degree[i] = tree[i].size();
            if (degree[i] == 1 && ptr == -1)
                ptr = i;
        }
        int[] res = new int[n - 2];
        int leaf = ptr;
        for (int i = 0; i < n - 2; ++i) {
            int next = parent[leaf];
            res[i] = next;
            --degree[next];
            if (degree[next] == 1 && next < ptr) {
                leaf = next;
            } else {
                ++ptr;
                while (ptr < n && degree[ptr] != 1)
                    ++ptr;
                leaf = ptr;
            }
        }
        return res;
    }

    static void pruferDfs(List<Integer>[] tree, int[] parent, int v) {
        for (int i = 0; i < tree[v].size(); ++i) {
            int to = tree[v].get(i);
            if (to != parent[v]) {
                parent[to] = v;
                pruferDfs(tree, parent, to);
            }
        }
    }

    // precondition: n >= 2
    public static List<Integer>[] getRandomTree(int V, Random rnd) {
        int[] a = new int[V - 2];
        for (int i = 0; i < a.length; i++) {
            a[i] = rnd.nextInt(V);
        }
        return pruferCode2Tree(a);
    }

    // precondition: V >= 2, V-1 <= E <= V*(V-1)/2
    public static List<Integer>[] getRandomUndirectedConnectedGraph(int V, int E, Random rnd) {
        List<Integer>[] g = getRandomTree(V, rnd);
        Set<Long> edgeSet = new LinkedHashSet<>();
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                edgeSet.add(((long) i << 32) + j);
            }
        }
        for (int i = 0; i < V; i++) {
            for (int j : g[i]) {
                edgeSet.remove(((long) i << 32) + j);
            }
        }
        List<Long> edges = new ArrayList<>(edgeSet);
        for (int x : getRandomArrangement(edges.size(), E - (V - 1), rnd)) {
            long e = edges.get(x);
            int u = (int) (e >>> 32);
            int v = (int) e;
            g[u].add(v);
            g[v].add(u);
        }
        for (int i = 0; i < V; i++)
            Collections.sort(g[i]);
        return g;
    }

    // precondition: V >= 2, V-1 <= E <= V*(V-1)/2
    public static List<Integer>[] getRandomUndirectedConnectedGraph2(int V, int E, Random rnd) {
        List<Integer>[] g = getRandomTree(V, rnd);
        Set<Long> edgeSet = new LinkedHashSet<>();
        for (int i = 0; i < V; i++) {
            for (int j : g[i]) {
                edgeSet.add(((long) i << 32) + j);
            }
        }
        for (int i = 0; i < E - (V - 1); i++) {
            int u;
            int v;
            long edge;
            while (true) {
                u = rnd.nextInt(V);
                v = rnd.nextInt(V);
                edge = ((long) u << 32) + v;
                if (u < v && !edgeSet.contains(edge)) break;
            }
            edgeSet.add(edge);
            g[u].add(v);
            g[v].add(u);
        }
        for (int i = 0; i < V; i++)
            Collections.sort(g[i]);
        return g;
    }

    static int[] getRandomArrangement(int n, int m, Random rnd) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = i;
        }
        for (int i = 0; i < m; i++) {
            int j = n - 1 - rnd.nextInt(n - i);
            int t = res[i];
            res[i] = res[j];
            res[j] = t;
        }
        return Arrays.copyOf(res, m);
    }

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] tree = pruferCode2Tree(new int[]{3, 3, 3, 4});
        System.out.println(Arrays.toString(tree));
        System.out.println(Arrays.toString(tree2PruferCode(tree)));
        System.out.println(Arrays.toString(pruferCode2Tree(new int[]{0, 0})));

        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int V = rnd.nextInt(50) + 2;
            checkGraph(V, V - 1, rnd);
            checkGraph(V, V * (V - 1) / 2, rnd);
            checkGraph(V, rnd.nextInt(V * (V - 1) / 2 - (V - 1) + 1) + V - 1, rnd);
        }
    }

    static void checkGraph(int V, int E, Random rnd) {
        List<Integer>[] g = getRandomUndirectedConnectedGraph(V, E, rnd);
        int n = g.length;
        int[][] a = new int[n][n];
        int edges = 0;
        for (int i = 0; i < n; i++) {
            for (int j : g[i]) {
                ++a[i][j];
                ++edges;
            }
        }
        if (edges != 2 * E) {
            throw new RuntimeException();
        }
        for (int i = 0; i < n; i++) {
            if (a[i][i] != 0) {
                throw new RuntimeException();
            }
            for (int j = 0; j < n; j++) {
                if (a[i][j] != a[j][i] || a[i][j] != 0 && a[i][j] != 1) {
                    throw new RuntimeException();
                }
            }
        }
    }
}
