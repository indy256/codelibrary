#include <bits/stdc++.h>

using namespace std;

const int maxnodes = 200'000;
const int maxedges = 1000'000;

// graph
int edges;
int last[maxnodes], head[maxedges], previous[maxedges], len[maxedges];
int prio[maxnodes], pred[maxnodes];

void reset_graph() {
    fill(last, last + maxnodes, -1);
    edges = 0;
}

void add_edge(int u, int v, int length) {
    head[edges] = v;
    len[edges] = length;
    previous[edges] = last[u];
    last[u] = edges++;
}

// heap
int h[maxnodes];
int pos2Id[maxnodes];
int id2Pos[maxnodes];
int hsize;

void hswap(int i, int j) {
    swap(h[i], h[j]);
    swap(pos2Id[i], pos2Id[j]);
    swap(id2Pos[pos2Id[i]], id2Pos[pos2Id[j]]);
}

void move_up(int pos) {
    while (pos > 0) {
        int parent = (pos - 1) >> 1;
        if (h[pos] >= h[parent]) {
            break;
        }
        hswap(pos, parent);
        pos = parent;
    }
}

void add(int id, int prio) {
    h[hsize] = prio;
    pos2Id[hsize] = id;
    id2Pos[id] = hsize;
    move_up(hsize++);
}

void decrease_value(int id, int prio) {
    int pos = id2Pos[id];
    h[pos] = prio;
    move_up(pos);
}

void move_down(int pos) {
    while (pos < (hsize >> 1)) {
        int child = 2 * pos + 1;
        if (child + 1 < hsize && h[child + 1] < h[child]) {
            ++child;
        }
        if (h[pos] <= h[child]) {
            break;
        }
        hswap(pos, child);
        pos = child;
    }
}

int remove_min() {
    int res = pos2Id[0];
    int lastNode = h[--hsize];
    if (hsize > 0) {
        h[0] = lastNode;
        int id = pos2Id[hsize];
        id2Pos[id] = 0;
        pos2Id[0] = id;
        move_down(0);
    }
    return res;
}

void dijkstra(int s) {
    fill(pred, pred + maxnodes, -1);
    fill(prio, prio + maxnodes, numeric_limits<int>::max());
    prio[s] = 0;
    hsize = 0;
    add(s, prio[s]);

    while (hsize) {
        int u = remove_min();
        for (int e = last[u]; e >= 0; e = previous[e]) {
            int v = head[e];
            int nprio = prio[u] + len[e];
            if (prio[v] > nprio) {
                if (prio[v] == numeric_limits<int>::max())
                    add(v, nprio);
                else
                    decrease_value(v, nprio);
                prio[v] = nprio;
                pred[v] = u;
            }
        }
    }
}

int main() {
    reset_graph();
    add_edge(0, 1, 10);
    add_edge(1, 2, -5);
    add_edge(0, 2, 8);

    dijkstra(0);

    for (int i = 0; i < 3; i++)
        cout << prio[i] << endl;
}
