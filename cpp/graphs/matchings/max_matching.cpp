#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Hopcroft–Karp_algorithm in (E * sqrt(V))

const int MAXV1 = 50000;
const int MAXV2 = 50000;
const int MAXE = 150000;

int n1, n2, edges, last[MAXV1], previous[MAXE], head[MAXE];
int matching[MAXV2], dist[MAXV1], Q[MAXV1];
bool used[MAXV1], vis[MAXV1];

void reset_graph(int n1, int n2) {
    ::n1 = n1;
    ::n2 = n2;
    edges = 0;
    fill(last, last + n1, -1);
}

void add_edge(int u, int v) {
    head[edges] = v;
    previous[edges] = last[u];
    last[u] = edges++;
}

void bfs() {
    fill(dist, dist + n1, -1);
    int sizeQ = 0;
    for (int u = 0; u < n1; ++u) {
        if (!used[u]) {
            Q[sizeQ++] = u;
            dist[u] = 0;
        }
    }
    for (int i = 0; i < sizeQ; i++) {
        int u1 = Q[i];
        for (int e = last[u1]; e >= 0; e = previous[e]) {
            int u2 = matching[head[e]];
            if (u2 >= 0 && dist[u2] < 0) {
                dist[u2] = dist[u1] + 1;
                Q[sizeQ++] = u2;
            }
        }
    }
}

bool dfs(int u1) {
    vis[u1] = true;
    for (int e = last[u1]; e >= 0; e = previous[e]) {
        int v = head[e];
        int u2 = matching[v];
        if (u2 < 0 || !vis[u2] && dist[u2] == dist[u1] + 1 && dfs(u2)) {
            matching[v] = u1;
            used[u1] = true;
            return true;
        }
    }
    return false;
}

int max_matching() {
    fill(used, used + n1, false);
    fill(matching, matching + n2, -1);
    for (int res = 0;;) {
        bfs();
        fill(vis, vis + n1, false);
        int f = 0;
        for (int u = 0; u < n1; ++u)
            if (!used[u] && dfs(u))
                ++f;
        if (!f)
            return res;
        res += f;
    }
}

int main() {
    reset_graph(2, 2);

    add_edge(0, 0);
    add_edge(0, 1);
    add_edge(1, 1);

    cout << (2 == max_matching()) << endl;
}
