#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Hungarian_algorithm in O(n^2 * m)
pair<int, vector<int>> min_weight_perfect_matching(const vector<vector<int>> &a) {
    int n = a.size();
    int m = a[0].size();
    vector<int> u(n);
    vector<int> v(m);
    vector<int> p(m);
    vector<int> way(m);
    for (int i = 1; i < n; ++i) {
        vector<int> minv(m, numeric_limits<int>::max());
        vector<bool> used(m);
        p[0] = i;
        int j0 = 0;
        while (p[j0] != 0) {
            used[j0] = true;
            int i0 = p[j0];
            int delta = numeric_limits<int>::max();
            int j1 = 0;
            for (int j = 1; j < m; ++j)
                if (!used[j]) {
                    int d = a[i0][j] - u[i0] - v[j];
                    if (minv[j] > d) {
                        minv[j] = d;
                        way[j] = j0;
                    }
                    if (delta > minv[j]) {
                        delta = minv[j];
                        j1 = j;
                    }
                }
            for (int j = 0; j < m; ++j)
                if (used[j]) {
                    u[p[j]] += delta;
                    v[j] -= delta;
                } else
                    minv[j] -= delta;
            j0 = j1;
        }
        while (j0 != 0) {
            int j1 = way[j0];
            p[j0] = p[j1];
            j0 = j1;
        }
    }
    vector<int> matching(n);
    for (int i = 1; i < m; ++i)
        matching[p[i]] = i;
    return {-v[0], matching};
}

// usage example
int main() {
    // row1 and col1 should contain 0
    vector<vector<int>> a{{0, 0, 0}, {0, 1, 2}, {0, 1, 2}};
    auto [cost, matching] = min_weight_perfect_matching(a);
    cout << boolalpha << (cost == 3) << endl;
}
