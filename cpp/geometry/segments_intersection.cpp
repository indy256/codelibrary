#include <bits/stdc++.h>

#include <optional>

using namespace std;

using ll = long long;

bool is_cross_intersect(ll x1, ll y1, ll x2, ll y2, ll x3, ll y3, ll x4, ll y4) {
    ll z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
    ll z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
    ll z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
    ll z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
    return (z1 < 0 || z2 < 0) && (z1 > 0 || z2 > 0) && (z3 < 0 || z4 < 0) && (z3 > 0 || z4 > 0);
}

bool is_cross_or_touch_intersect(ll x1, ll y1, ll x2, ll y2, ll x3, ll y3, ll x4, ll y4) {
    if (max(x1, x2) < min(x3, x4) || max(x3, x4) < min(x1, x2) || max(y1, y2) < min(y3, y4) ||
        max(y3, y4) < min(y1, y2))
        return false;
    ll z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
    ll z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
    ll z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
    ll z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
    return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
}

optional<pair<double, double>> get_lines_intersection(ll x1, ll y1, ll x2, ll y2, ll x3, ll y3, ll x4, ll y4) {
    ll a1 = y2 - y1;
    ll b1 = x1 - x2;
    ll c1 = -(x1 * y2 - x2 * y1);
    ll a2 = y4 - y3;
    ll b2 = x3 - x4;
    ll c2 = -(x3 * y4 - x4 * y3);
    ll det = a1 * b2 - a2 * b1;
    if (det == 0)
        return {};
    double x = -(c1 * b2 - c2 * b1) / (double)det;
    double y = -(a1 * c2 - a2 * c1) / (double)det;
    return optional{make_pair(x, y)};
}

// usage example
int main() {
    optional<pair<double, double>> intersection = get_lines_intersection(0, 0, 4, 2, 2, -1, 2, 5);
    cout << (bool)intersection << endl;
    cout << intersection->first << " " << intersection->second << endl;

    optional<pair<double, double>> no_intersection = get_lines_intersection(0, 0, 0, 1, 1, 0, 1, 1);
    cout << (bool)no_intersection << endl;
}
