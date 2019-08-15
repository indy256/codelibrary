#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Minimum-cost_flow_problem in O(min(E^2*V^2, E*V*FLOW))

struct Edge {
    int to, rev, f, cap, cost;
};

struct min_cost_flow {
    vector<vector<Edge>> g;

    min_cost_flow(int nodes) : g(nodes) {}

    void add_bidi_edge(int s, int t, int cap, int cost) {
        Edge a = {t, (int) g[t].size(), 0, cap, cost};
        Edge b = {s, (int) g[s].size(), 0, 0, -cost};
        g[s].emplace_back(a);
        g[t].emplace_back(b);
    }

    void bellman_ford(int s, vector<int> &curflow, vector<int> &prio, vector<int> &prevnode, vector<int> &prevedge) {
        int n = g.size();
        vector<int> q(n);
        vector<bool> inqueue(n);
        fill(prio.begin(), prio.end(), numeric_limits<int>::max());
        prio[s] = 0;
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; qh != qt; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (size_t i = 0; i < g[u].size(); i++) {
                Edge &e = g[u][i];
                if (e.cap <= e.f) continue;
                int v = e.to;
                int ndist = prio[u] + e.cost;
                if (prio[v] > ndist) {
                    prio[v] = ndist;
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
        size_t n = g.size();
        vector<int> curflow(n), prio(n), prevnode(n), prevedge(n);
        while (flow < maxf) {
            curflow[s] = numeric_limits<int>::max();
            bellman_ford(s, curflow, prio, prevnode, prevedge);
            if (prio[t] == numeric_limits<int>::max())
                break;
            int df = min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                Edge &e = g[prevnode[v]][prevedge[v]];
                e.f += df;
                g[v][e.rev].f -= df;
                flow_cost += df * e.cost;
            }
        }
        return {flow, flow_cost};
    }
};

// Usage example
int main() {
    int capacity[][3] = {{0, 3, 2},
                         {0, 0, 2},
                         {0, 0, 0}};
    int n = 3;
    min_cost_flow mcf(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                mcf.add_bidi_edge(i, j, capacity[i][j], 1);

    int s = 0;
    int t = 2;
    auto[flow, flow_cost] = mcf.calc_min_cost_flow(s, t, numeric_limits<int>::max());

    cout << (4 == flow) << endl;
    cout << (6 == flow_cost) << endl;
}
