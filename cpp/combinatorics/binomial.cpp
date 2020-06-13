#include <bits/stdc++.h>
using namespace std;

int binomial(int n, int k) {
    int res = 1;
    for (int i = 0; i < k; i++) {
        res = res * (n - i) / (i + 1);
    }
    return res;
}

int main() {
    cout << binomial(5, 3) << endl;
}
