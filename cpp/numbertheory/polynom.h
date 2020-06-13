#include <bits/stdc++.h>

#include "../numeric/fft.h"
#include "modint.h"

using namespace std;

// https://cp-algorithms.com/algebra/polynomial.html

template <class T>
vector<T> &operator+=(vector<T> &a, const vector<T> &b) {
    if (a.size() < b.size()) {
        a.resize(b.size());
    }
    for (size_t i = 0; i < b.size(); i++) {
        a[i] += b[i];
    }
    return a;
}

template <class T>
vector<T> operator+(vector<T> a, const vector<T> &b) {
    a += b;
    return a;
}

template <class T>
vector<T> &operator-=(vector<T> &a, const vector<T> &b) {
    if (a.size() < b.size()) {
        a.resize(b.size());
    }
    for (int i = 0; i < b.size(); i++) {
        a[i] -= b[i];
    }
    return a;
}

template <class T>
vector<T> operator-(vector<T> a, const vector<T> &b) {
    a -= b;
    return a;
}

template <class T>
vector<T> operator-(vector<T> a) {
    for (int i = 0; i < a.size(); i++) {
        a[i] = -a[i];
    }
    return a;
}

// fast multiplication for modint in O(n*log(n))
template <int mod>
vector<modint<mod>> operator*(const vector<modint<mod>> &a, const vector<modint<mod>> &b) {
    if (a.empty() || b.empty()) {
        return {};
    }
    if (min(a.size(), b.size()) < 150) {
        vector<modint<mod>> c(a.size() + b.size() - 1, 0);
        for (size_t i = 0; i < a.size(); i++) {
            for (size_t j = 0; j < b.size(); j++) {
                c[i + j] += a[i] * b[j];
            }
        }
        return c;
    }
    vector<int> a_int(a.size());
    for (size_t i = 0; i < a.size(); i++) {
        a_int[i] = static_cast<int>(a[i]);
    }
    vector<int> b_int(b.size());
    for (size_t i = 0; i < b.size(); i++) {
        b_int[i] = static_cast<int>(b[i]);
    }
    vector<int> c_int = multiply_mod(a_int, b_int, mod);
    vector<modint<mod>> c(c_int.size());
    for (size_t i = 0; i < c.size(); i++) {
        c[i] = c_int[i];
    }
    return c;
}

// fallback multiplication in O(n*m)
template <class T>
vector<T> operator*(const vector<T> &a, const vector<T> &b) {
    if (a.empty() || b.empty()) {
        return {};
    }
    vector<T> c(a.size() + b.size() - 1, 0);
    for (int i = 0; i < a.size(); i++) {
        for (int j = 0; j < b.size(); j++) {
            c[i + j] += a[i] * b[j];
        }
    }
    return c;
}

template <class T>
vector<T> &operator*=(vector<T> &a, const vector<T> &b) {
    a = a * b;
    return a;
}

template <class T>
vector<T> inverse(const vector<T> &a) {
    assert(!a.empty());
    int n = a.size();
    vector<T> b = {1 / a[0]};
    while (b.size() < n) {
        vector<T> a_cut(a.begin(), a.begin() + min(a.size(), b.size() << 1));
        vector<T> x = b * b * a_cut;
        b.resize(b.size() << 1);
        for (size_t i = b.size() >> 1; i < min(x.size(), b.size()); i++) {
            b[i] = -x[i];
        }
    }
    b.resize(n);
    return b;
}

template <class T>
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

template <class T>
vector<T> operator/(vector<T> a, const vector<T> &b) {
    a /= b;
    return a;
}

template <class T>
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

template <class T>
vector<T> operator%(vector<T> a, const vector<T> &b) {
    a %= b;
    return a;
}

template <class T>
vector<T> power(const vector<T> &a, long long b, const vector<T> &mod) {
    assert(b >= 0);
    vector<T> res = vector<T>{1} % mod;
    int highest_one_bit = -1;
    while (1LL << (highest_one_bit + 1) <= b)
        ++highest_one_bit;
    for (int i = highest_one_bit; i >= 0; i--) {
        res = res * res % mod;
        if (b >> i & 1) {
            res = res * a % mod;
        }
    }
    return res;
}

template <class T>
vector<T> derivative(vector<T> a) {
    for (size_t i = 0; i < a.size(); i++) {
        a[i] *= i;
    }
    if (!a.empty()) {
        a.erase(a.begin());
    }
    return a;
}

template <class T>
vector<T> integrate(vector<T> a) {
    a.insert(a.begin(), 0);
    for (int i = 1; i < a.size(); i++) {
        a[i] /= i;
    }
    return a;
}

template <class T>
vector<T> logarithm(const vector<T> &a) {
    assert(!a.empty() && a[0] == 1);
    vector<T> res = integrate(derivative(a) * inverse(a));
    res.resize(a.size());
    return res;
}

template <class T>
vector<T> exponent(const vector<T> &a) {
    assert(!a.empty() && a[0] == 0);
    int n = a.size();
    vector<T> b = {1};
    while (b.size() < n) {
        vector<T> x(a.begin(), a.begin() + min(a.size(), b.size() << 1));
        x[0] += 1;
        vector<T> old_b = b;
        b.resize(b.size() << 1);
        x -= logarithm(b);
        x *= old_b;
        for (int i = b.size() >> 1; i < min(x.size(), b.size()); i++) {
            b[i] = x[i];
        }
    }
    b.resize(n);
    return b;
}

template <class T>
vector<T> sqrt(const vector<T> &a) {
    assert(!a.empty() && a[0] == 1);
    int n = a.size();
    vector<T> b = {1};
    while (b.size() < n) {
        vector<T> x(a.begin(), a.begin() + min(a.size(), b.size() << 1));
        b.resize(b.size() << 1);
        x *= inverse(b);
        T inv2 = 1 / static_cast<T>(2);
        for (int i = b.size() >> 1; i < min(x.size(), b.size()); i++) {
            b[i] = x[i] * inv2;
        }
    }
    b.resize(n);
    return b;
}

template <class T>
vector<T> multiply(const vector<vector<T>> &a) {
    if (a.empty()) {
        return {0};
    }
    function<vector<T>(int, int)> mult = [&](int l, int r) {
        if (l == r) {
            return a[l];
        }
        int m = (l + r) >> 1;
        return mult(l, m) * mult(m + 1, r);
    };
    return mult(0, (int)a.size() - 1);
}

template <class T>
T evaluate(const vector<T> &a, const T &x) {
    T res = 0;
    for (int i = (int)a.size() - 1; i >= 0; i--) {
        res = res * x + a[i];
    }
    return res;
}

template <class T>
vector<T> evaluate(const vector<T> &a, const vector<T> &x) {
    if (x.empty()) {
        return {};
    }
    if (a.empty()) {
        return vector<T>(x.size());
    }
    int n = x.size();
    vector<vector<T>> t(2 * n - 1);
    function<void(int, int, int)> build = [&](int v, int l, int r) {
        if (l == r) {
            t[v] = vector<T>{-x[l], 1};
        } else {
            int m = (l + r) >> 1;
            int y = v + ((m - l + 1) << 1);
            build(v + 1, l, m);
            build(y, m + 1, r);
            t[v] = t[v + 1] * t[y];
        }
    };
    build(0, 0, n - 1);
    vector<T> res(n);
    function<void(int, int, int, vector<T>)> eval = [&](int v, int l, int r, vector<T> p) {
        p %= t[v];
        if (p.size() < 150) {
            for (int i = l; i <= r; i++) {
                res[i] = evaluate(p, x[i]);
            }
            return;
        }
        if (l == r) {
            res[l] = p[0];
        } else {
            int m = (l + r) >> 1;
            int z = v + ((m - l + 1) << 1);
            eval(v + 1, l, m, p);
            eval(z, m + 1, r, p);
        }
    };
    eval(0, 0, n - 1, a);
    return res;
}

template <class T>
vector<T> interpolate(const vector<T> &x, const vector<T> &y) {
    if (x.empty()) {
        return {};
    }
    assert(x.size() == y.size());
    int n = x.size();
    vector<vector<T>> t(2 * n - 1);
    function<void(int, int, int)> build = [&](int v, int l, int r) {
        if (l == r) {
            t[v] = vector<T>{-x[l], 1};
        } else {
            int m = (l + r) >> 1;
            int z = v + ((m - l + 1) << 1);
            build(v + 1, l, m);
            build(z, m + 1, r);
            t[v] = t[v + 1] * t[z];
        }
    };
    build(0, 0, n - 1);
    vector<T> val(n);
    function<void(int, int, int, vector<T>)> eval = [&](int v, int l, int r, vector<T> p) {
        p %= t[v];
        if (p.size() < 150) {
            for (int i = l; i <= r; i++) {
                val[i] = evaluate(p, x[i]);
            }
            return;
        }
        if (l == r) {
            val[l] = p[0];
        } else {
            int m = (l + r) >> 1;
            int z = v + ((m - l + 1) << 1);
            eval(v + 1, l, m, p);
            eval(z, m + 1, r, p);
        }
    };
    vector<T> d = derivative(t[0]);
    eval(0, 0, n - 1, d);
    for (int i = 0; i < n; i++) {
        val[i] = y[i] / val[i];
    }
    function<vector<T>(int, int, int)> calc = [&](int v, int l, int r) {
        if (l == r) {
            return vector<T>{val[l]};
        }
        int m = (l + r) >> 1;
        int z = v + ((m - l + 1) << 1);
        return calc(v + 1, l, m) * t[z] + calc(z, m + 1, r) * t[v + 1];
    };
    return calc(0, 0, n - 1);
}

// f[i] = 1^n + 2^n + ... + up^n
// O(n*log(n)) complexity
template <class T>
vector<T> faulhaber(const T &up, int n) {
    vector<T> ex(n + 1);
    T e = 1;
    for (int i = 0; i <= n; i++) {
        ex[i] = e;
        e /= i + 1;
    }
    vector<T> den = ex;
    den.erase(den.begin());
    for (auto &d : den) {
        d = -d;
    }
    vector<T> num(n);
    T p = 1;
    for (int i = 0; i < n; i++) {
        p *= up + 1;
        num[i] = ex[i + 1] * (1 - p);
    }
    vector<T> res = num * inverse(den);
    res.resize(n);
    T f = 1;
    for (int i = 0; i < n; i++) {
        res[i] *= f;
        f *= i + 1;
    }
    return res;
}

// (x + 1) * (x + 2) * ... * (x + n)
// (can be optimized with precomputed inverses)
template <class T>
vector<T> sequence(int n) {
    if (n == 0) {
        return {1};
    }
    if (n % 2 == 1) {
        return sequence<T>(n - 1) * vector<T>{n, 1};
    }
    vector<T> c = sequence<T>(n / 2);
    vector<T> a = c;
    reverse(a.begin(), a.end());
    T f = 1;
    for (int i = n / 2 - 1; i >= 0; i--) {
        f *= n / 2 - i;
        a[i] *= f;
    }
    vector<T> b(n / 2 + 1);
    b[0] = 1;
    for (int i = 1; i <= n / 2; i++) {
        b[i] = b[i - 1] * (n / 2) / i;
    }
    vector<T> h = a * b;
    h.resize(n / 2 + 1);
    reverse(h.begin(), h.end());
    f = 1;
    for (int i = 1; i <= n / 2; i++) {
        f /= i;
        h[i] *= f;
    }
    vector<T> res = c * h;
    return res;
}

template <class T>
T factorial(long long n) {
    if (n == 0)
        return 1;
    int m = min((long long)(sqrt(n) * 2), n);
    vector<T> a = sequence<T>(m);
    vector<T> x(n / m);
    for (size_t i = 0; i < x.size(); ++i) {
        x[i] = i;
        x[i] *= m;
    }
    vector<T> b = evaluate(a, x);
    T res = 1;
    for (auto v : b)
        res *= v;
    for (long long i = n / m * m + 1; i <= n; ++i) {
        res *= i;
    }
    return res;
}

template <class T>
T binomial(int n, int m) {
    return factorial<T>(n) / factorial<T>(m) / factorial<T>(n - m);
}

template <class T>
class OnlineProduct {
   public:
    const vector<T> a;
    vector<T> b;
    vector<T> c;

    OnlineProduct(const vector<T> &a_) : a(a_) {}

    T add(const T &val) {
        int i = (int)b.size();
        b.push_back(val);
        if ((int)c.size() <= i) {
            c.resize(i + 1);
        }
        c[i] += a[0] * b[i];
        int z = 1;
        while ((i & (z - 1)) == z - 1 && (int)a.size() > z) {
            vector<T> a_mul(a.begin() + z, a.begin() + min(z << 1, (int)a.size()));
            vector<T> b_mul(b.end() - z, b.end());
            vector<T> c_mul = a_mul * b_mul;
            if ((int)c.size() <= i + (int)c_mul.size()) {
                c.resize(i + c_mul.size() + 1);
            }
            for (int j = 0; j < (int)c_mul.size(); j++) {
                c[i + 1 + j] += c_mul[j];
            }
            z <<= 1;
        }
        return c[i];
    }
};
