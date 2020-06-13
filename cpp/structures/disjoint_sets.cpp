#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/disjoint_set_union.html

vector<int> create_sets(int n) {
    vector<int> res(n);
    iota(res.begin(), res.end(), 0);
    return res;
}

int root(vector<int> &p, int x) {
    return x == p[x] ? x : (p[x] = root(p, p[x]));
}

void unite(vector<int> &p, int a, int b) {
    a = root(p, a);
    b = root(p, b);
    p[b] = a;
}

// usage example
int main() {
    vector<int> p = create_sets(3);
    unite(p, 0, 2);
    cout << (0 == root(p, 0)) << endl;
    cout << (1 == root(p, 1)) << endl;
    cout << (0 == root(p, 2)) << endl;
}
