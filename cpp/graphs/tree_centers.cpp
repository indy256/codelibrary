#include <bits/stdc++.h>

using namespace std;

// returns vertex that has all its subtrees sizes <= n/2
int find_tree_centroid(const vector<vector<int>> &tree, int u, int p) {
    int n = tree.size();
    int cnt = 1;
    bool goodCenter = true;
    for (int v : tree[u]) {
        if (v == p)
            continue;
        int res = find_tree_centroid(tree, v, u);
        if (res >= 0)
            return res;
        int size = -res;
        goodCenter &= size <= n / 2;
        cnt += size;
    }
    goodCenter &= n - cnt <= n / 2;
    return goodCenter ? u : -cnt;
}

// usage example
int main() {}