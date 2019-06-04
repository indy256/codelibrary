#include <bits/stdc++.h>

using namespace std;

class segtree {
public:
    struct node {
        // initial values for leaves
        long long mx = 0;
        long long sum = 0;
        long long add = 0;

        void apply(int l, int r, long long v) {
            mx += v;
            sum += (r - l + 1) * v;
            add += v;
        }
    };

    node unite(const node &a, const node &b) const {
        node res;
        res.mx = max(a.mx, b.mx);
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

    int n;
    vector<node> tree;

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

    node get(int x, int l, int r, int ll, int rr) {
        if (ll <= l && r <= rr) {
            return tree[x];
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        push(x, l, r);
        node res;
        if (rr <= m) {
            res = get(x + 1, l, m, ll, rr);
        } else {
            if (ll > m) {
                res = get(y, m + 1, r, ll, rr);
            } else {
                res = unite(get(x + 1, l, m, ll, rr), get(y, m + 1, r, ll, rr));
            }
        }
        pull(x, y);
        return res;
    }

    template<class T>
    void modify(int x, int l, int r, int ll, int rr, const T &v) {
        if (ll <= l && r <= rr) {
            tree[x].apply(l, r, v);
            return;
        }
        int m = (l + r) >> 1;
        int y = x + ((m - l + 1) << 1);
        push(x, l, r);
        if (ll <= m) {
            modify(x + 1, l, m, ll, rr, v);
        }
        if (rr > m) {
            modify(y, m + 1, r, ll, rr, v);
        }
        pull(x, y);
    }

    segtree(int _n) : n(_n) {
        assert(n > 0);
        tree.resize(2 * n - 1);
        build(0, 0, n - 1);
    }

    node get(int ll, int rr) {
        assert(0 <= ll && ll <= rr && rr <= n - 1);
        return get(0, 0, n - 1, ll, rr);
    }

    template<class T>
    void modify(int ll, int rr, const T v) {
        assert(0 <= ll && ll <= rr && rr <= n - 1);
        modify(0, 0, n - 1, ll, rr, v);
    }
};

class HeavyLight {
public:
    vector<vector<int>> tree;
    bool valuesOnVertices; // true - values on vertices, false - values on edges
    segtree segment_tree;
    vector<int> parent;
    vector<int> heavy;
    vector<int> depth;
    vector<int> pathRoot;
    vector<int> pos;

    HeavyLight(const vector<vector<int>> &tree, bool valuesOnVertices) :
            tree(tree), valuesOnVertices(valuesOnVertices), segment_tree(tree.size()),
            parent(tree.size()), heavy(tree.size(), -1), depth(tree.size()), pathRoot(tree.size()), pos(tree.size()) {
        int n = tree.size();

        parent[0] = -1;
        depth[0] = 0;

        function<int(int)> dfs = [&](int u) {
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
        };

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

    long long get(int u, int v) {
        long long res = 0;
        process_path(u, v, [this, &res](int a, int b) { res += segment_tree.get(a, b).sum; });
        return res;
    }

    void modify(int u, int v, int delta) {
        process_path(u, v, [this, delta](int a, int b) { segment_tree.modify(a, b, delta); });
    }

    void process_path(int u, int v, const function<void(int x, int y)> &op) {
        for (; pathRoot[u] != pathRoot[v]; v = parent[pathRoot[v]]) {
            if (depth[pathRoot[u]] > depth[pathRoot[v]])
                swap(u, v);
            op(pos[pathRoot[v]], pos[v]);
        }
        if (u != v || valuesOnVertices)
            op(min(pos[u], pos[v]) + (valuesOnVertices ? 0 : 1), max(pos[u], pos[v]));
    }
};

// usage example
int main() {
    vector<vector<int>> tree{{1, 2},
                             {0, 3, 4},
                             {0},
                             {1},
                             {1}};

    HeavyLight hl_v(tree, true);
    hl_v.modify(3, 2, 1);
    hl_v.modify(1, 0, -1);
    cout << hl_v.get(4, 2) << endl;

    HeavyLight hl_e(tree, false);
    hl_e.modify(3, 2, 1);
    hl_e.modify(1, 0, -1);
    cout << hl_e.get(4, 2) << endl;
}
