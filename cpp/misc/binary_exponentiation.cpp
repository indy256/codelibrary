#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Exponentiation_by_squaring
// https://cp-algorithms.com/algebra/binary-exp.html
int pow_mod(int x, int n, int mod) {
    int res = 1;
    for (long long p = x; n > 0; n >>= 1, p = (p * p) % mod)
        if ((n & 1) != 0)
            res = (int)(res * p % mod);
    return res;
}

// usage example
int main() {
    const int MOD = 1000'000'007;
    int x = pow_mod(2, 10, MOD);
    cout << x << endl;
}
