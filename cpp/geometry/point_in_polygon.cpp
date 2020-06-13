#include <bits/stdc++.h>

using namespace std;

using ll = long long;

int pointInPolygon(int qx, int qy, const vector<int> &x, const vector<int> &y) {
    int n = x.size();
    int cnt = 0;
    for (int i = 0, j = n - 1; i < n; j = i++) {
        if (y[i] == qy && (x[i] == qx || (y[j] == qy && (x[i] <= qx || x[j] <= qx) && (x[i] >= qx || x[j] >= qx))))
            return 0;  // boundary
        if ((y[i] > qy) != (y[j] > qy)) {
            ll det = ((ll)x[i] - qx) * ((ll)y[j] - qy) - ((ll)x[j] - qx) * ((ll)y[i] - qy);
            if (det == 0)
                return 0;  // boundary
            if ((det > 0) != (y[j] > y[i]))
                ++cnt;
        }
    }
    return cnt % 2 == 0 ? -1 /* exterior */ : 1 /* interior */;
}

// usage example
int main() {
    vector<int> x{0, 0, 2, 2};
    vector<int> y{0, 2, 2, 0};
    cout << (1 == pointInPolygon(1, 1, x, y)) << endl;
    cout << (0 == pointInPolygon(0, 0, x, y)) << endl;
    cout << (-1 == pointInPolygon(0, 3, x, y)) << endl;
}
