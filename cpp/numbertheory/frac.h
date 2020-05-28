#include <bits/stdc++.h>

using namespace std;

template<class T>
struct frac {
    T a, b;

    frac(T a, T b = 1) : a{a}, b{b} {
        normalize();
    }

    void normalize() {
        if (b < 0) {
            a = -a;
            b = -b;
        }
        T g = gcd(::abs(a), b);
        if (g != 0) {
            a /= g;
            b /= g;
        }
        this->a = a;
        this->b = b;
    }

    bool operator<(frac lhs) const { return a * lhs.b < b * lhs.a; }

    bool operator>(frac lhs) const { return a * lhs.b > b * lhs.a; }

    frac operator-() const { return frac(-a, b); }

    frac abs() const { return frac(::abs(a), b); }

    frac &operator+=(frac rhs) {
        a = a * rhs.b + b * rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    frac &operator-=(frac rhs) {
        a = a * rhs.b - b * rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    frac &operator*=(frac rhs) {
        a *= rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    frac &operator/=(frac rhs) {
        a *= rhs.b;
        b *= rhs.a;
        normalize();
        return *this;
    }

    friend frac operator+(frac lhs, frac rhs) { return lhs += rhs; }

    friend frac operator-(frac lhs, frac rhs) { return lhs -= rhs; }

    friend frac operator*(frac lhs, frac rhs) { return lhs *= rhs; }

    friend frac operator/(frac lhs, frac rhs) { return lhs /= rhs; }

    friend bool operator==(frac lhs, frac rhs) { return lhs.a == rhs.a && lhs.b == rhs.b; }

    friend bool operator!=(frac lhs, frac rhs) { return !(lhs == rhs); }
};
