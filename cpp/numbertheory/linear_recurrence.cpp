#include <bits/stdc++.h>
#include "polynom.h"

using namespace std;

// returns f[n] = f[n-1]*a[k-1] + ... + f[n-k]*a[0], where f[0], ..., f[k-1] are provided
// O(k*log(k)*log(n)) complexity
template<class T>
T nth_element_of_recurrence(vector<T> a, const vector<T> &f, long long n) {
    if (n < f.size())
        return f[n];
    a = -a;
    a.emplace_back(1);
    vector<T> xn = power({0, 1}, n, a);
    return inner_product(f.begin(), f.begin() + min(f.size(), xn.size()), xn.begin(), T{0});
}

// https://en.wikipedia.org/wiki/Berlekamp%E2%80%93Massey_algorithm
template<typename T>
vector<T> berlekamp_massey(const vector<T> &a) {
    vector<T> f = {1};
    vector<T> p = {1};
    int L = 0;
    for (int i = 0; i < a.size(); i++) {
        T delta = 0;
        for (int j = 0; j <= L; j++) {
            delta += a[i - j] * f[j];
        }
        p.insert(p.begin(), 0);
        if (delta != 0) {
            vector<T> t = f;
            if (p.size() > t.size()) {
                t.resize(p.size());
            }
            for (int j = 0; j < p.size(); j++) {
                t[j] -= delta * p[j];
            }
            if (2 * L <= i) {
                p = f;
                T od = 1 / delta;
                for (T &x : p) {
                    x *= od;
                }
                L = i + 1 - L;
            }
            swap(f, t);
        }
    }
    reverse(f.begin(), f.end());
    assert(f.size() == L + 1);
    f.erase(f.begin() + L);
    return -f;
}

// usage example
constexpr int mod = (int) 1e9 + 7;
using mint = modint<mod>;

int main() {
    {
        // Fibonacci numbers
        vector<mint> f{1, 1};
        vector<mint> a{1, 1};
        for (int i = 0; i < 10; ++i) {
            cout << (int) nth_element_of_recurrence(a, f, i) << endl;
        }
        cout << endl;
    }
    {
        vector<mint> f = berlekamp_massey(vector<mint>({1, 1, 2, 3, 5}));
        for (auto v:f) cout << (int) v << " ";
        cout << endl;
    }
}
