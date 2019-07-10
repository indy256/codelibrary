#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)

const int maxnodes = 5000;

int nodes = maxnodes, src, dest;
int dist[maxnodes], q[maxnodes], work[maxnodes];

struct Edge {
    int to, rev, f, cap;
};

vector<Edge> g[maxnodes];

// Adds bidirectional edge
void add_edge(int s, int t, int cap) {
    Edge a = {t, (int) g[t].size(), 0, cap};
    Edge b = {s, (int) g[s].size(), 0, cap};
    g[s].emplace_back(a);
    g[t].emplace_back(b);
}

bool dinic_bfs() {
    fill(dist, dist + nodes, -1);
    dist[src] = 0;
    int qt = 0;
    q[qt++] = src;
    for (int qh = 0; qh < qt; qh++) {
        int u = q[qh];
        for (auto &e : g[u]) {
            int v = e.to;
            if (dist[v] < 0 && e.f < e.cap) {
                dist[v] = dist[u] + 1;
                q[qt++] = v;
            }
        }
    }
    return dist[dest] >= 0;
}

int dinic_dfs(int u, int f) {
    if (u == dest)
        return f;
    for (int &i = work[u]; i < g[u].size(); i++) {
        Edge &e = g[u][i];
        if (e.cap <= e.f) continue;
        int v = e.to;
        if (dist[v] == dist[u] + 1) {
            int df = dinic_dfs(v, min(f, e.cap - e.f));
            if (df > 0) {
                e.f += df;
                g[v][e.rev].f -= df;
                return df;
            }
        }
    }
    return 0;
}

int max_flow(int _src, int _dest) {
    src = _src;
    dest = _dest;
    int result = 0;
    while (dinic_bfs()) {
        fill(work, work + nodes, 0);
        while (int delta = dinic_dfs(src, numeric_limits<int>::max()))
            result += delta;
    }
    return result;
}

vector<bool> min_cut() {
    vector<bool> cut(nodes);
    for (int i = 0; i < nodes; ++i) {
        cut[i] = dist[i] != -1;
    }
    return cut;
}

void clear_flow() {
    for (int i = 0; i < maxnodes; ++i)
        for (Edge &e : g[i])
            e.f = 0;
}
