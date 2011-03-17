#include <vector>
#include <iostream>
using namespace std;

vector<int> getPrimes(int n) {
	vector<bool> prime(n + 1, true);
	prime[0] = prime[1] = false;
	for (int i = 2; i <= n; i++)
		if (prime[i])
			for (int j = i + i; j <= n; j += i)
				prime[j] = false;
	vector<int> primes;
	for (int i = 2; i <= n; i++)
		if (prime[i])
			primes.push_back(i);
	return primes;
}

bool isPrime(long long n) {
	if (n <= 1)
		return false;

	for (long long i = 2; i * i <= n; i++)
		if (n % i == 0)
			return false;

	return true;
}

int main() {
	vector<int> primes = getPrimes(10);

	for (int i = 0; i < primes.size(); i++)
		cout << primes[i] << endl;
}
