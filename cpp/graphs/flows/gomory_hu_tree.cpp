#include <bits/stdc++.h>
#include "max_flow_dinic.h"

using namespace std;

vector<vector<pair<int, int>>> gomory_hu_tree() {
    int n = nodes;
    vector<vector<pair<int, int>>> t(n, vector<pair<int, int>>());
    vector<int> p(n);
    for (int i = 1; i < n; i++) {
        clear_flow();
        int flow = max_flow(i, p[i]);
        vector<bool> cut = min_cut();
        for (int j = i + 1; j < n; j++)
            if (cut[j] == cut[i] && p[j] == p[i])
                p[j] = i;
        t[p[i]].emplace_back(i, flow);
    }
    return t;
}

// usage example
int main() {
    int capacity[][3] = {{0, 3, 2},
                         {0, 0, 2},
                         {0, 0, 0}};
    int n = 3;
    nodes = n;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                add_edge(i, j, capacity[i][j]);

    vector<vector<pair<int, int>>> t = gomory_hu_tree();
    for (int u = 0; u < n; ++u)
        for (auto[v, flow]:t[u])
            cout << u << " " << v << " " << flow << endl;
}
