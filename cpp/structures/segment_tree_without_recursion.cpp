#include <bits/stdc++.h>

using namespace std;

int get(const vector<int> &t, int i) {
    return t[i + t.size() / 2];
}

void add(vector<int> &t, int i, int value) {
    i += t.size() / 2;
    t[i] += value;
    for (; i > 1; i >>= 1)
        t[i >> 1] = min(t[i], t[i ^ 1]);
}

int min(const vector<int> &t, int a, int b) {
    int res = numeric_limits<int>::max();
    for (a += t.size() / 2, b += t.size() / 2; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
        if ((a & 1) != 0)
            res = min(res, t[a]);
        if ((b & 1) == 0)
            res = min(res, t[b]);
    }
    return res;
}

// usage example
int main() {
    int n = 10;
    vector<int> t(n + n);
    add(t, 0, -1);
    add(t, 9, -2);
    cout << (-2 == min(t, 0, 9)) << endl;
}
