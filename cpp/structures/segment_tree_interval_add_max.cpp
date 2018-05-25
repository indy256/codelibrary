#include <bits/stdc++.h>

using namespace std;

// https://e-maxx-eng.appspot.com/data_structures/segment_tree.html

const int maxn = 200000;
int tmax[4 * maxn];
int tadd[4 * maxn]; // tadd[i] applies to tmax[i], tadd[2*i+1] and tadd[2*i+2]

void push(int root) {
    tmax[root] += tadd[root];
    tadd[2 * root + 1] += tadd[root];
    tadd[2 * root + 2] += tadd[root];
    tadd[root] = 0;
}

int max(int from, int to, int root = 0, int left = 0, int right = maxn - 1) {
    if (from > right || left > to)
        return INT_MIN;
    if (from <= left && right <= to) {
        return tmax[root] + tadd[root];
    }
    push(root);
    int mid = (left + right) / 2;
    int res = std::max(max(from, to, 2 * root + 1, left, mid),
                       max(from, to, 2 * root + 2, mid + 1, right));
    return res;
}

void add(int from, int to, int delta, int root = 0, int left = 0, int right = maxn - 1) {
    if (from > right || left > to)
        return;
    if (from <= left && right <= to) {
        tadd[root] += delta;
        return;
    }
    push(root); // this push may be omitted for add, but is necessary for other operations such as set
    int mid = (left + right) / 2;
    add(from, to, delta, 2 * root + 1, left, mid);
    add(from, to, delta, 2 * root + 2, mid + 1, right);
    tmax[root] = std::max(tmax[2 * root + 1] + tadd[2 * root + 1], tmax[2 * root + 2] + tadd[2 * root + 2]);
}

// usage example
int main() {
    add(0, 9, 1);
    add(2, 4, 2);
    add(3, 5, 3);

    cout << (6 == max(0, 9)) << endl;
    cout << (6 == tmax[0] + tadd[0]) << endl;
    cout << (1 == max(0, 0)) << endl;
}
