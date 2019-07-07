#include <bits/stdc++.h>
#include "fft.h"

using namespace std;

template<class T>
vector<T> &operator+=(vector<T> &a, const vector<T> &b) {
    if (a.size() < b.size()) {
        a.resize(b.size());
    }
    for (int i = 0; i < b.size(); i++) {
        a[i] += b[i];
    }
    return a;
}

template<class T>
vector<T> operator+(vector<T> a, const vector<T> &b) {
    a += b;
    return a;
}

template<class T>
vector<T> &operator-=(vector<T> &a, const vector<T> &b) {
    if (a.size() < b.size()) {
        a.resize(b.size());
    }
    for (int i = 0; i < b.size(); i++) {
        a[i] -= b[i];
    }
    return a;
}

template<class T>
vector<T> operator-(vector<T> a, const vector<T> &b) {
    a -= b;
    return a;
}

template<class T>
vector<T> operator-(vector<T> a) {
    for (int i = 0; i < a.size(); i++) {
        a[i] = -a[i];
    }
    return a;
}

template<class T>
vector<T> inverse(const vector<T> &a) {
    assert(!a.empty());
    int n = a.size();
    vector<T> b = {1 / a[0]};
    while (b.size() < n) {
        vector<T> a_cut(a.begin(), a.begin() + min(a.size(), b.size() << 1));
        vector<T> x = b * b * a_cut;
        b.resize(b.size() << 1);
        for (int i = b.size() >> 1; i < min(x.size(), b.size()); i++) {
            b[i] = -x[i];
        }
    }
    b.resize(n);
    return b;
}

template<class T>
vector<T> &operator/=(vector<T> &a, vector<T> b) {
    int n = a.size();
    int m = b.size();
    if (n < m) {
        a.clear();
    } else {
        reverse(a.begin(), a.end());
        reverse(b.begin(), b.end());
        b.resize(n - m + 1);
        a *= inverse(b);
        a.erase(a.begin() + n - m + 1, a.end());
        reverse(a.begin(), a.end());
    }
    return a;
}

template<class T>
vector<T> operator/(vector<T> a, const vector<T> &b) {
    a /= b;
    return a;
}

template<class T>
vector<T> &operator%=(vector<T> &a, const vector<T> &b) {
    int n = a.size();
    int m = b.size();
    if (n >= m) {
        vector<T> c = (a / b) * b;
        a.resize(m - 1);
        for (int i = 0; i < m - 1; i++) {
            a[i] -= c[i];
        }
    }
    return a;
}

template<class T>
vector<T> operator%(vector<T> a, const vector<T> &b) {
    a %= b;
    return a;
}

template<class T>
vector<T> power(const vector<T> &a, long long b, const vector<T> &mod) {
    assert(b >= 0);
    vector<T> res = vector<T>{1} % mod;
    int highest_one_bit = b ? __builtin_clzll(1) - __builtin_clzll(b) : -1;
    for (int i = highest_one_bit; i >= 0; i--) {
        res = res * res % mod;
        if (b >> i & 1) {
            res = res * a % mod;
        }
    }
    return res;
}

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

template<class T>
vector<T> derivative(vector<T> a) {
    for (int i = 0; i < a.size(); i++) {
        a[i] *= i;
    }
    if (!a.empty()) {
        a.erase(a.begin());
    }
    return a;
}

template<class T>
vector<T> integrate(vector<T> a) {
    a.insert(a.begin(), 0);
    for (int i = 1; i < a.size(); i++) {
        a[i] /= i;
    }
    return a;
}

template<class T>
vector<T> logarithm(const vector<T> &a) {
    assert(!a.empty() && a[0] == 1);
    vector<T> res = integrate(derivative(a) * inverse(a));
    res.resize(a.size());
    return res;
}

// usage example
#include "../numbertheory/modint.h"

constexpr int mod = (int) 1e9 + 7;
using mint = modint<mod>;

int main() {
    // Fibonacci numbers
    vector<mint> f{1, 1};
    vector<mint> a{1, 1};
    for (int i = 0; i < 10; ++i) {
        cout << (int) nth_element_of_recurrence(a, f, i) << endl;
    }
}
