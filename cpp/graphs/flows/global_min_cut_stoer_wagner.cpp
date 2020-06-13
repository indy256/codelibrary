#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Stoer%E2%80%93Wagner_algorithm in O(V^3)

pair<int, vector<int>> min_cut(vector<vector<int>> &cap) {
    int best_cap = numeric_limits<int>::max();
    vector<int> best_cut;
    int n = cap.size();
    vector<vector<int>> v(n);
    for (int i = 0; i < n; ++i)
        v[i].push_back(i);
    vector<int> w(n);
    vector<bool> exist(n, true);
    vector<bool> in_a(n);
    for (int ph = 0; ph < n - 1; ++ph) {
        fill(in_a.begin(), in_a.end(), false);
        fill(w.begin(), w.end(), 0);
        for (int it = 0, prev; it < n - ph; ++it) {
            int sel = -1;
            for (int i = 0; i < n; ++i)
                if (exist[i] && !in_a[i] && (sel == -1 || w[i] > w[sel])) {
                    sel = i;
                }
            if (it == n - ph - 1) {
                if (w[sel] < best_cap) {
                    best_cap = w[sel];
                    best_cut = v[sel];
                }
                v[prev].insert(v[prev].end(), v[sel].begin(), v[sel].end());
                for (int i = 0; i < n; ++i) {
                    cap[i][prev] += cap[sel][i];
                    cap[prev][i] += cap[sel][i];
                }
                exist[sel] = false;
            } else {
                in_a[sel] = true;
                for (int i = 0; i < n; ++i)
                    w[i] += cap[sel][i];
                prev = sel;
            }
        }
    }
    return {best_cap, best_cut};
}

// usage example
int main() {
    vector<vector<int>> capacity{{0, 1, 1, 0}, {1, 0, 1, 1}, {1, 1, 0, 1}, {0, 1, 1, 0}};
    auto [cap, cut] = min_cut(capacity);
    cout << cap << endl;
    for (int v : cut)
        cout << v << " ";
    cout << endl;
}
