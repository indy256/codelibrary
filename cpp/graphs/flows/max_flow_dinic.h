#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)

struct Edge {
    int to, rev, f, cap;
};

struct max_flow_dinic {
    vector<vector<Edge>> g;
    vector<int> dist;

    max_flow_dinic(int nodes) : g(nodes), dist(nodes) {}

    void add_bidi_edge(int s, int t, int cap) {
        Edge a = {t, (int) g[t].size(), 0, cap};
        Edge b = {s, (int) g[s].size(), 0, cap};
        g[s].emplace_back(a);
        g[t].emplace_back(b);
    }

    bool dinic_bfs(int src, int dest) {
        fill(dist.begin(), dist.end(), -1);
        dist[src] = 0;
        vector<int> q(g.size());
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

    int dinic_dfs(vector<int> &ptr, int u, int dest, int f) {
        if (u == dest)
            return f;
        for (int &i = ptr[u]; i < (int) g[u].size(); i++) {
            Edge &e = g[u][i];
            if (e.cap <= e.f) continue;
            int v = e.to;
            if (dist[v] == dist[u] + 1) {
                int df = dinic_dfs(ptr, v, dest, min(f, e.cap - e.f));
                if (df > 0) {
                    e.f += df;
                    g[v][e.rev].f -= df;
                    return df;
                }
            }
        }
        return 0;
    }

    int max_flow(int src, int dest) {
        int flow = 0;
        while (dinic_bfs(src, dest)) {
            vector<int> ptr(g.size());
            while (int delta = dinic_dfs(ptr, src, dest, numeric_limits<int>::max()))
                flow += delta;
        }
        return flow;
    }

    // invoke after max_flow()
    vector<bool> min_cut() {
        vector<bool> cut(g.size());
        for (size_t i = 0; i < cut.size(); ++i) {
            cut[i] = dist[i] != -1;
        }
        return cut;
    }

    void clear_flow() {
        for (auto &v : g)
            for (Edge &e : v)
                e.f = 0;
    }
};
