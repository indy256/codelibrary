#include <bits/stdc++.h>

using namespace std;

template <int mod>
struct modint {
    int value;

    modint(long long x = 0) { value = normalize(x); }

    int normalize(long long x) {
        if (x < -mod || x >= mod)
            x %= mod;
        if (x < 0)
            x += mod;
        return static_cast<int>(x);
    }

    explicit operator int() const { return value; }

    modint operator-() const { return modint(-value); }

    modint &operator+=(modint rhs) {
        if ((value += rhs.value) >= mod)
            value -= mod;
        return *this;
    }

    modint &operator-=(modint rhs) {
        if ((value -= rhs.value) < 0)
            value += mod;
        return *this;
    }

    modint &operator*=(modint rhs) {
        value = normalize(static_cast<long long>(value) * rhs.value);
        return *this;
    }

    modint &operator/=(modint rhs) { return *this *= modint(inverse(rhs.value, mod)); }

    int inverse(int a, int m) {
        int u = 0, v = 1;
        while (a != 0) {
            int t = m / a;
            m -= t * a;
            swap(a, m);
            u -= t * v;
            swap(u, v);
        }
        assert(m == 1);
        return u;
    }

    bool operator==(modint rhs) const { return value == rhs.value; }

    bool operator!=(modint rhs) const { return !(*this == rhs); }

    friend modint operator+(modint lhs, modint rhs) { return lhs += rhs; }

    friend modint operator-(modint lhs, modint rhs) { return lhs -= rhs; }

    friend modint operator*(modint lhs, modint rhs) { return lhs *= rhs; }

    friend modint operator/(modint lhs, modint rhs) { return lhs /= rhs; }
};
