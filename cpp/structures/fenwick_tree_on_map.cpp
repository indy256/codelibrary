#include <bits/stdc++.h>

using namespace std;

const int n = 2000000000;

void add(map<int, int> &t, int i, int value) {
    for (; i < n; i |= i + 1)
        t[i] += value;
}

// sum[0,i]
int sum(map<int, int> &t, int i) {
    int res = 0;
    for (; i >= 0; i = (i & (i + 1)) - 1)
        if (t.count(i)) res += t[i];
    return res;
}

// Usage example
int main() {
    map<int, int> t;
    add(t, 0, 4);
    add(t, 1, 5);
    add(t, 2, 5);
    add(t, 2, 5);

    cout << (4 == sum(t, 0)) << endl;
    cout << (19 == sum(t, 2)) << endl;
    cout << (19 == sum(t, 1000000000)) << endl;
}
