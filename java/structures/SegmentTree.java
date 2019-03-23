package structures;

import static java.lang.Long.max;

public class SegmentTree {

    static class node {
        // initial values for leaves
        long mx = 0;
        long add = 0;

        void apply(int l, int r, long... v) {
            mx += v[0];
            add += v[0];
        }
    }

    node unite(node a, node b) {
        node res = new node();
        res.mx = max(a.mx, b.mx);
        return res;
    }

    void push(int x, int l, int r) {
        int y = (l + r) >> 1;
        int z = x + ((y - l + 1) << 1);
        // push from x into (x + 1) and z
        if (tree[x].add != 0) {
            tree[x + 1].apply(l, y, tree[x].add);
            tree[z].apply(y + 1, r, tree[x].add);
            tree[x].add = 0;
        }
    }

    void pull(int x, int z) {
        tree[x] = unite(tree[x + 1], tree[z]);
    }

    int n;
    node[] tree;

    void build(int x, int l, int r) {
        if (l == r) {
            return;
        }
        int y = (l + r) >> 1;
        int z = x + ((y - l + 1) << 1);
        build(x + 1, l, y);
        build(z, y + 1, r);
        pull(x, z);
    }

    void build(int x, int l, int r, long[] v) {
        if (l == r) {
            tree[x].apply(l, r, v[l]);
            return;
        }
        int y = (l + r) >> 1;
        int z = x + ((y - l + 1) << 1);
        build(x + 1, l, y, v);
        build(z, y + 1, r, v);
        pull(x, z);
    }

    node get(int x, int l, int r, int ll, int rr) {
        if (ll <= l && r <= rr) {
            return tree[x];
        }
        int y = (l + r) >> 1;
        int z = x + ((y - l + 1) << 1);
        push(x, l, r);
        node res;
        if (rr <= y) {
            res = get(x + 1, l, y, ll, rr);
        } else {
            if (ll > y) {
                res = get(z, y + 1, r, ll, rr);
            } else {
                res = unite(get(x + 1, l, y, ll, rr), get(z, y + 1, r, ll, rr));
            }
        }
        pull(x, z);
        return res;
    }

    void modify(int x, int l, int r, int ll, int rr, long... v) {
        if (ll <= l && r <= rr) {
            tree[x].apply(l, r, v);
            return;
        }
        int y = (l + r) >> 1;
        int z = x + ((y - l + 1) << 1);
        push(x, l, r);
        if (ll <= y) {
            modify(x + 1, l, y, ll, rr, v);
        }
        if (rr > y) {
            modify(z, y + 1, r, ll, rr, v);
        }
        pull(x, z);
    }

    SegmentTree(int n) {
        this.n = n;
        tree = new node[2 * n - 1];
        for (int i = 0; i < tree.length; i++) tree[i] = new node();
        build(0, 0, n - 1);
    }

    SegmentTree(long[] v) {
        n = v.length;
        tree = new node[2 * n - 1];
        for (int i = 0; i < tree.length; i++) tree[i] = new node();
        build(0, 0, n - 1, v);
    }

    node get(int ll, int rr) {
        assert (0 <= ll && ll <= rr && rr <= n - 1);
        return get(0, 0, n - 1, ll, rr);
    }

    node get(int p) {
        assert (0 <= p && p <= n - 1);
        return get(0, 0, n - 1, p, p);
    }

    void modify(int ll, int rr, long... v) {
        assert (0 <= ll && ll <= rr && rr <= n - 1);
        modify(0, 0, n - 1, ll, rr, v);
    }

    // Usage example
    public static void main(String[] args) {
        SegmentTree t = new SegmentTree(10);
        t.modify(1, 2, 10);
        t.modify(2, 3, 20);
        System.out.println(t.get(1, 3).mx);
    }
}
