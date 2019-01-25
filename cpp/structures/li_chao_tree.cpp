#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/geometry/convex_hull_trick.html

using ftype = int;
using point = complex<ftype>;

ftype dot(point a, point b) {
    return (conj(a) * b).real();
}

ftype f(point a, ftype x) {
    return dot(a, {x, 1});
}

const int maxn = 2e5;

point lines[4 * maxn];

void add_line(point line, int node = 1, int l = 0, int r = maxn) {
    int m = (l + r) / 2;
    bool left = f(line, l) < f(lines[node], l);
    bool mid = f(line, m) < f(lines[node], m);
    if (mid) {
        swap(lines[node], line);
    }
    if (r - l == 1) {
        return;
    } else if (left != mid) {
        add_line(line, 2 * node, l, m);
    } else {
        add_line(line, 2 * node + 1, m, r);
    }
}

int get_min(int x, int node = 1, int l = 0, int r = maxn) {
    int m = (l + r) / 2;
    if (r - l == 1) {
        return f(lines[node], x);
    } else if (x < m) {
        return min(f(lines[node], x), get_min(x, 2 * node, l, m));
    } else {
        return min(f(lines[node], x), get_min(x, 2 * node + 1, m, r));
    }
}

// usage example
int main() {
    fill(lines, lines + 4 * maxn, point(0, numeric_limits<int>::max()));
    add_line({1, 3});
    add_line({2, 1});
    cout << get_min(1) << endl;
}
