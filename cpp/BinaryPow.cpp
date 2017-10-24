#include <iostream>

using namespace std;

int pow(int x, int n, int MOD) {
	long long y = x;
	int res = 1;
	for (; n > 0; n >>= 1) {
		if (n & 1)
			res = res * y % MOD;
		y = y * y % MOD;
	}
	return res;
}

int main() {
	const int MOD = 1000000007;
	int x = pow(2, 10, MOD);
	cout << x << endl;
}
