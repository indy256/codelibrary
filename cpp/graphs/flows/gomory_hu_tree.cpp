#include <bits/stdc++.h>

#include "max_flow_dinic.h"

using namespace std;

vector<vector<pair<int, int>>> gomory_hu_tree(max_flow_dinic &flow) {
    int n = flow.graph.size();
    vector<vector<pair<int, int>>> t(n, vector<pair<int, int>>());
    vector<int> p(n);
    for (int i = 1; i < n; i++) {
        flow.clear_flow();
        int f = flow.max_flow(i, p[i]);
        vector<bool> cut = flow.min_cut();
        for (int j = i + 1; j < n; j++)
            if (cut[j] == cut[i] && p[j] == p[i])
                p[j] = i;
        t[p[i]].emplace_back(i, f);
    }
    return t;
}

// usage example
int main() {
    int capacity[][3] = {{0, 3, 2}, {0, 0, 2}, {0, 0, 0}};
    int n = 3;
    max_flow_dinic flow(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                flow.add_bidi_edge(i, j, capacity[i][j]);

    vector<vector<pair<int, int>>> t = gomory_hu_tree(flow);
    for (int u = 0; u < n; ++u)
        for (auto [v, f] : t[u])
            cout << u << " " << v << " " << f << endl;
}
