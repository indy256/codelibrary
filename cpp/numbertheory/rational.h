#include <bits/stdc++.h>

using namespace std;

template <class T>
struct rational {
    T a, b;

    rational(T a, T b = 1) : a{a}, b{b} { normalize(); }

    void normalize() {
        if (b < 0) {
            a = -a;
            b = -b;
        }
        auto g = gcd(::abs(a), b);
        if (g != 0) {
            a /= g;
            b /= g;
        }
    }

    bool operator==(rational rhs) const { return a == rhs.a && b == rhs.b; }

    bool operator!=(rational rhs) const { return !(*this == rhs); }

    bool operator<(rational rhs) const { return a * rhs.b < b * rhs.a; }

    bool operator<=(rational rhs) const { return !(rhs < *this); }

    bool operator>(rational rhs) const { return rhs < *this; }

    bool operator>=(rational rhs) const { return !(*this < rhs); }

    rational operator-() const { return rational(-a, b); }

    rational abs() const { return rational(::abs(a), b); }

    rational &operator+=(rational rhs) {
        a = a * rhs.b + b * rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    rational &operator-=(rational rhs) {
        a = a * rhs.b - b * rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    rational &operator*=(rational rhs) {
        a *= rhs.a;
        b *= rhs.b;
        normalize();
        return *this;
    }

    rational &operator/=(rational rhs) {
        a *= rhs.b;
        b *= rhs.a;
        normalize();
        return *this;
    }

    friend rational operator+(rational lhs, rational rhs) { return lhs += rhs; }

    friend rational operator-(rational lhs, rational rhs) { return lhs -= rhs; }

    friend rational operator*(rational lhs, rational rhs) { return lhs *= rhs; }

    friend rational operator/(rational lhs, rational rhs) { return lhs /= rhs; }
};
