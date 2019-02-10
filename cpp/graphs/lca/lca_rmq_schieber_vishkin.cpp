#include <bits/stdc++.h>

using namespace std;

const int MAX_NODES = 200000;
int parent[MAX_NODES];
unsigned pre_order[MAX_NODES];
unsigned I[MAX_NODES];
int head[MAX_NODES];
unsigned A[MAX_NODES];
unsigned Time;

unsigned lowest_one_bit(unsigned x) {
    return x & -x;
}

unsigned highest_one_bit(unsigned x) {
    return x ? 1u << (31 - __builtin_clz(x)) : 0;
}

void dfs1(const vector<vector<int>> &tree, int u, int p) {
    parent[u] = p;
    I[u] = pre_order[u] = Time++;
    for (int v : tree[u]) {
        if (v == p) continue;
        dfs1(tree, v, u);
        if (lowest_one_bit(I[v]) > lowest_one_bit(I[u])) {
            I[u] = I[v];
        }
    }
    head[I[u]] = u;
}

void dfs2(const vector<vector<int>> &tree, int u, int p, unsigned up) {
    A[u] = up | lowest_one_bit(I[u]);
    for (int v : tree[u]) {
        if (v == p) continue;
        dfs2(tree, v, u, A[u]);
    }
}

void init_lca(const vector<vector<int>> &tree, int root) {
    Time = 0;
    dfs1(tree, root, -1);
    dfs2(tree, root, -1, 0);
}

int enter_into_strip(int x, int hz) {
    if (lowest_one_bit(I[x]) == hz)
        return x;
    int hw = highest_one_bit(A[x] & (hz - 1));
    return parent[head[(I[x] & (~hw + 1)) | hw]];
}

int lca(int x, int y) {
    int hb = I[x] == I[y] ? lowest_one_bit(I[x]) : highest_one_bit(I[x] ^ I[y]);
    int hz = lowest_one_bit(A[x] & A[y] & (~hb + 1));
    int ex = enter_into_strip(x, hz);
    int ey = enter_into_strip(y, hz);
    return pre_order[ex] < pre_order[ey] ? ex : ey;
}

void init_rmq(const vector<int> &values) {
    int n = values.size();
    vector<int> st;
    vector<vector<int>> tree(n);
    int root = 0;
    // build Cartesian Tree
    for (int i = 0; i < n; ++i) {
        int last = -1;
        while (!st.empty() && values[i] < values[st.back()]) {
            last = st.back();
            st.pop_back();
        }
        if (!st.empty()) {
            if (!tree[st.back()].empty() && last != -1) {
                assert(tree[st.back()].back() == last);
                tree[st.back()].pop_back();
            }
            tree[st.back()].push_back(i);
        } else {
            root = i;
        }
        if (last != -1) {
            tree[i].push_back(last);
        }
        st.push_back(i);
    }
    init_lca(tree, root);
}

// usage
int main() {
    vector<vector<int>> t{{1, 2},
                          {0},
                          {0}};
    init_lca(t, 0);
    cout << lca(1, 2) << endl;

    init_rmq({5, 1, 4, 3, 2});
    cout << lca(1, 3) << endl;
    cout << lca(2, 4) << endl;
}
