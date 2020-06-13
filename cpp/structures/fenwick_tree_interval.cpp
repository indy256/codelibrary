#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/fenwick.html

template <class T>
class fenwick_interval {
    vector<T> t1, t2;

    void add(vector<T> &t, int i, T value) {
        for (; i < t.size(); i |= i + 1)
            t[i] += value;
    }

    T sum(vector<T> &t, int i) {
        T res = 0;
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res += t[i];
        return res;
    }

   public:
    fenwick_interval(int n) : t1(n), t2(n) {}

    void add(int a, int b, T value) {
        add(t1, a, value);
        add(t1, b, -value);
        add(t2, a, -value * (a - 1));
        add(t2, b, value * b);
    }

    // sum[0..i]
    T sum(int i) { return sum(t1, i) * i + sum(t2, i); }
};

// usage example
int main() {
    fenwick_interval<int> f(3);
    f.add(0, 0, 4);
    f.add(1, 1, 5);
    f.add(2, 2, 5);
    f.add(2, 2, 5);

    cout << boolalpha;
    cout << (4 == f.sum(0)) << endl;
    cout << (19 == f.sum(2)) << endl;
}
