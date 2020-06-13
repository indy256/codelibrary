#include <bits/stdc++.h>

using namespace std;

using ll = long long;
#define PI acos(-1)

// pay attention to case ax==0 && ay==0 or bx==0 && by == 0
double angle_between(ll ax, ll ay, ll bx, ll by) {
    double a = atan2(ax * by - ay * bx, ax * bx + ay * by);
    return a < 0 ? a + 2 * PI : a;
}

// pay attention to case ax==0 && ay==0 or bx==0 && by == 0
double angle_between2(ll ax, ll ay, ll bx, ll by) {
    double a = atan2(by, bx) - atan2(ay, ax);
    return a < 0 ? a + 2 * PI : a;
}

ll double_signed_area(const vector<int> &x, const vector<int> &y) {
    int n = x.size();
    ll area = 0;
    for (int i = 0, j = n - 1; i < n; j = i++) {
        area += (ll)(x[i] - x[j]) * (y[i] + y[j]);  // area += (long) x[i] * y[j] - (long) x[j] * y[i];
    }
    return area;
}

// Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise orientation
int orientation(ll ax, ll ay, ll bx, ll by, ll cx, ll cy) {
    bx -= ax;
    by -= ay;
    cx -= ax;
    cy -= ay;
    ll det = bx * cy - by * cx;
    return (det > 0) - (det < 0);
}

bool is_middle(ll a, ll m, ll b) {
    return min(a, b) <= m && m <= max(a, b);
}

bool is_middle(ll ax, ll ay, ll mx, ll my, ll bx, ll by) {
    return orientation(ax, ay, mx, my, bx, by) == 0 && is_middle(ax, mx, bx) && is_middle(ay, my, by);
}

struct Point {
    int x, y;

    bool operator<(const Point &o) const {
        bool up1 = y > 0 || (y == 0 && x >= 0);
        bool up2 = o.y > 0 || (o.y == 0 && o.x >= 0);
        if (up1 != up2)
            return up1;
        ll cmp = (ll)o.x * y - (ll)o.y * x;
        if (cmp != 0)
            return cmp < 0;
        return (ll)x * x + (ll)y * y < (ll)o.x * o.x + (ll)o.y * o.y;
        // return atan2(y, x) < atan2(o.y, o.x);
    }
};

pair<double, double> rotate_ccw(pair<double, double> p, double angle) {
    return {p.first * cos(angle) - p.second * sin(angle), p.first * sin(angle) + p.second * cos(angle)};
}

struct Line {
    ll a, b, c;
};

Line perpendicular(Line line, ll x, ll y) {
    return {-line.b, line.a, line.b * x - line.a * y};
}

// usage example
int main() {}
