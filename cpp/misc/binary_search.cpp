#include <bits/stdc++.h>

using namespace std;

int binary_search(bool (*f)(int), int from_inclusive, int to_inclusive) {
    // invariant: f[lo] == false, f[hi] == true
    int lo = from_inclusive - 1;
    int hi = to_inclusive + 1;
    // while there are some elements between lo and hi
    while (hi - lo > 1) {
        // invariant: lo < mid < hi
        int mid = (lo + hi) / 2;
        if (!f(mid)) {
            lo = mid;
        } else {
            hi = mid;
        }
    }
    // here lo + 1 == high
    return hi;
}

// binary_search(new bool[5]{false, false, false, true, true}, 0, 4) == 3
int binary_search(bool a[], int from_inclusive, int to_inclusive) {
    // invariant: f[lo] == false, f[hi] == true
    int lo = from_inclusive - 1;
    int hi = to_inclusive + 1;
    // while there are some elements between lo and hi
    while (hi - lo > 1) {
        // invariant: lo < mid < hi
        int mid = (lo + hi) / 2;
        if (!a[mid]) {
            lo = mid;
        } else {
            hi = mid;
        }
    }
    // here lo + 1 == high
    return hi;
}

// usage example
int main() {
    int first_true = binary_search([](int x) { return x >= 4; }, 0, 10);
    cout << (first_true == 4) << endl;

    cout << (binary_search(new bool[3]{false, true, true}, 0, 2) == 1) << endl;
}
