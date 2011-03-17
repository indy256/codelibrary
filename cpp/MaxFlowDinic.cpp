#include <algorithm>
#include <climits>
#include <iostream>
using namespace std;

const int maxnodes = 5000;
const int maxedges = 60000;

int src, dest, edges, nodes;
int last[maxnodes], head[maxedges], prev[maxedges];
int flow[maxedges], cap[maxedges];
int dist[maxnodes], Q[maxnodes], work[maxnodes];

void init(int _nodes) {
    fill(last, last + _nodes, -1);
    edges = 0;
    nodes = _nodes;
}

void addEdge(int u, int v, int cap1, int cap2) {
    head[edges] = v;
    cap[edges] = cap1;
    flow[edges] = 0;
    prev[edges] = last[u];
    last[u] = edges++;
    head[edges] = u;
    cap[edges] = cap2;
    flow[edges] = 0;
    prev[edges] = last[v];
    last[v] = edges++;
}

bool dinic_bfs() {
    fill(dist, dist + nodes, -1);
    dist[src] = 0;
    int sizeQ = 0;
    Q[sizeQ++] = src;
    for (int i = 0; i < sizeQ; i++) {
        int u = Q[i];
        for (int e = last[u]; e >= 0; e = prev[e]) {
            int v = head[e];
            if (dist[v] < 0 && flow[e] < cap[e]) {
                dist[v] = dist[u] + 1;
                Q[sizeQ++] = v;
            }
        }
    }
    return dist[dest] >= 0;
}

int dinic_dfs(int u, int f) {
    if (u == dest)
        return f;
    for (int &e = work[u]; e >= 0; e = prev[e]) {
        int v = head[e];
        if (dist[v] == dist[u] + 1 && flow[e] < cap[e]) {
            int df = dinic_dfs(v, min(f, cap[e] - flow[e]));
            if (df > 0) {
                flow[e] += df;
                flow[e ^ 1] -= df;
                return df;
            }
        }
    }
    return 0;
}

int maxFlow(int _src, int _dest) {
    src = _src;
    dest = _dest;
    int result = 0;
    while (dinic_bfs()) {
        copy(last, last + nodes, work);
        while (int delta = dinic_dfs(src, INT_MAX))
            result += delta;
    }
    return result;
}

int main() {
    int n = 3;
    init(n);

    int capacity[][3] = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                addEdge(i, j, capacity[i][j], capacity[i][j]);
    cout << (4 == maxFlow(0, 2)) << endl;
}
