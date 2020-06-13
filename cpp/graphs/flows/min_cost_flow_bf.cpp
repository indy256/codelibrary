#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Minimum-cost_flow_problem in O(E * V * FLOW)
// negative-cost edges are allowed
// negative-cost cycles are not allowed

struct Edge {
    int to, rev, cap, f, cost;
};

struct min_cost_flow {
    vector<vector<Edge>> graph;

    min_cost_flow(int nodes) : graph(nodes) {}

    void add_bidi_edge(int s, int t, int cap, int cost) {
        Edge a = {t, (int)graph[t].size(), cap, 0, cost};
        Edge b = {s, (int)graph[s].size(), 0, 0, -cost};
        graph[s].emplace_back(a);
        graph[t].emplace_back(b);
    }

    void bellman_ford(int s, vector<int> &curflow, vector<int> &dist, vector<int> &prevnode, vector<int> &prevedge) {
        int n = graph.size();
        vector<int> q(n);
        vector<bool> inqueue(n);
        fill(dist.begin(), dist.end(), numeric_limits<int>::max());
        dist[s] = 0;
        curflow[s] = numeric_limits<int>::max();
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; qh != qt; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (size_t i = 0; i < graph[u].size(); i++) {
                Edge &e = graph[u][i];
                if (e.cap <= e.f)
                    continue;
                int v = e.to;
                int ndist = dist[u] + e.cost;
                if (dist[v] > ndist) {
                    dist[v] = ndist;
                    prevnode[v] = u;
                    prevedge[v] = i;
                    curflow[v] = min(curflow[u], e.cap - e.f);
                    if (!inqueue[v]) {
                        inqueue[v] = true;
                        q[qt++ % n] = v;
                    }
                }
            }
        }
    }

    tuple<int, int> calc_min_cost_flow(int s, int t, int maxf) {
        int flow = 0;
        int flow_cost = 0;
        size_t n = graph.size();
        vector<int> curflow(n), dist(n), prevnode(n), prevedge(n);
        while (flow < maxf) {
            bellman_ford(s, curflow, dist, prevnode, prevedge);
            if (dist[t] == numeric_limits<int>::max())
                break;
            int df = min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                Edge &e = graph[prevnode[v]][prevedge[v]];
                e.f += df;
                graph[v][e.rev].f -= df;
                flow_cost += df * e.cost;
            }
        }
        return {flow, flow_cost};
    }
};

// Usage example
int main() {
    int capacity[][3] = {{0, 3, 2}, {0, 0, 2}, {0, 0, 0}};
    int nodes = 3;
    min_cost_flow mcf(nodes);
    for (int i = 0; i < nodes; i++)
        for (int j = 0; j < nodes; j++)
            if (capacity[i][j] != 0)
                mcf.add_bidi_edge(i, j, capacity[i][j], 1);

    int s = 0;
    int t = 2;
    auto [flow, flow_cost] = mcf.calc_min_cost_flow(s, t, numeric_limits<int>::max());

    cout << (4 == flow) << endl;
    cout << (6 == flow_cost) << endl;
}
