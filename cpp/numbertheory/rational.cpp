#include <bits/stdc++.h>
#include "rational.h"

using namespace std;
using rt = rational<long long/*__int128*/>;

// usage example
int main() {
    rt x{1, 2};
    rt y{2, 3};
    if (y > x)
        cout << "y > x" << endl;
    x -= y;
    x = x.abs();
    cout << x.a << "/" << x.b << endl;
}
