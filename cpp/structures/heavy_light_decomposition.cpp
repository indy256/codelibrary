#include <bits/stdc++.h>

#include "segment_tree.h"

using namespace std;

class HeavyLight {
   public:
    vector<vector<int>> tree;
    bool valuesOnVertices;  // true - values on vertices, false - values on edges
    segtree segment_tree;
    vector<int> parent;
    vector<int> depth;
    vector<int> pathRoot;
    vector<int> in;

    HeavyLight(const vector<vector<int>> &t, bool valuesOnVertices)
        : tree(t),
          valuesOnVertices(valuesOnVertices),
          segment_tree(t.size()),
          parent(t.size()),
          depth(t.size()),
          pathRoot(t.size()),
          in(t.size()) {
        int time = 0;
        parent[0] = -1;

        function<int(int)> dfs1 = [&](int u) {
            int size = 1;
            int maxSubtree = 0;
            for (int &v : tree[u]) {
                if (v == parent[u])
                    continue;
                parent[v] = u;
                depth[v] = depth[u] + 1;
                int subtree = dfs1(v);
                if (maxSubtree < subtree) {
                    maxSubtree = subtree;
                    swap(v, tree[u][0]);
                }
                size += subtree;
            }
            return size;
        };

        function<void(int)> dfs2 = [&](int u) {
            in[u] = time++;
            for (int v : t[u]) {
                if (v == parent[u])
                    continue;
                pathRoot[v] = v == t[u][0] ? pathRoot[u] : v;
                dfs2(v);
            }
        };

        dfs1(0);
        dfs2(0);
    }

    segtree::node get(int u, int v) {
        segtree::node res;
        process_path(u, v, [this, &res](int a, int b) { res = segtree::unite(res, segment_tree.get(a, b)); });
        return res;
    }

    void modify(int u, int v, long long delta) {
        process_path(u, v, [this, delta](int a, int b) { segment_tree.modify(a, b, delta); });
    }

    void process_path(int u, int v, const function<void(int x, int y)> &op) {
        for (; pathRoot[u] != pathRoot[v]; v = parent[pathRoot[v]]) {
            if (depth[pathRoot[u]] > depth[pathRoot[v]])
                swap(u, v);
            op(in[pathRoot[v]], in[v]);
        }
        if (u != v || valuesOnVertices)
            op(min(in[u], in[v]) + (valuesOnVertices ? 0 : 1), max(in[u], in[v]));
    }
};

// usage example
int main() {
    vector<vector<int>> tree{{1, 2}, {0, 3, 4}, {0}, {1}, {1}};

    HeavyLight hl_v(tree, true);
    hl_v.modify(3, 2, 1);
    hl_v.modify(1, 0, -1);
    cout << hl_v.get(4, 2).sum << endl;

    HeavyLight hl_e(tree, false);
    hl_e.modify(3, 2, 1);
    hl_e.modify(1, 0, -1);
    cout << hl_e.get(4, 2).sum << endl;
}
