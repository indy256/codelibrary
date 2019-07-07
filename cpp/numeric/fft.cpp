#include <bits/stdc++.h>
#include "fft.h"

using namespace std;

// usage example
#include "../numbertheory/modint.h"

constexpr int mod = (int) 1e9 + 7;
using mint = modint<mod>;

int main() {
    vector<mint> a{5, 1};
    vector<mint> b{2, 1};
    vector<mint> mul_mod = a * b;
    for (auto x:mul_mod) cout << (int) x << " ";
    cout << endl;

    vector<int> aa{5, 1};
    vector<int> bb{2, 1};
    vector<int> mul = aa * bb;
    for (int x:mul) cout << x << " ";
    cout << endl;
}
