#include <bits/stdc++.h>
using namespace std;

int knapsack(const vector<int> &weights, int W) {
    vector<bool> dp(W + 1);
    dp[0] = true;
    for (int w: weights) {
        for (int j = W; j >= w; --j) {
            dp[j] = dp[j] | dp[j - w];
        }
    }
    int res = 0;
    for (int i = 0; i <= W; ++i) {
        if (dp[i]) res = i;
    }
    return res;
}

int main() {
    cout << knapsack({3, 4, 5, 15, 20}, 30) << endl;
}
