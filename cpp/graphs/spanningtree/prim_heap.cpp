#include <bits/stdc++.h>

using namespace std;

typedef pair<int, int> edge;
typedef pair<int, int> item;

// Prim's algorithm in O(E*log(V)) time: https://e-maxx-eng.appspot.com/graph/mst_prim.html
tuple<long long, vector<int>> prim_mst(const vector<vector<edge>> &g) {
    size_t n = g.size();
    vector<int> pred(n, -1);
    vector<int> prio(n, INT_MAX);
    priority_queue<item, vector<item>, greater<>> q;
    q.emplace(prio[0] = 0, 0);
    long long tree_weight = 0;

    while (!q.empty()) {
        auto[w, u] = q.top();
        q.pop();

        if (prio[u] != w)
            continue;
        tree_weight += w;

        for (auto[v, weight] : g[u]) {
            if (prio[v] > weight) {
                prio[v] = weight;
                pred[v] = u;
                q.emplace(weight, v);
            }
        }
    }
    return {tree_weight, pred};
}

// usage example
int main() {
    vector<vector<edge>> graph(3);
    graph[0].emplace_back(1, 10);
    graph[1].emplace_back(0, 10);
    graph[1].emplace_back(2, 10);
    graph[2].emplace_back(1, 10);
    graph[2].emplace_back(0, 5);
    graph[0].emplace_back(2, 5);

    auto[tree_weight, pred] = prim_mst(graph);
    cout << tree_weight << endl;
    copy(pred.begin(), pred.end(), ostream_iterator<int>(cout, " "));
}
