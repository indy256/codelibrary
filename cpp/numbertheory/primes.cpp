#include <bits/stdc++.h>

using namespace std;

// https://e-maxx-eng.appspot.com/algebra/sieve-of-eratosthenes.html

vector<int> get_primes(int n) {
    if (n <= 1)
        return {};
    vector<bool> prime(n + 1, true);
    prime[0] = prime[1] = false;

    for (int i = 2; i * i <= n; i++)
        if (prime[i])
            for (int j = i * i; j <= n; j += i)
                prime[j] = false;

    vector<int> primes;
    for (int i = 0; i < prime.size(); ++i)
        if (prime[i])
            primes.emplace_back(i);

    return primes;
}

bool is_prime(long long n) {
    if (n <= 1)
        return false;

    for (long long i = 2; i * i <= n; i++)
        if (n % i == 0)
            return false;

    return true;
}

// usage example
int main() {
    int n = 31;
    for (int prime : get_primes(n))
        cout << prime << " ";

    cout << endl;

    for (int i = 0; i <= n; i++)
        if (is_prime(i))
            cout << i << " ";
}
