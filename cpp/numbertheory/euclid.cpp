#include <bits/stdc++.h>

#include <optional>

using namespace std;

// precondition: mod > 1 && gcd(a, mod) = 1
int mod_inverse(int a, int mod) {
    int u = 0, v = 1, m = mod;
    while (a != 0) {
        int t = m / a;
        m -= t * a;
        swap(a, m);
        u -= t * v;
        swap(u, v);
    }
    assert(m == 1);
    return u < 0 ? u + mod : u;
}

// returns { gcd(a,b), x, y } such that gcd(a,b) = a*x + b*y
template <class T>
tuple<T, T, T> euclid(T a, T b) {
    T x = 1, y = 0, x1 = 0, y1 = 1;
    // invariant: a=a_orig*x+b_orig*y, b=a_orig*x1+b_orig*y1
    while (b != 0) {
        T q = a / b;
        T _x1 = x1;
        T _y1 = y1;
        T _b = b;
        x1 = x - q * x1;
        y1 = y - q * y1;
        b = a - q * b;
        x = _x1;
        y = _y1;
        a = _b;
    }
    return a > 0 ? tuple{a, x, y} : tuple{-a, -x, -y};
}

// https://cp-algorithms.com/algebra/linear_congruence_equation.html
// solves a * x = b (mod m)
int solve1(int a, int b, int m) {
    int g = gcd(a, m);
    if (b % g)
        return -1;
    a /= g;
    b /= g;
    m /= g;
    int x = (long long)b * mod_inverse(a, m) % m;
    return x;
    // all solutions: x[i]=x+i*m, i=0..g-1
}

// https://cp-algorithms.com/algebra/linear-diophantine-equation.html
// solves a * x + b * y = c (mod m)
optional<tuple<int, int>> solve2(int a, int b, int c) {
    auto [g, x0, y0] = euclid(abs(a), abs(b));
    if (c % g != 0)
        return {};
    x0 *= c / g;
    y0 *= c / g;
    return make_optional(tuple{a > 0 ? x0 : -x0, b > 0 ? y0 : -y0});
    // all solutions: x=x0+k*b/g, y=y0-k*a/g, k∈Z
}

// usage example
int main() {
    auto [gcd, x, y] = euclid(6, 9);
    cout << gcd << " = 6 * " << x << " + 9 * " << y << endl;

    cout << "x=" << solve1(2, 3, 5) << endl;

    auto res = solve2(4, 6, 2);
    if (res) {
        auto [xx, yy] = *res;
        cout << "x=" << xx << " y=" << yy << endl;
    }

    cout << mod_inverse(3, 10) << endl;
}
