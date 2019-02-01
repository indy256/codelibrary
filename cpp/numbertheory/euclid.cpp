#include <bits/stdc++.h>

using namespace std;

int mod(int a, int m) {
    a %= m;
    return a >= 0 ? a : a + m;
}

// precondition: m > 0 && gcd(a, m) = 1
int mod_inverse(int a, int m) {
    a = mod(a, m);
    return a == 0 ? 0 : mod(((1 - (long long) mod_inverse(m, a) * m) / a), m);
}

// https://cp-algorithms.com/algebra/linear_congruence_equation.html
// solves a * x = b (mod m)
int solve(int a, int b, int m) {
    int g = gcd(a, m);
    if (b % g) return -1;
    a /= g;
    b /= g;
    int x = (long long) b * mod_inverse(a, m) % m;
    return x;
}

// usage example
int main() {
    cout << solve(2, 3, 5) << endl;
}
