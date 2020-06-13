#include <bits/stdc++.h>

using namespace std;

typedef pair<int, int> edge;
typedef pair<int, int> item;

// https://cp-algorithms.com/graph/dijkstra_sparse.html

// O(E*log(V)) time and O(E) memory
tuple<vector<int>, vector<int>> dijkstra_heap(const vector<vector<edge>> &g, int s) {
    size_t n = g.size();
    vector<int> prio(n, numeric_limits<int>::max());
    vector<int> pred(n, -1);
    priority_queue<item, vector<item>, greater<>> q;
    q.emplace(prio[s] = 0, s);

    while (!q.empty()) {
        auto [d, u] = q.top();
        q.pop();

        if (d != prio[u])
            continue;

        for (auto [v, len] : g[u]) {
            int nprio = prio[u] + len;
            if (prio[v] > nprio) {
                prio[v] = nprio;
                pred[v] = u;
                q.emplace(nprio, v);
            }
        }
    }

    return {prio, pred};
}

// O(E*log(V)) time and O(V) memory
tuple<vector<int>, vector<int>> dijkstra_set(const vector<vector<edge>> &g, int s) {
    size_t n = g.size();
    vector<int> prio(n, numeric_limits<int>::max());
    vector<int> pred(n, -1);
    set<item> q;
    q.emplace(prio[s] = 0, s);

    while (!q.empty()) {
        int u = q.begin()->second;
        q.erase(q.begin());

        for (auto [v, len] : g[u]) {
            int nprio = prio[u] + len;
            if (prio[v] > nprio) {
                q.erase({prio[v], v});
                prio[v] = nprio;
                pred[v] = u;
                q.emplace(prio[v], v);
            }
        }
    }

    return {prio, pred};
}

int main() {
    vector<vector<edge>> g(3);
    g[0].emplace_back(1, 10);
    g[1].emplace_back(2, -5);
    g[0].emplace_back(2, 8);

    auto [prio1, pred1] = dijkstra_heap(g, 0);
    auto [prio2, pred2] = dijkstra_set(g, 0);

    for (int x : prio1)
        cout << x << " ";
    cout << endl;

    for (int x : prio2)
        cout << x << " ";
    cout << endl;
}
