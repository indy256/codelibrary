#include <bits/stdc++.h>

using namespace std;

vector<int> stable_matching(vector<vector<int>> prefer_m, vector<vector<int>> prefer_w) {
    int n = prefer_m.size();
    vector<int> pair_m(n, -1);
    vector<int> pair_w(n, -1);
    vector<int> p(n);
    for (int i = 0; i < n; i++) {
        while (pair_m[i] < 0) {
            int w = prefer_m[i][p[i]++];
            int m = pair_w[w];
            if (m == -1) {
                pair_m[i] = w;
                pair_w[w] = i;
            } else if (prefer_w[w][i] < prefer_w[w][m]) {
                pair_m[m] = -1;
                pair_m[i] = w;
                pair_w[w] = i;
                i = m;
            }
        }
    }
    return pair_m;
}

// usage example
int main() {
    vector<vector<int>> prefer_m{{0, 1, 2}, {0, 2, 1}, {1, 0, 2}};

    vector<vector<int>> prefer_w{{0, 1, 2}, {2, 0, 1}, {2, 1, 0}};

    vector<int> matching = stable_matching(prefer_m, prefer_w);
    for (int x : matching)
        cout << x << " ";
    cout << endl;
}
