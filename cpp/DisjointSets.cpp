#include <algorithm>
#include <iostream>
using namespace std;

const int maxn = 200000;
int rank[maxn];
int p[maxn];
int n;

void init(int _n) {
    n = _n;
    fill(rank, rank + n, 0);
    for (int i = 0; i < n; i++)
        p[i] = i;
}

int root(int x) {
    if (p[x] != x)
        p[x] = root(p[x]);
    return p[x];
}

void unite(int a, int b) {
    a = root(a);
    b = root(b);
    if (rank[a] < rank[b]) {
        p[a] = b;
    } else {
        p[b] = a;
        if (rank[a] == rank[b])
            ++rank[a];
    }
}

int main() {
    init(3);
    unite(0, 2);
    cout << root(0) << endl;
    cout << root(1) << endl;
    cout << root(2) << endl;
}
