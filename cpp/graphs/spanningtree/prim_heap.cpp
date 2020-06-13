#include <bits/stdc++.h>

using namespace std;

using edge = pair<int, int>;  // (v, cost)
using item = pair<int, int>;  // (cost, v)

// Prim's algorithm in O(E*log(V)) time: https://cp-algorithms.com/graph/mst_prim.html
tuple<long long, vector<int>> prim_mst(const vector<vector<edge>> &g) {
    size_t n = g.size();
    vector<int> pred(n, -1);
    vector<int> prio(n, numeric_limits<int>::max());
    priority_queue<item, vector<item>, greater<>> q;
    q.emplace(prio[0] = 0, 0);
    long long tree_cost = 0;

    while (!q.empty()) {
        auto [c, u] = q.top();
        q.pop();

        if (prio[u] != c)
            continue;
        tree_cost += c;

        for (auto [v, cost] : g[u]) {
            if (prio[v] > cost) {
                prio[v] = cost;
                pred[v] = u;
                q.emplace(cost, v);
            }
        }
    }
    return {tree_cost, pred};
}

// usage example
int main() {
    vector<vector<edge>> graph = {{edge{1, 10}, edge{2, 5}}, {edge{0, 10}, edge{2, 10}}, {edge{1, 10}, edge{0, 5}}};
    auto [tree_weight, pred] = prim_mst(graph);
    cout << tree_weight << endl;
    for (int x : pred)
        cout << x << " ";
    cout << endl;
}
