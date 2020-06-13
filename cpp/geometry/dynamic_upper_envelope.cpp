#include <bits/stdc++.h>

using namespace std;

// Container where you can add lines of the form ax+b, and
// get_max maximum values at points x. For each line, also keeps a value p,
// which is the last (maximum) point for which the current line is dominant.
// (obviously, for the last line, p is infinity) Useful for dynamic programming.

using ll = long long;

struct Line {
    ll a, b;
    mutable ll p;

    bool operator<(const Line &o) const { return a < o.a; }

    bool operator<(ll x) const { return p < x; }
};

struct LineContainer : multiset<Line, less<>> {
    // (for doubles, use inf = 1/.0, div(a,b) = a/b)
    const ll inf = numeric_limits<ll>::max();

    ll div(ll a, ll b) {  // floored division
        return a / b - ((a ^ b) < 0 && a % b);
    }

    bool isect(iterator x, iterator y) {
        if (y == end()) {
            x->p = inf;
            return false;
        }
        if (x->a == y->a)
            x->p = x->b > y->b ? inf : -inf;
        else
            x->p = div(y->b - x->b, x->a - y->a);
        return x->p >= y->p;
    }

    void add_line(ll a, ll b) {
        auto z = insert({a, b, 0}), y = z++, x = y;
        while (isect(y, z))
            z = erase(z);
        if (x != begin() && isect(--x, y))
            isect(x, erase(y));
        while ((y = x) != begin() && (--x)->p >= y->p)
            isect(x, erase(y));
    }

    ll get_max(ll x) {
        assert(!empty());
        auto line = *lower_bound(x);
        return line.a * x + line.b;
    }
};

// usage example
int main() {
    LineContainer lc;
    lc.add_line(1, 3);
    lc.add_line(2, 1);
    cout << lc.get_max(1) << endl;
}
