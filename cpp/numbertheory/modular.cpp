#include <bits/stdc++.h>

using namespace std;

template<int mod>
class Modular {
public:
    int value;

    Modular(int x) {
        value = normalize(x);
    }

    int normalize(int x) {
        if (x < -mod || x >= mod)
            x %= mod;
        if (x < 0) x += mod;
        return x;
    }

    int operator()() const { return value; }

    Modular &operator+=(const Modular &other) {
        if ((value += other.value) >= mod) value -= mod;
        return *this;
    }

    Modular &operator-=(const Modular &other) {
        if ((value -= other.value) < 0) value += mod;
        return *this;
    }

    Modular &operator+=(int other) { return *this += Modular(other); }

    Modular &operator-=(int other) { return *this -= Modular(other); }

    Modular operator-() const { return Modular(-value); }

    Modular &operator*=(const Modular &rhs) {
        value = normalize(static_cast<long long>(value) * rhs.value % mod);
        return *this;
    }

    Modular &operator/=(const Modular &other) { return *this *= Modular(inverse(other.value, mod)); }

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
};

template<int mod>
Modular<mod> operator+(const Modular<mod> &lhs, const Modular<mod> &rhs) { return Modular<mod>(lhs) += rhs; }

template<int mod>
Modular<mod> operator-(const Modular<mod> &lhs, const Modular<mod> &rhs) { return Modular<mod>(lhs) -= rhs; }

template<int mod>
Modular<mod> operator*(const Modular<mod> &lhs, const Modular<mod> &rhs) { return Modular<mod>(lhs) *= rhs; }

template<int mod>
Modular<mod> operator/(const Modular<mod> &lhs, const Modular<mod> &rhs) { return Modular<mod>(lhs) /= rhs; }

constexpr int mod = (int) 1e9 + 7;
using Mint = Modular<mod>;

// usage example
int main() {
    Mint a = 1;
    Mint b = 2;
    Mint c = 1000'000'000;
    Mint d = a / b * c / c;
    cout << d() << endl;
}
