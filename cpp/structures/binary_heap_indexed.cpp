#include "binary_heap_indexed.h"

#include <bits/stdc++.h>

using namespace std;

// usage example
int main() {
    binary_heap_indexed<int> h(5);
    h.add(0, 50);
    h.add(1, 30);
    h.add(2, 40);
    h.change_value(0, 20);
    h.remove(1);
    while (h.size) {
        cout << h.remove_min() << endl;
    }
}
