#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/fenwick.html

template <class T>
struct fenwick {
    vector<T> t;

    fenwick(int n) : t(n) {}

    void add(int i, T value) {
        for (; i < t.size(); i |= i + 1)
            t[i] += value;
    }

    // sum[0..i]
    T sum(int i) {
        T res{};
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res += t[i];
        return res;
    }

    // returns min(p | sum[0..p] >= sum)
    // requires non-negative tree values
    int lower_bound(T sum) {
        int highest_one_bit = 1;
        while (highest_one_bit << 1 <= t.size())
            highest_one_bit <<= 1;
        int pos = 0;
        for (size_t blockSize = highest_one_bit; blockSize != 0; blockSize >>= 1) {
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
