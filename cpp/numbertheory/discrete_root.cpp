#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/algebra/discrete-root.html

int pow_mod(int x, int n, int mod) {
    int res = 1;
    for (long long p = x; n > 0; n >>= 1, p = (p * p) % mod)
        if ((n & 1) != 0)
            res = (int)(res * p % mod);
    return res;
}

int generator(int m) {
    if (m == 2)
        return 1;
    vector<int> factors;
    int phi = m - 1;
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

// returns any x such that x^a = b (mod m)
// precondition: m is prime
int discrete_root(int a, int b, int m) {
    if (a == 0)
        return -1;
    int g = generator(m);
    int sq = (int)sqrt(m) + 1;
    vector<pair<int, int>> dec(sq);
    for (int i = 1; i <= sq; ++i)
        dec[i - 1] = {pow_mod(g, (long long)i * sq * b % (m - 1), m), i};
    sort(dec.begin(), dec.end());
    for (int i = 0; i < sq; ++i) {
        int my = pow_mod(g, (long long)i * b % (m - 1), m) * (long long)a % m;
        auto it = lower_bound(dec.begin(), dec.end(), make_pair(my, 0));
        if (it != dec.end() && it->first == my) {
            int x = it->second * sq - i;
            int delta = (m - 1) / gcd(b, m - 1);
            return pow_mod(g, x % delta, m);
        }
    }
    return -1;
}

// usage example
int main() {
    cout << discrete_root(3, 3, 5) << endl;
}
