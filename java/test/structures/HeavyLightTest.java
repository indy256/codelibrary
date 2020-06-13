package test.structures;

import java.util.*;
import java.util.stream.Stream;
import structures.HeavyLight;

public class HeavyLightTest {
    // Random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            List<Integer>[] tree = getRandomTree(n, rnd);
            HeavyLight hl = new HeavyLight(tree, true);
            long[] x = new long[n];
            Arrays.fill(x, 0);
            for (int i = 0; i < 1000; i++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                List<Integer> path = new ArrayList<>();
                getPathFromAtoB(tree, a, b, -1, path);
                if (rnd.nextBoolean()) {
                    long delta = rnd.nextInt(50) - 100;
                    hl.modify(a, b, delta);
                    for (int u : path) x[u] = x[u] + delta;
                } else {
                    long res1 = hl.get(a, b).sum;
                    long res2 = 0;
                    for (int u : path) res2 = res2 + x[u];
                    if (res1 != res2)
                        throw new RuntimeException();
                }
            }
        }

        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            List<Integer>[] tree = getRandomTree(n, rnd);
            HeavyLight hl = new HeavyLight(tree, false);
            Map<Long, Integer> x = new HashMap<>();
            for (int u = 0; u < tree.length; u++)
                for (int v : tree[u]) x.put(edge(u, v), 0);
            for (int i = 0; i < 1000; i++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                List<Integer> path = new ArrayList<>();
                getPathFromAtoB(tree, a, b, -1, path);
                if (rnd.nextBoolean()) {
                    int delta = rnd.nextInt(50) - 100;
                    hl.modify(a, b, delta);
                    for (int j = 0; j + 1 < path.size(); j++) {
                        long key = edge(path.get(j), path.get(j + 1));
                        x.put(key, x.get(key) + delta);
                    }
                } else {
                    long res1 = hl.get(a, b).sum;
                    long res2 = 0;
                    for (int j = 0; j + 1 < path.size(); j++) {
                        long key = edge(path.get(j), path.get(j + 1));
                        res2 = res2 + x.get(key);
                    }
                    if (res1 != res2)
                        throw new RuntimeException();
                }
            }
        }
        System.out.println("Test passed");
    }

    static long edge(int u, int v) {
        return ((long) Math.min(u, v) << 16) + Math.max(u, v);
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
}
