#include <bits/stdc++.h>

#include "treap.h"

using namespace std;

struct tree_2d {
    vector<pTreap> t;

    tree_2d(int n) : t(2 * n) {}

    long long query(int x1, int x2, int y1, int y2) {
        long long res = 0;
        for (x1 += t.size() / 2, x2 += t.size() / 2; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
            if ((x1 & 1) != 0)
                res += ::query(t[x1], y1, y2).sum;
            if ((x2 & 1) == 0)
                res += ::query(t[x2], y1, y2).sum;
        }
        return res;
    }

    void insert(int x, int y, int value) {
        x += t.size() / 2;
        for (; x > 0; x >>= 1)
            ::insert(t[x], y, value);
    }

    void remove(int x, int y) {
        x += t.size() / 2;
        for (; x > 0; x >>= 1)
            ::remove(t[x], y);
    }
};

// usage example
int main() {
    tree_2d t(10);
    t.insert(1, 5, 3);
    t.insert(3, 3, 2);
    t.insert(2, 6, 1);
    t.remove(2, 6);
    cout << t.query(0, 9, 0, 9) << endl;
}
