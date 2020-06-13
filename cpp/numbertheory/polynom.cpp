#include "polynom.h"

#include <bits/stdc++.h>

using namespace std;

// usage example
constexpr int mod = (int)1e9 + 7;
using mint = modint<mod>;

int main() {
    {
        vector<mint> poly{3, 2, 1};
        vector<mint> x{1, 2, 3, 4, 5};
        vector<mint> values = evaluate(poly, x);
        for (size_t i = 0; i < x.size(); ++i) {
            cout << (int)values[i] << " " << (int)evaluate(poly, x[i]) << endl;
        }
        cout << endl;
    }
    {
        vector<mint> x{7, 2, 1, 3, 5, 6};
        vector<mint> y;
        for (size_t i = 0; i < x.size(); ++i) {
            y.emplace_back(x[i] * x[i] * x[i] + 5 * x[i] * x[i] + x[i] + 3);
        }
        vector<mint> poly = interpolate(x, y);
        for (size_t i = 0; i < poly.size(); ++i) {
            cout << (int)poly[i] << " ";
        }
        cout << endl << endl;
    }
    {
        for (int i = 0; i < 10; ++i) {
            vector<mint> s = sequence<mint>(i);
            for (auto x : s)
                cout << (int)x << " ";
            cout << endl;
        }
        cout << endl;
    }
    {
        for (int i = 0; i < 10; ++i) {
            cout << (int)factorial<mint>(i) << endl;
        }

        auto t1 = chrono::high_resolution_clock::now();
        cout << (int)factorial<mint>(1000'000'000) << endl;
        auto t2 = chrono::high_resolution_clock::now();
        chrono::duration<double, milli> duration = t2 - t1;
        cout << duration.count() << " ms" << endl;

        cout << endl;
    }
}
