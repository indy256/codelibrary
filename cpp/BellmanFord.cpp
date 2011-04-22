#include <vector>
#include <climits>
#include <iostream>
using namespace std;

typedef pair<int, int> pii;
typedef vector<vector<pii> > Graph;

const int INF = INT_MAX / 3;

bool bellmanFord(Graph &g, int s, vector<int> &prio, vector<int> &pred) {
    int n = g.size();
    pred.assign(n, -1);
    prio.assign(n, INF);
    prio[s] = 0;
    bool wasChanged = true;
    for (int k = 0; k < n; k++) {
        wasChanged = false;
        for (int u = 0; u < n; u++) {
            for (int i = 0; i < (int) g[u].size(); i++) {
                int v = g[u][i].first;
                int cost = g[u][i].second;
                if (prio[v] > prio[u] + cost) {
                    prio[v] = prio[u] + cost;
                    pred[v] = u;
                    wasChanged = true;
                }
            }
        }
        if (!wasChanged)
            break;
    }
    // wasChanged is true iff graph has a negative cycle
    return wasChanged;
}

vector<int> findNegativeCycle(Graph &g) {
    int n = g.size();
    vector<int> pred(n, -1);
    vector<int> prio(n, INF);
    prio[0] = 0;
    int last = 0;
    for (int k = 0; k < n; k++) {
        last = -1;
        for (int u = 0; u < n; u++) {
            for (int i = 0; i < (int) g[u].size(); i++) {
                int v = g[u][i].first;
                int cost = g[u][i].second;
                if (prio[v] > prio[u] + cost) {
                    prio[v] = prio[u] + cost;
                    pred[v] = u;
                    last = v;
                }
            }
        }
        if (last == -1)
            return vector<int>();
    }

    vector<int> path(n);
    vector<int> pos(n, -1);
    for (int i = 0;; i++) {
        if (pos[last] != -1)
            return vector<int>(path.rend() - i, path.rend() - pos[last]);
        path[i] = last;
        pos[last] = i;
        last = pred[last];
    }
}

int main() {
    Graph g(4);
    g[0].push_back(make_pair(1, 1));
    g[1].push_back(make_pair(0, 1));
    g[1].push_back(make_pair(2, 1));
    g[2].push_back(make_pair(3, -10));
    g[3].push_back(make_pair(1, 1));

    vector<int> cycle = findNegativeCycle(g);
    for (int i = 0; i < (int) cycle.size(); i++)
        cout << cycle[i] << " ";
}
