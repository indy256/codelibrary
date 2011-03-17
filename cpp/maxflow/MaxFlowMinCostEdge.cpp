#include <vector>
#include <queue>
#include <climits>
#include <iostream>
using namespace std;

class Edge {
    public:
    int s, t, rev;
    int cap, cost, f;
    Edge(int s, int t, int rev, int cap, int cost) :
        s(s), t(t), rev(rev), cap(cap), cost(cost), f(0) {
    }
};

typedef pair<int, int> pii;
typedef vector<vector<Edge> > Graph;

void addEdge(Graph &g, int s, int t, int cap, int cost) {
    g[s].push_back(Edge(s, t, g[t].size(), cap, cost));
    g[t].push_back(Edge(t, s, g[s].size() - 1, 0, -cost));
}

pii maxFlowOfMinCost(Graph &g, int s, int t) {
    int INF = INT_MAX / 2;
    int flow = 0;
    int flowCost = 0;
    vector<int> u(g.size());
    while (true) {
        priority_queue<pii, vector<pii> , greater<pii> > q;
        q.push(make_pair(0, s));
        vector<Edge*> path(g.size());
        vector<int> prio(g.size(), INF);
        prio[s] = 0;
        while (!q.empty()) {
            int cur_prio = q.top().first;
            int cur_v = q.top().second;
            q.pop();
            if (cur_prio != prio[cur_v])
                continue;
            for (int i = 0; i < (int) g[cur_v].size(); i++) {
                Edge &e = g[cur_v][i];
                int nprio = cur_prio + e.cost + u[cur_v] - u[e.t];
                if (e.cap > e.f && prio[e.t] > nprio) {
                    prio[e.t] = nprio;
                    q.push(make_pair(nprio, e.t));
                    path[e.t] = &e;
                }
            }
        }
        if (prio[t] == INF)
            break;
        for (int i = 0; i < (int) g.size(); i++)
            if (prio[i] < INF)
                u[i] += prio[i];
        int df = INT_MAX;
        for (int v = t; v != s; v = path[v]->s)
            df = min(df, path[v]->cap - path[v]->f);
        for (int v = t; v != s; v = path[v]->s) {
            path[v]->f += df;
            g[v][path[v]->rev].f -= df;
            flowCost += df * path[v]->cost;
        }
        flow += df;
    }
    return make_pair(flow, flowCost);
}

int main() {
    int capacity[3][3] = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
    int n = 3;
    Graph g(n);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (capacity[i][j] != 0) {
                addEdge(g, i, j, capacity[i][j], 1);
            }
        }
    }
    int s = 0;
    int t = 2;
    pii res = maxFlowOfMinCost(g, s, t);
    int flow = res.first;
    int flowCost = res.second;
    cout << (4 == flow) << endl;
    cout << (6 == flowCost) << endl;
}
