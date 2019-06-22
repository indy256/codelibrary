#include <bits/stdc++.h>
#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>

using namespace std;
using namespace __gnu_pbds;

using ordered_set = tree<int, null_type, less<>, rb_tree_tag, tree_order_statistics_node_update>;
using ordered_map = tree<int, int, less<>, rb_tree_tag, tree_order_statistics_node_update>;

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

    cout << *set.find_by_order(1) << endl; // 2
    cout << *set.find_by_order(2) << endl; // 4
    cout << *set.find_by_order(4) << endl; // 16
    cout << (set.end() == set.find_by_order(6)) << endl; // true
    cout << set.order_of_key(-5) << endl; // 0
    cout << set.order_of_key(1) << endl; // 0
    cout << set.order_of_key(3) << endl; // 2
    cout << set.order_of_key(4) << endl; // 2
    cout << set.order_of_key(400) << endl; // 5
}
