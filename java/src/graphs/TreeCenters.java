package graphs;

import java.util.*;
import java.util.stream.Stream;

public class TreeCenters {

    // returns 1 or 2 tree centers
    // http://en.wikipedia.org/wiki/Graph_center
    public static List<Integer> findTreeCenters(List<Integer>[] tree) {
        int n = tree.length;
        List<Integer> leaves = new ArrayList<>();
        int[] degree = new int[n];
        for (int i = 0; i < n; i++) {
            degree[i] = tree[i].size();
            if (degree[i] <= 1) {
                leaves.add(i);
            }
        }
        int removedLeaves = leaves.size();
        while (removedLeaves < n) {
            List<Integer> nleaves = new ArrayList<>();
            for (int u : leaves) {
                for (int v : tree[u]) {
                    if (--degree[v] == 1) {
                        nleaves.add(v);
                    }
                }
            }
            leaves = nleaves;
            removedLeaves += leaves.size();
        }
        return leaves;
    }

    // returns vertex that has all its subtrees sizes <= n/2
    public static int findTreeCentroid(List<Integer>[] tree, int u, int p) {
        int n = tree.length;
        int cnt = 1;
        boolean goodCenter = true;
        for (int v : tree[u]) {
            if (v == p) continue;
            int res = findTreeCentroid(tree, v, u);
            if (res >= 0)
                return res;
            int size = -res;
            goodCenter &= size <= n / 2;
            cnt += size;
        }
        goodCenter &= n - cnt <= n / 2;
        return goodCenter ? u : -cnt;
    }

    public static int diameter(List<Integer>[] tree) {
        int furthestVertex = (int) dfs(tree, 0, -1, 0);
        return (int) (dfs(tree, furthestVertex, -1, 0) >>> 32);
    }

    static long dfs(List<Integer>[] tree, int u, int p, int depth) {
        long res = ((long) depth << 32) + u;
        for (int v : tree[u])
            if (v != p)
                res = Math.max(res, dfs(tree, v, u, depth + 1));
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 4;
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
        tree[3].add(0);
        tree[0].add(3);
        tree[3].add(1);
        tree[1].add(3);
        tree[3].add(2);
        tree[2].add(3);
        System.out.println(3 == findTreeCentroid(tree, 0, -1));
        System.out.println(3 == findTreeCenters(tree).get(0));
        System.out.println(2 == diameter(tree));
    }
}
