#include <bits/stdc++.h>

using namespace std;
#ifdef _MSC_VER
int __builtin_clz(unsigned x) {
    int bit = 31;
    while (bit >= 0 && (x & (1 << bit)) == 0)
        --bit;
    return 31 - bit;
}
#endif

const int MAX_NODES = 200'000;
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
        if (v == p)
            continue;
        dfs1(tree, v, u);
        if (lowest_one_bit(I[u]) < lowest_one_bit(I[v])) {
            I[u] = I[v];
        }
    }
    head[I[u]] = u;
}

void dfs2(const vector<vector<int>> &tree, int u, int p, unsigned up) {
    A[u] = up | lowest_one_bit(I[u]);
    for (int v : tree[u]) {
        if (v == p)
            continue;
        dfs2(tree, v, u, A[u]);
    }
}

// initialization in O(n)
void init_lca(const vector<vector<int>> &tree, int root) {
    Time = 0;
    dfs1(tree, root, -1);
    dfs2(tree, root, -1, 0);
}

int enter_into_strip(int x, int hz) {
    if (lowest_one_bit(I[x]) == hz)
        return x;
    int hw = highest_one_bit(A[x] & (hz - 1));
    return parent[head[(I[x] & -hw) | hw]];
}

// lca in O(1)
int lca(int x, int y) {
    int hb = I[x] == I[y] ? lowest_one_bit(I[x]) : highest_one_bit(I[x] ^ I[y]);
    int hz = lowest_one_bit(A[x] & A[y] & -hb);
    int ex = enter_into_strip(x, hz);
    int ey = enter_into_strip(y, hz);
    return pre_order[ex] < pre_order[ey] ? ex : ey;
}

void init_rmq(const vector<int> &values) {
    // build Cartesian Tree
    int n = values.size();
    int root = 0;
    vector<int> p(n, -1);
    for (int i = 1; i < n; i++) {
        int prev = i - 1;
        int next = -1;
        while (values[prev] > values[i] && p[prev] != -1) {
            next = prev;
            prev = p[prev];
        }
        if (values[prev] > values[i]) {
            p[prev] = i;
            root = i;
        } else {
            p[i] = prev;
            if (next != -1) {
                p[next] = i;
            }
        }
    }
    vector<vector<int>> tree(n);
    for (int i = 0; i < n; ++i)
        if (p[i] != -1)
            tree[p[i]].push_back(i);
    init_lca(tree, root);
}

// random test
int main() {
    mt19937 rng(1);
    for (int step = 0; step < 1000; ++step) {
        int n = uniform_int_distribution<int>(1, 10)(rng);
        vector<int> v(n);
        for (int i = 0; i < n; ++i) {
            v[i] = uniform_int_distribution<int>(0, 5)(rng);
        }
        int a = uniform_int_distribution<int>(0, n - 1)(rng);
        int b = uniform_int_distribution<int>(0, n - 1)(rng);
        if (a > b)
            swap(a, b);
        init_rmq(v);
        int res1 = v[lca(a, b)];
        int res2 = *min_element(v.begin() + a, v.begin() + b + 1);
        if (res1 != res2) {
            for (int i = 0; i < n; ++i)
                cout << v[i] << " ";
            cout << endl;
            cout << a << " " << b << " - " << res1 << " " << res2 << endl;
            assert(res1 != res2);
        }
    }
}
