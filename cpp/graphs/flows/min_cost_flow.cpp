#include <bits/stdc++.h>

using namespace std;

// https://e-maxx-eng.appspot.com/graph/min_cost_flow.html

const int maxnodes = 200000;

int nodes = maxnodes;
int prio[maxnodes], curflow[maxnodes], prevedge[maxnodes], prevnode[maxnodes], q[maxnodes], pot[maxnodes];
bool inqueue[maxnodes];

struct edge {
    int to, f, cap, cost, rev;
};

vector<edge> graph[maxnodes];

void add_edge(int s, int t, int cap, int cost) {
    edge a = {t, 0, cap, cost, (int) graph[t].size()};
    edge b = {s, 0, 0, -cost, (int) graph[s].size()};
    graph[s].emplace_back(a);
    graph[t].emplace_back(b);
}

void bellman_ford(int s, int *dist) {
    fill(dist, dist + nodes, INT_MAX);
    dist[s] = 0;
    int qt = 0;
    q[qt++] = s;
    for (int qh = 0; (qh - qt) % nodes != 0; qh++) {
        int u = q[qh % nodes];
        inqueue[u] = false;
        for (auto &e : graph[u]) {
            if (e.cap <= e.f) continue;
            int v = e.to;
            int ndist = dist[u] + e.cost;
            if (dist[v] > ndist) {
                dist[v] = ndist;
                if (!inqueue[v]) {
                    inqueue[v] = true;
                    q[qt++ % nodes] = v;
                }
            }
        }
    }
}

tuple<int, int> min_cost_flow(int s, int t, int maxf) {
    // bellman_ford can be safely commented if edges costs are non-negative
    bellman_ford(s, pot);
    int flow = 0;
    int flow_cost = 0;
    while (flow < maxf) {
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> q;
        q.emplace(0, s);
        fill(prio, prio + nodes, INT_MAX);
        prio[s] = 0;
        curflow[s] = INT_MAX;
        while (!q.empty()) {
            auto[d, u] = q.top();
            q.pop();
            if (d != prio[u])
                continue;
            for (int i = 0; i < graph[u].size(); i++) {
                edge &e = graph[u][i];
                int v = e.to;
                if (e.cap <= e.f) continue;
                int nprio = prio[u] + e.cost + pot[u] - pot[v];
                if (prio[v] > nprio) {
                    prio[v] = nprio;
                    q.emplace(nprio, v);
                    prevnode[v] = u;
                    prevedge[v] = i;
                    curflow[v] = min(curflow[u], e.cap - e.f);
                }
            }
        }
        if (prio[t] == INT_MAX)
            break;
        for (int i = 0; i < nodes; i++)
            pot[i] += prio[i];
        int df = min(curflow[t], maxf - flow);
        flow += df;
        for (int v = t; v != s; v = prevnode[v]) {
            edge &e = graph[prevnode[v]][prevedge[v]];
            e.f += df;
            graph[v][e.rev].f -= df;
            flow_cost += df * e.cost;
        }
    }
    return {flow, flow_cost};
}

// Usage example
int main() {
    int capacity[][3] = {{0, 3, 2},
                         {0, 0, 2},
                         {0, 0, 0}};
    nodes = 3;
    for (int i = 0; i < nodes; i++)
        for (int j = 0; j < nodes; j++)
            if (capacity[i][j] != 0)
                add_edge(i, j, capacity[i][j], 1);

    int s = 0;
    int t = 2;
    auto[flow, flow_cost] = min_cost_flow(s, t, INT_MAX);

    cout << (4 == flow) << endl;
    cout << (6 == flow_cost) << endl;
}
