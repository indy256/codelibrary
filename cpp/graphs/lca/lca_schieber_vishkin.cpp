#include <bits/stdc++.h>

using namespace std;

const int MAX_NODES = 200000;
int parent[MAX_NODES];
unsigned preOrder[MAX_NODES];
unsigned I[MAX_NODES];
int head[MAX_NODES];
unsigned A[MAX_NODES];
unsigned Time;

int lowest_one_bit(int x) {
    return x & -x;
}

unsigned highest_one_bit(unsigned i) {
    i |= (i >> 1);
    i |= (i >> 2);
    i |= (i >> 4);
    i |= (i >> 8);
    i |= (i >> 16);
    return i - (i >> 1);
}

void dfs1(const vector<vector<int>> &tree, int u, int p) {
    parent[u] = p;
    I[u] = preOrder[u] = Time++;
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

void init(const vector<vector<int>> &tree, int root) {
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
    return preOrder[ex] < preOrder[ey] ? ex : ey;
}

// usage
int main() {
    vector<vector<int>> t{{1, 2},
                          {0},
                          {0}};
    init(t, 0);
    cout << lca(1, 2) << endl;
}
