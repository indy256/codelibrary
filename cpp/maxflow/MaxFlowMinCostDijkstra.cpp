#include <queue>
#include <climits>
#include <iostream>
using namespace std;

typedef pair<int, int> pii;

const int maxnodes = 200000;
const int maxedges = 1000000;

int edges, nodes;
int last[maxnodes], head[maxedges], previous[maxedges];
int flow[maxedges], cap[maxedges], cost[maxedges];
int prio[maxnodes], curflow[maxnodes], edge[maxnodes];

void graphClear(int _nodes) {
    fill(last, last + maxnodes, -1);
    edges = 0;
    nodes = _nodes;
}

void addEdge1(int u, int v, int _cap, int _cost) {
    head[edges] = v;
    cap[edges] = _cap;
    cost[edges] = _cost;
    flow[edges] = 0;
    previous[edges] = last[u];
    last[u] = edges++;
}

void addEdge(int u, int v, int cap, int cost) {
    addEdge1(u, v, cap, cost);
    addEdge1(v, u, 0, -cost);
}

pii maxFlowOfMinCost(int s, int t) {
    int f = 0;
    int flowCost = 0;
    while (true) {
        priority_queue<pii, vector<pii> , greater<pii> > q;
        q.push(make_pair(0, s));
        fill(prio, prio + nodes, INT_MAX);
        prio[s] = 0;
        curflow[s] = INT_MAX;
        while (!q.empty()) {
            int d = q.top().first;
            int u = q.top().second;
            q.pop();
            if (d != prio[u])
                continue;
            for (int e = last[u]; e >= 0; e = previous[e]) {
                int v = head[e];
                int nprio = d + cost[e];
                if (cap[e] > flow[e] && prio[v] > nprio) {
                    prio[v] = nprio;
                    q.push(make_pair(nprio, v));
                    edge[v] = e;
                    curflow[v] = min(curflow[u], cap[e] - flow[e]);
                }
            }
        }
        if (prio[t] == INT_MAX)
            break;
        int df = curflow[t];
        f += df;
        flowCost += df * prio[t];
        for (int v = t; v != s; v = head[edge[v] ^ 1]) {
            flow[edge[v]] += df;
            flow[edge[v] ^ 1] -= df;
        }
    }
    return make_pair(f, flowCost);
}

int main() {
    int capacity[3][3] = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
    int n = 3;
    graphClear(n);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (capacity[i][j] != 0) {
                addEdge(i, j, capacity[i][j], 1);
            }
        }
    }
    int s = 0;
    int t = 2;
    pii res = maxFlowOfMinCost(s, t);
    int flow = res.first;
    int flowCost = res.second;
    cout << (4 == flow) << endl;
    cout << (6 == flowCost) << endl;
}

