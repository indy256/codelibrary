#include <bits/stdc++.h>

using namespace std;

int dfs(const vector<vector<int>> &graph, int u, vector<int> &color, vector<int> &next) {
    color[u] = 1;
    for (int v : graph[u]) {
        next[u] = v;
        if (color[v] == 0) {
            int cycleStart = dfs(graph, v, color, next);
            if (cycleStart != -1) {
                return cycleStart;
            }
        } else if (color[v] == 1) {
            return v;
        }
    }
    color[u] = 2;
    return -1;
}

vector<int> find_cycle(const vector<vector<int>> &graph) {
    int n = graph.size();
    vector<int> color(n);
    vector<int> next(n);
    for (int u = 0; u < n; u++) {
        if (color[u] != 0)
            continue;
        int cycleStart = dfs(graph, u, color, next);
        if (cycleStart != -1) {
            vector<int> cycle;
            cycle.push_back(cycleStart);
            for (int i = next[cycleStart]; i != cycleStart; i = next[i]) {
                cycle.push_back(i);
            }
            cycle.push_back(cycleStart);
            return cycle;
        }
    }
    return {};
}

// usage example
int main() {
    vector<vector<int>> graph{{1}, {2}, {0}};
    auto cycle = find_cycle(graph);
    cout << cycle.size() << endl;
    for (int x : cycle)
        cout << x << " ";
    cout << endl;

    graph = {{1}, {2}, {}};
    cycle = find_cycle(graph);
    cout << cycle.size() << endl;
}
