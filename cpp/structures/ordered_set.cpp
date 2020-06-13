#if defined(__GNUC__) && !defined(__clang__)
#include <bits/stdc++.h>

#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>

using namespace std;
using namespace __gnu_pbds;

using ordered_set = tree<int, null_type, less<>, rb_tree_tag, tree_order_statistics_node_update>;
using ordered_map = tree<int, int, less<>, rb_tree_tag, tree_order_statistics_node_update>;

struct tree2d {
    vector<ordered_set> sets;

    tree2d(int n) : sets(n) {}

    void add(int x, int v) {
        for (; x < sets.size(); x |= x + 1)
            sets[x].insert(v);
    }

    void remove(int x, int v) {
        while (x < sets.size()) {
            sets[x].erase(v);
            x += x & -x;
        }
    }

    int query(int x, int y1, int y2) {
        int res = 0;
        for (; x >= 0; x = (x & (x + 1)) - 1)
            res += sets[x].order_of_key(y2 + 1) - sets[x].order_of_key(y1);
        return res;
    }
};

// usage example
int main() {
    ordered_set set;
    ordered_map map;

    map.insert(make_pair(1, 2));

    set.insert(1);
    set.insert(2);
    set.insert(4);
    set.insert(8);
    set.insert(16);

    cout << *set.find_by_order(1) << endl;                // 2
    cout << *set.find_by_order(2) << endl;                // 4
    cout << *set.find_by_order(4) << endl;                // 16
    cout << (set.end() == set.find_by_order(6)) << endl;  // true
    cout << set.order_of_key(-5) << endl;                 // 0
    cout << set.order_of_key(1) << endl;                  // 0
    cout << set.order_of_key(3) << endl;                  // 2
    cout << set.order_of_key(4) << endl;                  // 2
    cout << set.order_of_key(400) << endl;                // 5
    cout << endl;

    tree2d t(10);
    t.add(1, 5);
    t.add(3, 6);
    cout << t.query(3, 5, 6) << endl;
}

#else
int main() {}
#endif
