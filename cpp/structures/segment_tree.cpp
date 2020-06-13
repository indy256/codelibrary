#include "segment_tree.h"

#include <bits/stdc++.h>

using namespace std;

// usage example
int main() {
    segtree t(10);
    t.modify(2, 3, 1);
    t.modify(3, 4, 2);
    cout << t.get(2, 3).mx << endl;

    vector<long long> a{1, 2, 10, 20};
    segtree tt(a);
    cout << sum_lower_bound(tt, 0, tt.n - 1, 12) << endl;
}
