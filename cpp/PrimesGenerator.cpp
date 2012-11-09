#include <vector>
#include <iostream>
using namespace std;

vector<int> getPrimes(int n) {
	if (n <= 1)
		return vector<int>();
	vector<bool> prime(n + 1, true);
	prime[0] = prime[1] = false;
	vector<int> primes;
	for (int i = 2; i * i <= n; i++)
		if (prime[i]) {
			for (int j = i * i; j <= n; j += i)
				prime[j] = false;
			primes.push_back(i);
		}
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
	int n = 31;
	vector<int> primes = getPrimes(n);

	for (int i = 0; i < primes.size(); i++)
		cout << primes[i] << " ";

	cout << endl;

	for (int i = 0; i <= n; i++)
		if (isPrime(i))
			cout << i << " ";
}
