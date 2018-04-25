#include <bits/stdc++.h>

using namespace std;

typedef pair<int, int> edge;
typedef pair<int, int> item;

// https://en.wikipedia.org/wiki/Prim%27s_algorithm in O(E*log(V))
auto prim_mst(const vector<vector<edge>> &g) {
    size_t n = g.size();
    unique_ptr<int[]> pred(new int[n]);
    unique_ptr<int[]> prio(new int[n]);
    for (int i = 0; i < n; ++i) pred[i] = -1;
    for (int i = 0; i < n; ++i) prio[i] = INT_MAX;
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
    return make_tuple(tree_weight, move(pred));
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
    copy(&pred[0], &pred[3], ostream_iterator<int>(cout, " "));
}
