#include <iostream>
using namespace std;

int pow(long long a, int n, int MOD) {
    int res = 1;
    for (; n > 0; n >>= 1) {
        if (n & 1)
            res = res * a % MOD;
        a = a * a % MOD;
    }
    return res;
}

int main() {
    const int MOD = 1000000007;
    int x = pow(2, 10, MOD);
    cout << x << endl;
}
