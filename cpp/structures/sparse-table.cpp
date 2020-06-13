#include <bits/stdc++.h>

using namespace std;
#ifdef _MSC_VER
int __builtin_clz(unsigned x) {
    int bit = 31;
    while (bit >= 0 && (x & (1 << bit)) == 0)
        --bit;
    return 31 - bit;
}
#endif

template <class T, class F = function<T(const T &, const T &)>>
struct SparseTable {
    vector<vector<T>> t;
    F func;

    SparseTable(const vector<T> &a, F f) : t(32 - __builtin_clz(a.size())), func(std::move(f)) {
        t[0] = a;
        for (size_t i = 1; i < t.size(); i++) {
            t[i].resize(a.size() - (1 << i) + 1);
            for (size_t j = 0; j < t[i].size(); j++)
                t[i][j] = func(t[i - 1][j], t[i - 1][j + (1 << (i - 1))]);
        }
    }

    T get(int from, int to) const {
        assert(0 <= from && from <= to && to <= (int)t[0].size() - 1);
        int k = 31 - __builtin_clz(to - from + 1);
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
