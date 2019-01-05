int mod(int a, int m) {
    a %= m;
    return a >= 0 ? a : a + m;
}

// precondition: m > 0 && gcd(a, m) = 1
int mod_inverse(int a, int m) {
    a = mod(a, m);
    return a == 0 ? 0 : mod(((1 - (long long) mod_inverse(m, a) * m) / a), m);
}

// usage example
int main() {
}
