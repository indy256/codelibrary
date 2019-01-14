#include <bits/stdc++.h>
#include <optional>

using namespace std;

int dfs(vector<vector<int>> &graph, int u, vector<int> &color, vector<int> &next) {
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

optional<vector<int>> find_cycle(vector<vector<int>> &graph) {
    int n = graph.size();
    vector<int> color(n);
    vector<int> next(n);
    for (int u = 0; u < n; u++) {
        if (color[u] != 0) continue;
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
    vector<vector<int>> graph1{{1},
                               {2},
                               {0}};
    auto cycle1 = find_cycle(graph1);
    cout << cycle1.has_value() << endl;
    copy(cycle1->begin(), cycle1->end(), ostream_iterator<int>(cout, " "));
    cout << endl;

    vector<vector<int>> graph2{{1},
                               {2},
                               {}};
    auto cycle2 = find_cycle(graph2);
    cout << cycle2.has_value() << endl;
}
