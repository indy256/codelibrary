#include <bits/stdc++.h>

using namespace std;

// returns prime_divisor -> power
// O(sqrt(n)) complexity
map<long long, int> factorize(long long n) {
    map<long long, int> factors;
    for (int d = 2; (long long)d * d <= n; d++) {
        while (n % d == 0) {
            ++factors[d];
            n /= d;
        }
    }
    if (n > 1) {
        ++factors[n];
    }
    return factors;
}

// usage example
int main() {
    map<long long int, int> factors = factorize(4 * 3 * 1000000000039);
    for (auto e : factors) {
        cout << e.first << "^" << e.second << " " << endl;
    }
}
