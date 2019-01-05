#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Exponentiation_by_squaring
// https://cp-algorithms.com/algebra/binary-exp.html
int pow(int x, int n, int m) {
    int y = x;
    int res = 1;
    for (; n > 0; n >>= 1) {
        if (n & 1)
            res = (long long) res * y % m;
        y = (long long) y * y % m;
    }
    return res;
}

// usage example
int main() {
    const int MOD = 1000000007;
    int x = pow(2, 10, MOD);
    cout << x << endl;
}
