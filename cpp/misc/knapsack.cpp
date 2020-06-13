#include <bits/stdc++.h>

using namespace std;

vector<bool> knapsack(const vector<int> &w, int W) {
    vector<int> cnt(W + 1);
    for (int x : w) {
        if (x <= W)
            ++cnt[x];
    }
    vector<bool> can(W + 1);
    can[0] = true;
    for (int v = 1; v <= W; ++v) {
        int am = cnt[v];
        if (am == 0)
            continue;
        for (int from = 0; from < v; ++from) {
            int counter = -1;
            for (int got = from; got <= W; got += v) {
                --counter;
                if (can[got])
                    counter = am;
                else if (counter >= 0) {
                    can[got] = true;
                }
            }
        }
    }
    return can;
}

// usage example
int main() {
    auto b = knapsack({2, 3, 6, 7}, 10);
    for (auto x : b)
        cout << x;
    cout << endl;
}
