#include <bits/stdc++.h>

using namespace std;

// https://e-maxx-eng.appspot.com/graph/bellman_ford.html

typedef pair<int, int> edge;

const int INF = INT_MAX / 3;

tuple<bool, vector<int>, vector<int>> bellman_ford(const vector<vector<edge>> &g, int s) {
    size_t n = g.size();
    vector<int> prio(n, INF);
    prio[s] = 0;
    vector<int> pred(n, -1);
    bool was_changed = true;
    for (int k = 0; k < n; k++) {
        was_changed = false;
        for (int u = 0; u < n; u++) {
            for (auto[v, cost] : g[u]) {
                if (prio[v] > prio[u] + cost) {
                    prio[v] = prio[u] + cost;
                    pred[v] = u;
                    was_changed = true;
                }
            }
        }
        if (!was_changed)
            break;
    }
    // was_changed is true iff graph has a negative cycle
    return {was_changed, prio, pred};
}

vector<int> find_negative_cycle(const vector<vector<edge>> &g) {
    size_t n = g.size();
    vector<int> pred(n, -1);
    vector<int> prio(n, INF);
    prio[0] = 0;
    int last = 0;
    for (int k = 0; k < n; k++) {
        last = -1;
        for (int u = 0; u < n; u++) {
            for (auto[v, cost] : g[u]) {
                if (prio[v] > prio[u] + cost) {
                    prio[v] = prio[u] + cost;
                    pred[v] = u;
                    last = v;
                }
            }
        }
        if (last == -1)
            return {};
    }

    vector<int> path(n);
    vector<int> pos(n, -1);
    for (int i = 0;; i++) {
        if (pos[last] != -1)
            return vector<int>(path.rend() - i, path.rend() - pos[last]);
        path[i] = last;
        pos[last] = i;
        last = pred[last];
    }
}

int main() {
    vector<vector<edge>> g(4);
    g[0].emplace_back(1, 1);
    g[1].emplace_back(0, 1);
    g[1].emplace_back(2, 1);
    g[2].emplace_back(3, -10);
    g[3].emplace_back(1, 1);

    vector<int> cycle = find_negative_cycle(g);
    for (int u : cycle)
        cout << u << " ";
}
