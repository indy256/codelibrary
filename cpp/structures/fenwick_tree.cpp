#include <bits/stdc++.h>

using namespace std;

// https://e-maxx-eng.appspot.com/data_structures/fenwick.html

template<class T>
struct fenwick {
    vector<T> t;

    fenwick(int n) : t(n) {}

    void add(int i, T value) {
        for (; i < t.size(); i |= i + 1)
            t[i] += value;
    }

    // sum[0..i]
    int sum(int i) {
        T res{};
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res += t[i];
        return res;
    }

    // returns min(p | sum[0..p] >= sum)
    int lower_bound(T sum) {
        int pos = 0;
        for (int blockSize = 1 << (31 - __builtin_clz(t.size())); blockSize != 0; blockSize >>= 1) {
            int p = pos + blockSize - 1;
            if (p < t.size() && t[p] < sum) {
                sum -= t[p];
                pos += blockSize;
            }
        }
        return pos;
    }
};


// usage example
int main() {
    fenwick<int> f(3);
    f.add(0, 4);
    f.add(1, 5);
    f.add(2, 5);
    f.add(2, 5);

    cout << boolalpha;
    cout << (4 == f.sum(0)) << endl;
    cout << (19 == f.sum(2)) << endl;
    cout << (2 == f.lower_bound(19)) << endl;
    cout << (3 == f.lower_bound(20)) << endl;
}
