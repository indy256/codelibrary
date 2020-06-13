#include <bits/stdc++.h>

using namespace std;

// Negative cycles canceling algorithm in O(E^2 * V * MAX_EDGE_FLOW * MAX_EDGE_COST)
// negative-cost edges are allowed
// negative-cost cycles are allowed

struct Edge {
    int to, rev, f, cap, cost;
};

struct min_cost_circulation {
    vector<vector<Edge>> graph;

    min_cost_circulation(int nodes) : graph(nodes) {}

    void add_bidi_edge(int s, int t, int cap, int cost) {
        Edge a = {t, (int)graph[t].size(), 0, cap, cost};
        Edge b = {s, (int)graph[s].size(), 0, 0, -cost};
        graph[s].emplace_back(a);
        graph[t].emplace_back(b);
    }

    int calc_min_cost_circulation() {
        int circulation_cost = 0;
        int n = graph.size();
        constexpr int INF = 1000'000'000;
        for (bool found = true; found;) {
            found = false;
            vector<int> dist(n, INF);
            vector<int> p(n, -1);
            for (int i = 0; i < n; ++i) {
                if (dist[i] == INF) {
                    dist[i] = 0;
                    vector<int> q;
                    q.emplace_back(i);
                    for (int j = 0; j < n && !q.empty(); ++j) {
                        vector<int> nq;
                        for (int u : q)
                            for (auto &e : graph[u])
                                if (e.f < e.cap)
                                    if (dist[e.to] > dist[u] + e.cost) {
                                        dist[e.to] = dist[u] + e.cost;
                                        p[e.to] = u;
                                        nq.push_back(e.to);
                                    }
                        q = nq;
                        sort(q.begin(), q.end());
                        q.erase(unique(q.begin(), q.end()), q.end());
                    }
                    if (!q.empty()) {
                        int leaf = q[0];
                        vector<int> path;
                        for (int u = leaf; u != -1; u = p[u])
                            if (find(path.begin(), path.end(), u) == path.end())
                                path.push_back(u);
                            else {
                                path.erase(path.begin(), find(path.begin(), path.end(), u));
                                break;
                            }
                        for (size_t j = 0; j < path.size(); ++j) {
                            int to = path[j];
                            int u = path[(j + 1) % path.size()];
                            for (auto &e : graph[u]) {
                                if (e.to == to) {
                                    e.f += 1;
                                    graph[u][e.rev].f -= 1;
                                    circulation_cost += e.cost;
                                }
                            }
                        }
                        found = true;
                    }
                }
            }
        }
        return circulation_cost;
    }
};

// Usage example
int main() {
    int capacity[][3] = {{0, 1, 0}, {0, 0, 1}, {1, 0, 0}};
    int cost[][3] = {{0, -1, 0}, {0, 0, 1}, {-1, 0, 0}};
    int nodes = 3;
    min_cost_circulation mcf(nodes);
    for (int i = 0; i < nodes; i++)
        for (int j = 0; j < nodes; j++)
            if (capacity[i][j] != 0)
                mcf.add_bidi_edge(i, j, capacity[i][j], cost[i][j]);

    int circulation_cost = mcf.calc_min_cost_circulation();
    cout << circulation_cost << endl;
}
