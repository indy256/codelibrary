#include <bits/stdc++.h>
#include "frac.h"

using namespace std;
using fracll = frac<long long>;

// usage example
int main() {
    fracll x{1, 2};
    fracll y{2, 3};
    x -= y;
    x = x.abs();
    cout << x.a << "/" << x.b << endl;
}
