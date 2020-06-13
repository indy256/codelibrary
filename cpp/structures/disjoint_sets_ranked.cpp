#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/disjoint_set_union.html

tuple<vector<int>, vector<int>> create_sets(int n) {
    vector<int> res(n);
    iota(res.begin(), res.end(), 0);
    vector<int> rank(n);
    return tuple{res, rank};
}

int root(vector<int> &p, int x) {
    return x == p[x] ? x : (p[x] = root(p, p[x]));
}

void unite(vector<int> &p, vector<int> &rank, int a, int b) {
    a = root(p, a);
    b = root(p, b);
    if (a == b)
        return;
    if (rank[a] < rank[b])
        swap(a, b);
    if (rank[a] == rank[b])
        ++rank[a];
    p[b] = a;
}

// usage example
int main() {
    auto [p, rank] = create_sets(3);
    unite(p, rank, 0, 2);
    cout << (0 == root(p, 0)) << endl;
    cout << (1 == root(p, 1)) << endl;
    cout << (0 == root(p, 2)) << endl;
}
