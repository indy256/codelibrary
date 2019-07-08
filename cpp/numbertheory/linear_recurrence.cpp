#include <bits/stdc++.h>
#include "polynom.h"

using namespace std;

// returns f[n] = f[n-1]*a[k-1] + ... + f[n-k]*a[0], where f[0], ..., f[k-1] are provided
// O(k*log(k)*log(n)) complexity
template<class T>
T nth_element_of_recurrence(vector<T> a, const vector<T> &f, long long n) {
    if (n < f.size())
        return f[n];
    a = -a;
    a.emplace_back(1);
    vector<T> xn = power({0, 1}, n, a);
    return inner_product(f.begin(), f.begin() + min(f.size(), xn.size()), xn.begin(), T{0});
}

// usage example
constexpr int mod = (int) 1e9 + 7;
using mint = modint<mod>;

int main() {
    // Fibonacci numbers
    vector<mint> f{1, 1};
    vector<mint> a{1, 1};
    for (int i = 0; i < 10; ++i) {
        cout << (int) nth_element_of_recurrence(a, f, i) << endl;
    }
    cout << endl;
}
