package structures;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

// Heavy-light decomposition with path queries. Query complexity is O(log^2(n)).
// Based on the code from http://codeforces.com/blog/entry/22072
public class HeavyLight {

    int getNeutralValue() {
        return 0;
    }

    List<Integer>[] tree;
    boolean valuesOnVertices; // true - values on vertices, false - values on edges
    SegmentTree segmentTree;
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

    public int query(int u, int v) {
        AtomicInteger res = new AtomicInteger(getNeutralValue()); // just mutable integer
        processPath(u, v, (a, b) -> res.set(segmentTree.queryOperation(res.get(), segmentTree.query(a, b))));
        return res.get();
    }

    public void modify(int u, int v, int delta) {
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

    static class SegmentTree {
        // Modify the following 5 methods to implement your custom operations on the tree.
        // This example implements Add/Sum operations. Operations like Add/Max, Set/Max can also be implemented.
        int modifyOperation(int x, int y) {
            return x + y;
        }

        // query (or combine) operation
        int queryOperation(int leftValue, int rightValue) {
            return leftValue + rightValue;
        }

        int deltaEffectOnSegment(int delta, int segmentLength) {
            if (delta == getNeutralDelta()) return getNeutralDelta();
            // Here you must write a fast equivalent of following slow code:
            // int result = delta;
            // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
            // return result;
            return delta * segmentLength;
        }

        int getNeutralDelta() {
            return 0;
        }

        int getInitValue() {
            return 0;
        }

        // generic code
        int[] value;
        int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

        int joinValueWithDelta(int value, int delta) {
            if (delta == getNeutralDelta()) return value;
            return modifyOperation(value, delta);
        }

        int joinDeltas(int delta1, int delta2) {
            if (delta1 == getNeutralDelta()) return delta2;
            if (delta2 == getNeutralDelta()) return delta1;
            return modifyOperation(delta1, delta2);
        }

        void pushDelta(int i) {
            int d = 0;
            for (; (i >> d) > 0; d++) {
            }
            for (d -= 2; d >= 0; d--) {
                int x = i >> d;
                value[x >> 1] = joinNodeValueWithDelta(x >> 1, 1 << (d + 1));
                delta[x] = joinDeltas(delta[x], delta[x >> 1]);
                delta[x ^ 1] = joinDeltas(delta[x ^ 1], delta[x >> 1]);
                delta[x >> 1] = getNeutralDelta();
            }
        }

        public SegmentTree(int n) {
            value = new int[2 * n];
            for (int i = 0; i < n; i++) {
                value[i + n] = getInitValue();
            }
            for (int i = 2 * n - 1; i > 1; i -= 2) {
                value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
            }
            delta = new int[2 * n];
            Arrays.fill(delta, getNeutralDelta());
        }

        int joinNodeValueWithDelta(int i, int len) {
            return joinValueWithDelta(value[i], deltaEffectOnSegment(delta[i], len));
        }

        public int query(int from, int to) {
            from += value.length >> 1;
            to += value.length >> 1;
            pushDelta(from);
            pushDelta(to);
            int res = 0;
            boolean found = false;
            for (int len = 1; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1, len <<= 1) {
                if ((from & 1) != 0) {
                    res = found ? queryOperation(res, joinNodeValueWithDelta(from, len)) : joinNodeValueWithDelta(from, len);
                    found = true;
                }
                if ((to & 1) == 0) {
                    res = found ? queryOperation(res, joinNodeValueWithDelta(to, len)) : joinNodeValueWithDelta(to, len);
                    found = true;
                }
            }
            if (!found) throw new RuntimeException();
            return res;
        }

        public void modify(int from, int to, int delta) {
            from += value.length >> 1;
            to += value.length >> 1;
            pushDelta(from);
            pushDelta(to);
            int a = from;
            int b = to;
            for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
                if ((from & 1) != 0) {
                    this.delta[from] = joinDeltas(this.delta[from], delta);
                }
                if ((to & 1) == 0) {
                    this.delta[to] = joinDeltas(this.delta[to], delta);
                }
            }
            for (int i = a, len = 1; i > 1; i >>= 1, len <<= 1) {
                value[i >> 1] = queryOperation(joinNodeValueWithDelta(i, len), joinNodeValueWithDelta(i ^ 1, len));
            }
            for (int i = b, len = 1; i > 1; i >>= 1, len <<= 1) {
                value[i >> 1] = queryOperation(joinNodeValueWithDelta(i, len), joinNodeValueWithDelta(i ^ 1, len));
            }
        }
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            List<Integer>[] tree = getRandomTree(n, rnd);
            HeavyLight hl = new HeavyLight(tree, true);
            int[] x = new int[n];
            Arrays.fill(x, hl.segmentTree.getInitValue());
            for (int i = 0; i < 1000; i++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                List<Integer> path = new ArrayList<>();
                getPathFromAtoB(tree, a, b, -1, path);
                if (rnd.nextBoolean()) {
                    int delta = rnd.nextInt(50) - 100;
                    hl.modify(a, b, delta);
                    for (int u : path)
                        x[u] = hl.segmentTree.joinValueWithDelta(x[u], delta);
                } else {
                    int res1 = hl.query(a, b);
                    int res2 = hl.getNeutralValue();
                    for (int u : path)
                        res2 = hl.segmentTree.queryOperation(res2, x[u]);
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
                for (int v : tree[u])
                    x.put(edge(u, v), hl.segmentTree.getInitValue());
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
                        x.put(key, hl.segmentTree.joinValueWithDelta(x.get(key), delta));
                    }
                } else {
                    int res1 = hl.query(a, b);
                    int res2 = hl.getNeutralValue();
                    for (int j = 0; j + 1 < path.size(); j++) {
                        long key = edge(path.get(j), path.get(j + 1));
                        res2 = hl.segmentTree.queryOperation(res2, x.get(key));
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
}