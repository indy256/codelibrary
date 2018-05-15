#include <bits/stdc++.h>

using namespace std;

const int maxnodes = 200000;

int nodes = maxnodes;
int prio[maxnodes], curflow[maxnodes], prevedge[maxnodes], prevnode[maxnodes], q[maxnodes];
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

void bellman_ford(int s) {
    fill(prio, prio + nodes, INT_MAX);
    prio[s] = 0;
    int qt = 0;
    q[qt++] = s;
    for (int qh = 0; (qh - qt) % nodes != 0; qh++) {
        int u = q[qh % nodes];
        inqueue[u] = false;
        for (int i = 0; i < graph[u].size(); i++) {
            edge &e = graph[u][i];
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
                    q[qt++ % nodes] = v;
                }
            }
        }
    }
}

tuple<int, int> min_cost_flow(int s, int t, int maxf) {
    int flow = 0;
    int flow_cost = 0;
    while (flow < maxf) {
        curflow[s] = INT_MAX;
        bellman_ford(s);
        if (prio[t] == INT_MAX)
            break;
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
