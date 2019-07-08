#include <bits/stdc++.h>
#include "fft.h"
#include "modint.h"

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

// fast multiplication for modint in O(n*log(n))
template<int mod>
vector<modint<mod>> operator*(const vector<modint<mod>> &a, const vector<modint<mod>> &b) {
    if (a.empty() || b.empty()) {
        return {};
    }
    vector<int> a_int(a.size());
    for (int i = 0; i < a.size(); i++) {
        a_int[i] = static_cast<int>(a[i]);
    }
    vector<int> b_int(b.size());
    for (int i = 0; i < b.size(); i++) {
        b_int[i] = static_cast<int>(b[i]);
    }
    vector<int> c_int = multiply_mod(a_int, b_int, mod);
    vector<modint<mod>> c(c_int.size());
    for (int i = 0; i < c.size(); i++) {
        c[i] = c_int[i];
    }
    return c;
}

// fallback multiplication in O(n*m)
template<class T>
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

template<class T>
vector<T> &operator*=(vector<T> &a, const vector<T> &b) {
    a = a * b;
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

template<class T>
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
    return mult(0, (int) a.size() - 1);
}

template<class T>
T evaluate(const vector<T> &a, const T &x) {
    T res = 0;
    for (int i = (int) a.size() - 1; i >= 0; i--) {
        res = res * x + a[i];
    }
    return res;
}

template<class T>
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

template<class T>
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
