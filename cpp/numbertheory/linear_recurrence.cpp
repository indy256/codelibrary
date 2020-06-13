#include <bits/stdc++.h>

#include "polynom.h"

using namespace std;

// returns f[n] = f[n-1]*a[k-1] + ... + f[n-k]*a[0], where f[0], ..., f[k-1] are provided
// O(k*log(k)*log(n)) complexity
template <class T>
T nth_element_of_recurrence(vector<T> a, const vector<T> &f, long long n) {
    if (n < f.size())
        return f[n];
    a = -a;
    a.emplace_back(1);
    vector<T> xn = power({0, 1}, n, a);
    return inner_product(f.begin(), f.begin() + min(f.size(), xn.size()), xn.begin(), T{0});
}

// https://en.wikipedia.org/wiki/Berlekamp%E2%80%93Massey_algorithm
template <typename M>
vector<M> berlekamp_massey(const vector<M> &a) {
    int n = a.size();
    vector<M> C(n), B(n);
    C[0] = B[0] = 1;
    M b = 1;
    int L = 0;
    for (int i = 0, m = 1; i < n; ++i) {
        M d = a[i];
        for (int j = 1; j <= L; ++j)
            d = d + C[j] * a[i - j];
        if (d == 0) {
            ++m;
            continue;
        }
        vector<M> T = C;
        M coef = d / b;
        for (int j = m; j < n; ++j)
            C[j] -= coef * B[j - m];
        if (2 * L > i) {
            ++m;
            continue;
        }
        L = i + 1 - L;
        B = T;
        b = d;
        m = 1;
    }
    C.resize(L + 1);
    C.erase(C.begin());
    reverse(C.begin(), C.end());
    return -C;
}

// usage example
constexpr int mod = (int)1e9 + 7;
using mint = modint<mod>;

int main() {
    {
        // Fibonacci numbers
        vector<mint> f{1, 1};
        vector<mint> a{1, 1};
        for (int i = 0; i < 10; ++i) {
            cout << (int)nth_element_of_recurrence(a, f, i) << endl;
        }
        cout << endl;
    }
    {
        vector<mint> f = berlekamp_massey(vector<mint>({1, 1, 3, 5, 11}));
        for (auto v : f)
            cout << (int)v << " ";
        cout << endl;
    }
}
