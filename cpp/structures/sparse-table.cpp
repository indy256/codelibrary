#pragma GCC target("lzcnt")

#include <immintrin.h>
#include <bits/stdc++.h>

#include <utility>

using namespace std;

template<class T, class F = function<T(const T &, const T &)>>
struct SparseTable {
    int n;
    vector<vector<T>> t;
    F func;

    SparseTable(const vector<T> &a, F f) : n(a.size()), t(32 - _lzcnt_u32(n)), func(std::move(f)) {
        t[0] = a;
        for (size_t i = 1; i < t.size(); i++) {
            t[i].resize(n - (1 << i) + 1);
            for (size_t j = 0; j < t[i].size(); j++)
                t[i][j] = func(t[i - 1][j], t[i - 1][j + (1 << (i - 1))]);
        }
    }

    T get(int from, int to) const {
        assert(0 <= from && from <= to && to <= n - 1);
        int k = 31 - _lzcnt_u32(to - from + 1);
        return func(t[k][from], t[k][to - (1 << k) + 1]);
    }
};

// usage example
int main() {
    vector<int> a{3, 2, 1};
    SparseTable<int> st(a, [](int i, int j) { return min(i, j); });
    cout << st.get(0, 2) << endl;
    cout << st.get(0, 1) << endl;
}
