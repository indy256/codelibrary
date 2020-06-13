#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)

bool augment_path(vector<vector<int>> &cap, vector<bool> &vis, int i, int t) {
    if (i == t)
        return true;
    vis[i] = true;
    for (int j = 0; j < vis.size(); j++)
        if (!vis[j] && cap[i][j] > 0 && augment_path(cap, vis, j, t)) {
            --cap[i][j];
            ++cap[j][i];
            return true;
        }
    return false;
}

int maxFlow(vector<vector<int>> &cap, int s, int t) {
    for (int flow = 0;; ++flow) {
        vector<bool> vis(cap.size());
        if (!augment_path(cap, vis, s, t))
            return flow;
    }
}

// usage example
int main() {
    vector<vector<int>> capacity{{0, 1, 1, 0}, {1, 0, 1, 1}, {1, 1, 0, 1}, {0, 1, 1, 0}};
    cout << (2 == maxFlow(capacity, 0, 3)) << endl;
}
