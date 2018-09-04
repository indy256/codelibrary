package structures;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

// Heavy-light decomposition with path queries. Query complexity is O(log^2(n)).
// Based on the code from http://codeforces.com/blog/entry/22072
public class HeavyLight {

    public int getNeutralValue() {
        return 0;
    }

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

    public static class SegmentTree {
        // Modify the following 5 methods to implement your custom operations on the tree.
        // This example implements Add/Sum operations. Operations like Add/Max, Set/Max can also be implemented.
        int modifyOperation(int x, int y) {
            return x + y;
        }

        // query (or combine) operation
        public int queryOperation(int leftValue, int rightValue) {
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

        public int getInitValue() {
            return 0;
        }

        // generic code
        int[] value;
        int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

        public int joinValueWithDelta(int value, int delta) {
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

    // Usage example
    public static void main(String[] args) {
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(5).toArray(List[]::new);
        tree[0].add(1);
        tree[0].add(2);
        tree[1].add(3);
        tree[1].add(4);

        HeavyLight hlV = new HeavyLight(tree, true);
        hlV.modify(3, 2, 1);
        hlV.modify(1, 0, -1);
        System.out.println(1 == hlV.query(4, 2));

        HeavyLight hlE = new HeavyLight(tree, false);
        hlE.modify(3, 2, 1);
        hlE.modify(1, 0, -1);
        System.out.println(1 == hlE.query(4, 2));
    }
}
