package test.lca;

import graphs.lca.LcaSchieberVishkin;
import java.util.*;
import java.util.stream.Stream;

public class LcaSchieberVishkinTest {
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
        List<Integer>[] t = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
        int[] p = new int[n];
        for (int i = 0, j; i < n; j = rnd.nextInt(i + 1), p[i] = p[j], p[j] = i, i++)
            ; // random permutation
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
