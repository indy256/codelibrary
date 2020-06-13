#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/algebra/primitive-root.html

int pow_mod(int x, int n, int mod) {
    int res = 1;
    for (long long p = x; n > 0; n >>= 1, p = (p * p) % mod)
        if ((n & 1) != 0)
            res = (int)(res * p % mod);
    return res;
}

int totient_function(int n) {
    int res = n;
    for (int i = 2; i * i <= n; ++i)
        if (n % i == 0) {
            while (n % i == 0)
                n /= i;
            res -= res / i;
        }
    if (n > 1)
        res -= res / n;
    return res;
}

// returns g such that g^i runs through all numbers from 1 to m-1 modulo m
// g exists for m = 2,4,p^a,2*p^a, where p > 2 is a prime number
// O(m^0.5) complexity
int generator(int m) {
    if (m == 2)
        return 1;
    vector<int> factors;
    int phi = totient_function(m);
    int n = phi;
    for (int i = 2; i * i <= n; ++i)
        if (n % i == 0) {
            factors.emplace_back(i);
            while (n % i == 0)
                n /= i;
        }
    if (n > 1)
        factors.emplace_back(n);
    for (int res = 2; res <= m; ++res) {
        if (gcd(res, m) != 1)
            continue;
        bool ok = true;
        for (size_t i = 0; i < factors.size() && ok; ++i)
            ok &= pow_mod(res, phi / factors[i], m) != 1;
        if (ok)
            return res;
    }
    return -1;
}

// usage example
int main() {
    for (int i = 0; i < 15; ++i) {
        cout << "generator(" << i << ") = " << generator(i) << endl;
    }
    cout << "generator(" << 998244353 << ") = " << generator(998244353) << endl;
}
