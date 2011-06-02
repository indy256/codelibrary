#include <vector>
#include <queue>
#include <climits>
#include <iostream>
using namespace std;

typedef pair<int, int> pii;
typedef vector<vector<pii> > Graph;

long long prim(Graph &g, vector<int> &pred) {
    int n = g.size();
    pred.assign(n, -1);
    vector<bool> vis(n);
    vector<int> prio(n, INT_MAX);
    prio[0] = 0;
    priority_queue<pii, vector<pii> , greater<pii> > q;
    q.push(make_pair(prio[0] , 0));
    long long res = 0;

    while (!q.empty()) {
        int d = q.top().first;
        int u = q.top().second;
        q.pop();
        if (vis[u])
            continue;
        vis[u] = true;
        res += d;
        for (int i = 0; i < (int) g[u].size(); i++) {
            int v = g[u][i].first;
            if (vis[v])
                continue;
            int nprio = g[u][i].second;
            if (prio[v] > nprio) {
                prio[v] = nprio;
                pred[v] = u;
                q.push(make_pair(nprio, v));
            }
        }
    }
    return res;
}

int main() {
    Graph g(3);
    g[0].push_back(make_pair(1, 10));
    g[1].push_back(make_pair(0, 10));
    g[1].push_back(make_pair(2, 10));
    g[2].push_back(make_pair(1, 10));
    g[2].push_back(make_pair(0, 5));
    g[0].push_back(make_pair(2, 5));

    vector<int> pred;
    long long res = prim(g, pred);
    cout << res << endl;
}
