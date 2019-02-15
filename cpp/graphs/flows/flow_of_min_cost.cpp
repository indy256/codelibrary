#include <bits/stdc++.h>

using namespace std;

const int MAXN = 100 * 2;
int n;
struct edge {
    int v, to, u, f, c;
};
vector<edge> edges;
vector<int> g[MAXN];

void add_edge(int v, int to, int cap, int cost) {
    edge e1 = {v, to, cap, 0, cost};
    edge e2 = {to, v, 0, 0, -cost};
    g[v].push_back((int) edges.size());
    edges.push_back(e1);
    g[to].push_back((int) edges.size());
    edges.push_back(e2);
}

int main() {
    const int INF = 1000000000;
    for (bool found = true; found;) {
        found = false;

        vector<int> d(n, INF);
        vector<int> par(n, -1);
        for (int i = 0; i < n; ++i) {
            if (d[i] == INF) {
                d[i] = 0;
                vector<int> q, nq;
                q.push_back(i);
                for (int it = 0; it < n && q.size(); ++it) {
                    nq.clear();
                    sort(q.begin(), q.end());
                    q.erase(unique(q.begin(), q.end()), q.end());
                    for (size_t j = 0; j < q.size(); ++j) {
                        int v = q[j];
                        for (size_t k = 0; k < g[v].size(); ++k) {
                            int id = g[v][k];
                            if (edges[id].f < edges[id].u)
                                if (d[v] + edges[id].c < d[edges[id].to]) {
                                    d[edges[id].to] = d[v] + edges[id].c;
                                    par[edges[id].to] = v;
                                    nq.push_back(edges[id].to);
                                }
                        }
                    }
                    swap(q, nq);
                }
                if (q.size()) {
                    int leaf = q[0];
                    vector<int> path;
                    for (int v = leaf; v != -1; v = par[v])
                        if (find(path.begin(), path.end(), v) == path.end())
                            path.push_back(v);
                        else {
                            path.erase(path.begin(), find(path.begin(), path.end(), v));
                            break;
                        }
                    for (size_t j = 0; j < path.size(); ++j) {
                        int to = path[j], v = path[(j + 1) % path.size()];
                        for (size_t k = 0; k < g[v].size(); ++k)
                            if (edges[g[v][k]].to == to) {
                                int id = g[v][k];
                                edges[id].f += 1;
                                edges[id ^ 1].f -= 1;
                            }
                    }
                    found = true;
                }
            }
        }
    }
}
