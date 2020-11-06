#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/All_nearest_smaller_values
vector<int> nsv(const vector<int> &a) {
    int n = a.size();
    vector<int> p(n);
    for (int i = 0; i < n; i++) {
        int j = i - 1;
        while (j != -1 && a[j] >= a[i]) {
            j = p[j];
        }
        p[i] = j;
    }
    return p;
}

// usage example
int main() {
    const vector<int> p = nsv({1, 1, 3, 2});

    for (int x : p)
        cout << x << " ";
    cout << endl;
}
