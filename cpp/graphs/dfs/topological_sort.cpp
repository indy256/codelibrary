#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Topological_sorting

void dfs(const vector<vector<int>> &graph, vector<bool> &used, vector<int> &order, int u) {
    used[u] = true;
    for (int v : graph[u])
        if (!used[v])
            dfs(graph, used, order, v);
    order.push_back(u);
}

vector<int> topological_sort(const vector<vector<int>> &graph) {
    size_t n = graph.size();
    vector<bool> used(n);
    vector<int> order;
    for (int i = 0; i < n; i++)
        if (!used[i])
            dfs(graph, used, order, i);
    reverse(order.begin(), order.end());
    return order;
}

// usage example
int main() {
    vector<vector<int>> g = {{0}, {}, {0, 1}};

    vector<int> order = topological_sort(g);

    for (int v : order)
        cout << v << " ";
    cout << endl;
}
