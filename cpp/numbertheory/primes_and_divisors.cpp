#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/algebra/sieve-of-eratosthenes.html

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
            primes.push_back(i);

    return primes;
}

// Generates prime numbers up to n in O(n) time
vector<int> generate_primes_linear_time(int n) {
    vector<int> lp(n + 1);
    vector<int> primes;
    for (int i = 2; i <= n; ++i) {
        if (lp[i] == 0) {
            lp[i] = i;
            primes.push_back(i);
        }
        for (int j = 0; j < primes.size() && primes[j] <= lp[i] && i * primes[j] <= n; ++j)
            lp[i * primes[j]] = primes[j];
    }
    return primes;
}

vector<int> number_of_prime_divisors(int n) {
    vector<int> divisors(n + 1);
    fill(divisors.begin() + 2, divisors.end(), 1);
    for (int i = 2; i * i <= n; ++i)
        if (divisors[i] == 1)
            for (int j = i; j * i <= n; j++)
                divisors[i * j] = divisors[j] + 1;
    return divisors;
}

// Generates minimum prime divisor of all numbers up to n in O(n) time
vector<int> generate_min_divisors(int n) {
    vector<int> lp(n + 1);
    lp[1] = 1;
    vector<int> primes;
    for (int i = 2; i <= n; ++i) {
        if (lp[i] == 0) {
            lp[i] = i;
            primes.push_back(i);
        }
        for (int j = 0; j < primes.size() && primes[j] <= lp[i] && i * primes[j] <= n; ++j)
            lp[i * primes[j]] = primes[j];
    }
    return lp;
}

// Generates prime divisor of all numbers up to n
vector<int> generate_divisors(int n) {
    vector<int> divisors(n + 1);
    iota(divisors.begin(), divisors.end(), 0);
    for (int i = 2; i * i <= n; i++)
        if (divisors[i] == i)
            for (int j = i * i; j <= n; j += i)
                divisors[j] = i;
    return divisors;
}

// usage example
int main() {
    auto print = [](const vector<int> &a) {
        for (int x : a)
            cout << x << " ";
        cout << endl;
    };

    int n = 32;
    print(get_primes(n));
    print(generate_primes_linear_time(n));
    print(generate_min_divisors(n));
    print(generate_divisors(n));
}
