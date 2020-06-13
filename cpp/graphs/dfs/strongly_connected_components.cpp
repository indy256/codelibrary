#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm

void dfs(const vector<vector<int>> &graph, vector<bool> &used, vector<int> &res, int u) {
    used[u] = true;
    for (int v : graph[u])
        if (!used[v])
            dfs(graph, used, res, v);
    res.push_back(u);
}

// Get strongly connected components
vector<vector<int>> scc(const vector<vector<int>> &graph) {
    int n = graph.size();
    vector<bool> used(n);
    vector<int> order;
    for (int i = 0; i < n; i++)
        if (!used[i])
            dfs(graph, used, order, i);

    vector<vector<int>> reverseGraph(n);
    for (int i = 0; i < n; i++)
        for (int j : graph[i])
            reverseGraph[j].push_back(i);

    vector<vector<int>> components;
    fill(used.begin(), used.end(), false);
    reverse(order.begin(), order.end());

    for (int u : order)
        if (!used[u]) {
            vector<int> component;
            dfs(reverseGraph, used, component, u);
            components.push_back(component);
        }

    return components;
}

// DAG of strongly connected components
vector<vector<int>> scc_graph(const vector<vector<int>> &graph, const vector<vector<int>> &components) {
    vector<int> comp(graph.size());
    for (int i = 0; i < components.size(); i++)
        for (int u : components[i])
            comp[u] = i;
    vector<vector<int>> g(components.size());
    unordered_set<long long> edges;
    for (int u = 0; u < graph.size(); u++)
        for (int v : graph[u])
            if (comp[u] != comp[v] && edges.insert(((long long)comp[u] << 32) + comp[v]).second)
                g[comp[u]].push_back(comp[v]);
    return g;
}

// usage example
int main() {
    vector<vector<int>> graph = {{1}, {0}, {0, 1}};

    vector<vector<int>> components = scc(graph);
    for (auto &component : components) {
        for (int v : component)
            cout << v << " ";
        cout << endl;
    }

    vector<vector<int>> sg = scc_graph(graph, components);
    for (auto &a : sg) {
        for (int v : a)
            cout << v << " ";
        cout << endl;
    }
}
