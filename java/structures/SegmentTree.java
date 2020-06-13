package structures;

import java.util.function.Predicate;

public class SegmentTree {
    int n;
    Node[] tree;

    public static class Node {
        // initial values for leaves
        public long mx = 0;
        public long sum = 0;
        public long add = 0;

        void apply(int l, int r, long v) {
            mx += v;
            sum += v * (r - l + 1);
            add += v;
        }
    }

    static Node unite(Node a, Node b) {
        Node res = new Node();
        res.mx = Math.max(a.mx, b.mx);
        res.sum = a.sum + b.sum;
        return res;
    }

    void push(int x, int l, int r) {
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        if (tree[x].add != 0) {
            tree[x + 1].apply(l, m, tree[x].add);
            tree[y].apply(m + 1, r, tree[x].add);
            tree[x].add = 0;
        }
    }

    void pull(int x, int y) {
        tree[x] = unite(tree[x + 1], tree[y]);
    }

    SegmentTree(int n) {
        this.n = n;
        tree = new Node[2 * n - 1];
        for (int i = 0; i < tree.length; i++) tree[i] = new Node();
        build(0, 0, n - 1);
    }

    SegmentTree(long[] v) {
        n = v.length;
        tree = new Node[2 * n - 1];
        for (int i = 0; i < tree.length; i++) tree[i] = new Node();
        build(0, 0, n - 1, v);
    }

    void build(int x, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        build(x + 1, l, m);
        build(y, m + 1, r);
        pull(x, y);
    }

    void build(int x, int l, int r, long[] v) {
        if (l == r) {
            tree[x].apply(l, r, v[l]);
            return;
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        build(x + 1, l, m, v);
        build(y, m + 1, r, v);
        pull(x, y);
    }

    public Node get(int ll, int rr) {
        return get(ll, rr, 0, 0, n - 1);
    }

    Node get(int ll, int rr, int x, int l, int r) {
        if (ll <= l && r <= rr) {
            return tree[x];
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        push(x, l, r);
        Node res;
        if (rr <= m) {
            res = get(ll, rr, x + 1, l, m);
        } else {
            if (ll > m) {
                res = get(ll, rr, y, m + 1, r);
            } else {
                res = unite(get(ll, rr, x + 1, l, m), get(ll, rr, y, m + 1, r));
            }
        }
        pull(x, y);
        return res;
    }

    void modify(int ll, int rr, long v) {
        modify(ll, rr, v, 0, 0, n - 1);
    }

    void modify(int ll, int rr, long v, int x, int l, int r) {
        if (ll <= l && r <= rr) {
            tree[x].apply(l, r, v);
            return;
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        push(x, l, r);
        if (ll <= m) {
            modify(ll, rr, v, x + 1, l, m);
        }
        if (rr > m) {
            modify(ll, rr, v, y, m + 1, r);
        }
        pull(x, y);
    }

    // calls all FALSE elements to the left of the sought position exactly once
    int findFirst(int ll, int rr, Predicate<Node> f) {
        return findFirst(ll, rr, f, 0, 0, n - 1);
    }

    int findFirst(int ll, int rr, Predicate<Node> f, int x, int l, int r) {
        if (ll <= l && r <= rr && !f.test(tree[x])) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        push(x, l, r);
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        int res = -1;
        if (ll <= m) {
            res = findFirst(ll, rr, f, x + 1, l, m);
        }
        if (rr > m && res == -1) {
            res = findFirst(ll, rr, f, y, m + 1, r);
        }
        pull(x, y);
        return res;
    }

    // Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
    static int sumLowerBound(SegmentTree t, int ll, int rr, long sum) {
        long[] sumSoFar = new long[1];
        return t.findFirst(ll, rr, node -> {
            if (sumSoFar[0] + node.sum >= sum)
                return true;
            sumSoFar[0] += node.sum;
            return false;
        });
    }

    // Usage example
    public static void main(String[] args) {
        SegmentTree t = new SegmentTree(10);
        t.modify(1, 2, 10);
        t.modify(2, 3, 20);
        System.out.println(30 == t.get(1, 3).mx);
        System.out.println(60 == t.get(1, 3).sum);

        SegmentTree tt = new SegmentTree(new long[] {2, 1, 10, 20});
        System.out.println(2 == sumLowerBound(tt, 0, tt.n - 1, 12));
    }
}
