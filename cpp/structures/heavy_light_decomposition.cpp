#include <bits/stdc++.h>

using namespace std;

class SegmentTree {
    vector<int> value;
    vector<int> delta; // // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]
public:
    explicit SegmentTree(int n) : value(2 * n), delta(2 * n, neutral_delta) {
        fill(value.begin(), value.begin() + n, neutral_value);
        for (int i = 2 * n - 1; i > 1; i -= 2) {
            value[i >> 1] = query_operation(value[i], value[i ^ 1]);
        }
    }

    int query(int from, int to) {
        from += value.size() >> 1;
        to += value.size() >> 1;
        push_delta(from);
        push_delta(to);
        int res = neutral_value;
        bool found = false;
        for (int len = 1; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1, len <<= 1) {
            if ((from & 1) != 0) {
                res = found ? query_operation(res, join_node_value_with_delta(from, len)) :
                      join_node_value_with_delta(from, len);
                found = true;
            }
            if ((to & 1) == 0) {
                res = found ? query_operation(res, join_node_value_with_delta(to, len)) :
                      join_node_value_with_delta(to, len);
                found = true;
            }
        }
        return res;
    }

    void modify(int from, int to, int delta_value) {
        from += value.size() >> 1;
        to += value.size() >> 1;
        push_delta(from);
        push_delta(to);
        int a = from;
        int b = to;
        for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
            if ((from & 1) != 0) {
                delta[from] = join_deltas(delta[from], delta_value);
            }
            if ((to & 1) == 0) {
                delta[to] = join_deltas(delta[to], delta_value);
            }
        }
        for (int i = a, len = 1; i > 1; i >>= 1, len <<= 1) {
            value[i >> 1] = query_operation(join_node_value_with_delta(i, len), join_node_value_with_delta(i ^ 1, len));
        }
        for (int i = b, len = 1; i > 1; i >>= 1, len <<= 1) {
            value[i >> 1] = query_operation(join_node_value_with_delta(i, len), join_node_value_with_delta(i ^ 1, len));
        }
    }

    // Modify the following 5 methods to implement your custom operations on the tree.
    // This example implements Add/Sum operations. Operations like Add/Max, Set/Max can also be implemented.
    constexpr static auto update_operation = [](int x, int y) -> int { return x + y; };

    // query (or combine) operation
    constexpr static auto query_operation = [](int leftValue, int rightValue) -> int { return leftValue + rightValue; };

    const int neutral_delta = 0;
    const int neutral_value = 0;

private:
    int delta_effect_on_segment(int delta, int segmentLength) {
        if (delta == neutral_delta) return neutral_delta;
        // Here you must write a fast equivalent of following slow code:
        // int result = delta;
        // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
        // return result;
        return delta * segmentLength;
    }

    int join_value_with_delta(int value, int delta) {
        if (delta == neutral_delta) return value;
        return update_operation(value, delta);
    }

    int join_deltas(int delta1, int delta2) {
        if (delta1 == neutral_delta) return delta2;
        if (delta2 == neutral_delta) return delta1;
        return update_operation(delta1, delta2);
    }

    void push_delta(int i) {
        int d = 0;
        for (; (i >> d) > 0; d++) {
        }
        for (d -= 2; d >= 0; d--) {
            int x = i >> d;
            value[x >> 1] = join_node_value_with_delta(x >> 1, 1 << (d + 1));
            delta[x] = join_deltas(delta[x], delta[x >> 1]);
            delta[x ^ 1] = join_deltas(delta[x ^ 1], delta[x >> 1]);
            delta[x >> 1] = neutral_delta;
        }
    }

    int join_node_value_with_delta(int i, int len) {
        return join_value_with_delta(value[i], delta_effect_on_segment(delta[i], len));
    }
};

class HeavyLight {
    vector<vector<int>> tree;
    bool valuesOnVertices; // true - values on vertices, false - values on edges
    SegmentTree segment_tree;
    vector<int> parent;
    vector<int> heavy;
    vector<int> depth;
    vector<int> pathRoot;
    vector<int> pos;
public:

    int getNeutralValue() {
        return 0;
    }

    explicit HeavyLight(const vector<vector<int>> &tree, bool valuesOnVertices) :
            tree(tree), valuesOnVertices(valuesOnVertices), segment_tree(tree.size()),
            parent(tree.size()), heavy(tree.size(), -1), depth(tree.size()), pathRoot(tree.size()), pos(tree.size()) {
        int n = tree.size();

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

    int query(int u, int v) {
        int res = getNeutralValue();
        auto op = [this, &res](int a, int b) { res = SegmentTree::query_operation(res, segment_tree.query(a, b)); };
        process_path(u, v, op);
        return res;
    }

    void modify(int u, int v, int delta) {
        auto op = [this, delta](int a, int b) { segment_tree.modify(a, b, delta); };
        process_path(u, v, op);
    }

    void process_path(int u, int v, const function<void(int x, int y)> &op) {
        for (; pathRoot[u] != pathRoot[v]; v = parent[pathRoot[v]]) {
            if (depth[pathRoot[u]] > depth[pathRoot[v]]) {
                int t = u;
                u = v;
                v = t;
            }
            op(pos[pathRoot[v]], pos[v]);
        }
        if (!valuesOnVertices && u == v) return;
        op(min(pos[u], pos[v]) + (valuesOnVertices ? 0 : 1), max(pos[u], pos[v]));
    }
};

// usage example()
int main() {
    vector<vector<int>> tree{{1, 2},
                             {0, 3, 4},
                             {0},
                             {1},
                             {1}};

    HeavyLight hl_v(tree, true);
    hl_v.modify(3, 2, 1);
    hl_v.modify(1, 0, -1);
    cout << (1 == hl_v.query(4, 2)) << endl;

    HeavyLight hl_e(tree, false);
    hl_e.modify(3, 2, 1);
    hl_e.modify(1, 0, -1);
    cout << (1 == hl_e.query(4, 2)) << endl;
}
