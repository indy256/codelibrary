#include <bits/stdc++.h>
#include "polynom.h"

using namespace std;

// usage example
constexpr int mod = (int) 1e9 + 7;
using mint = modint<mod>;

int main() {
    {
        vector<mint> poly{3, 2, 1};
        vector<mint> x{1, 2, 3, 4, 5};
        vector<mint> values = evaluate(poly, x);
        for (int i = 0; i < x.size(); ++i) {
            cout << (int) values[i] << " " << (int) evaluate(poly, x[i]) << endl;
        }
        cout << endl;
    }
    {
        vector<mint> x{7, 2, 1, 3, 5, 6};
        vector<mint> y;
        for (int i = 0; i < x.size(); ++i) {
            y.emplace_back(x[i] * x[i] * x[i] + 5 * x[i] * x[i] + x[i] + 3);
        }
        vector<mint> poly = interpolate(x, y);
        for (int i = 0; i < poly.size(); ++i) {
            cout << (int) poly[i] << " ";
        }
        cout << endl;
    }
}
