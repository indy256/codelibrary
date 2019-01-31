#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/algebra/primitive-root.html

int powmod(int x, int n, int mod) {
    int y = x;
    int res = 1;
    for (; n > 0; n >>= 1) {
        if (n & 1)
            res = (long long) res * y % mod;
        y = (long long) y * y % mod;
    }
    return res;
}

// returns g such that g^i runs through all numbers from 1 to p-1 modulo p
// p - prime
int generator(int p) {
    vector<int> factors;
    int phi = p - 1;
    int n = phi;
    for (int i = 2; i * i <= n; ++i)
        if (n % i == 0) {
            factors.push_back(i);
            while (n % i == 0)
                n /= i;
        }
    if (n > 1)
        factors.push_back(n);
    for (int res = 2; res <= p; ++res) {
        bool ok = true;
        for (size_t i = 0; i < factors.size() && ok; ++i)
            ok &= powmod(res, phi / factors[i], p) != 1;
        if (ok) return res;
    }
    return -1;
}

// usage example
int main() {
    cout << generator(7) << endl;
}
