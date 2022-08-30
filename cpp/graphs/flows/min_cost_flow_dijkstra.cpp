#include <bits/stdc++.h>

#include "../../structures/binary_heap_indexed.h"

using namespace std;

// https://cp-algorithms.com/graph/min_cost_flow.html in O(E * V + E * logV * FLOW)
// negative-cost edges are allowed
// negative-cost cycles are not allowed

struct Edge {
    int to, rev, cap, f, cost;
};

struct min_cost_flow {
    vector<vector<Edge>> graph;

    min_cost_flow(int nodes) : graph(nodes) {}

    void add_edge(int s, int t, int cap, int cost) {
        Edge a = {t, (int)graph[t].size(), cap, 0, cost};
        Edge b = {s, (int)graph[s].size(), 0, 0, -cost};
        graph[s].emplace_back(a);
        graph[t].emplace_back(b);
    }

    void bellman_ford(int s, vector<int> &dist) {
        int n = graph.size();
        vector<int> q(n);
        vector<bool> inqueue(n);
        fill(dist.begin(), dist.end(), numeric_limits<int>::max());
        dist[s] = 0;
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; qh != qt; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (auto &e : graph[u]) {
                if (e.cap <= e.f)
                    continue;
                int v = e.to;
                int ndist = dist[u] + e.cost;
                if (dist[v] > ndist) {
                    dist[v] = ndist;
                    if (!inqueue[v]) {
                        inqueue[v] = true;
                        q[qt++ % n] = v;
                    }
                }
            }
        }
    }

    void dijkstra(int s, int t, vector<int> &pot, vector<int> &dist, vector<bool> &finished, vector<int> &curflow,
                  vector<int> &prevnode, vector<int> &prevedge) {
        binary_heap_indexed<int> h(graph.size());
        h.add(s, 0);
        fill(dist.begin(), dist.end(), numeric_limits<int>::max());
        dist[s] = 0;
        curflow[s] = numeric_limits<int>::max();
        fill(finished.begin(), finished.end(), false);
        while (!finished[t] && h.size != 0) {
            int u = h.remove_min();
            finished[u] = true;
            for (size_t i = 0; i < graph[u].size(); i++) {
                Edge &e = graph[u][i];
                if (e.f >= e.cap)
                    continue;
                int v = e.to;
                int nprio = dist[u] + e.cost + pot[u] - pot[v];

                if (dist[v] > nprio) {
                    if (dist[v] == numeric_limits<int>::max())
                        h.add(v, nprio);
                    else
                        h.change_value(v, nprio);
                    dist[v] = nprio;
                    prevnode[v] = u;
                    prevedge[v] = i;
                    curflow[v] = min(curflow[u], e.cap - e.f);
                }
            }
        }
    }

    tuple<int, int> cal_min_cost_flow(int s, int t, int maxf) {
        size_t n = graph.size();
        vector<int> pot(n), curflow(n), dist(n), prevnode(n), prevedge(n);
        vector<bool> finished(n);
        bellman_ford(s, pot);  // this can be commented out if edges costs are non-negative
        int flow = 0;
        int flow_cost = 0;
        while (flow < maxf) {
            dijkstra(s, t, pot, dist, finished, curflow, prevnode, prevedge);
            if (dist[t] == numeric_limits<int>::max())
                break;
            for (size_t i = 0; i < n; i++)
                if (finished[i])
                    pot[i] += dist[i] - dist[t];
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
                mcf.add_edge(i, j, capacity[i][j], 1);

    int s = 0;
    int t = 2;
    auto [flow, flow_cost] = mcf.cal_min_cost_flow(s, t, numeric_limits<int>::max());

    cout << (4 == flow) << endl;
    cout << (6 == flow_cost) << endl;
}
