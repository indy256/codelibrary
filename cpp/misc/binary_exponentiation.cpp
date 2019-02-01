#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Exponentiation_by_squaring
// https://cp-algorithms.com/algebra/binary-exp.html
int pow_mod(int x, int n, int mod) {
    int y = x;
    int res = 1;
    for (; n > 0; n >>= 1) {
        if (n & 1)
            res = (long long) res * y % mod;
        y = (long long) y * y % mod;
    }
    return res;
}

// usage example
int main() {
    const int MOD = 1000000007;
    int x = pow_mod(2, 10, MOD);
    cout << x << endl;
}
