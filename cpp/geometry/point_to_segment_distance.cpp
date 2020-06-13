#include <bits/stdc++.h>

using namespace std;

using ll = long long;

double fastHypot(double x, double y) {
    return sqrt(x * x + y * y);
}

double point_to_segment_distance(int x, int y, int x1, int y1, int x2, int y2) {
    ll dx = x2 - x1;
    ll dy = y2 - y1;
    ll px = x - x1;
    ll py = y - y1;
    ll squaredLength = dx * dx + dy * dy;
    ll dotProduct = dx * px + dy * py;
    if (dotProduct <= 0 || squaredLength == 0)
        return fastHypot(px, py);
    if (dotProduct >= squaredLength)
        return fastHypot(px - dx, py - dy);
    double q = (double)dotProduct / squaredLength;
    return fastHypot(px - q * dx, py - q * dy);
}

double point_to_line_distance(ll x, ll y, ll a, ll b, ll c) {
    return abs(a * x + b * y + c) / fastHypot(a, b);
}

// usage example
int main() {
    cout << fixed << setprecision(10);
    cout << point_to_segment_distance(0, 0, 0, 1, 1, 0) << endl;
    cout << point_to_line_distance(0, 0, 1, 1, 1) << endl;
}
