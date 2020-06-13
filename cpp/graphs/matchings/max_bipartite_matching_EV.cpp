#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs
// time complexity: O(E * V)

bool findPath(const vector<vector<int>> &graph, int u1, vector<int> &matching, vector<bool> &vis) {
    vis[u1] = true;
    for (int v : graph[u1]) {
        int u2 = matching[v];
        if (u2 == -1 || (!vis[u2] && findPath(graph, u2, matching, vis))) {
            matching[v] = u1;
            return true;
        }
    }
    return false;
}

tuple<int, vector<int>> max_matching(const vector<vector<int>> &graph) {
    int n2 = 0;
    for (auto &u1 : graph)
        for (int u2 : u1)
            n2 = max(n2, u2 + 1);
    int n1 = graph.size();
    vector<int> matching(n2, -1);
    int matches = 0;
    for (int u = 0; u < n1; u++) {
        vector<bool> vis(n1);
        if (findPath(graph, u, matching, vis))
            ++matches;
    }
    return {matches, matching};
}

// usage example
int main() {
    vector<vector<int>> g(2);
    g[0].push_back(0);
    g[0].push_back(1);
    g[1].push_back(1);
    g[2].push_back(1);

    auto [max_matching_cardinality, mapping] = max_matching(g);

    cout << (2 == max_matching_cardinality) << endl;

    for (int x : mapping)
        cout << x << " ";
    cout << endl;
}
