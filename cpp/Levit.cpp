#include <algorithm>
#include <climits>
#include <iostream>
using namespace std;

const int maxnodes = 200000;
const int maxedges = 1000000;

int edges, n;
int last[maxnodes], head[maxedges], previous[maxedges], len[maxedges];
int prio[maxnodes], pred[maxnodes], id[maxnodes], q[maxnodes];

void graphClear() {
    fill(last, last + maxnodes, -1);
    edges = 0;
}

void addEdge(int u, int v, int length) {
    head[edges] = v;
    len[edges] = length;
    previous[edges] = last[u];
    last[u] = edges++;
}

void levit(int s) {
    fill(id, id + n, 2);
    fill(pred, pred + n, -1);
    fill(prio, prio + n, INT_MAX);
    prio[s] = 0;
    int qh = 0;
    int qt = 0;
    q[qt++] = s;
    if (qt == n)
        qt = 0;
    while (qh != qt) {
        int u = q[qh++];
        if (qh == n)
            qh = 0;
        id[u] = 0;
        for (int e = last[u]; e >= 0; e = previous[e]) {
            int nprio = prio[u] + len[e];
            int v = head[e];
            if (prio[v] > nprio) {
                prio[v] = nprio;
                pred[v] = u;
                if (id[v] == 2) {
                    q[qt++] = v;
                    if (qt == n)
                        qt = 0;
                } else if (id[v] == 0) {
                    if (--qh == -1)
                        qh = n - 1;
                    q[qh] = v;
                }
                id[v] = 1;
            }
        }
    }
}

int main() {
    graphClear();
    addEdge(0, 1, 10);
    addEdge(1, 2, -5);
    addEdge(0, 2, 8);

    n = 3;
    levit(0);

    for (int i = 0; i < n; i++)
        cout << prio[i] << endl;
}
