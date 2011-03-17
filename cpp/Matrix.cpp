#include <vector>
#include <iostream>
using namespace std;

typedef vector<int> vi;
typedef vector<vi> vvi;

const int mod = 1234567891;

vvi matrixUnit(int n) {
    vvi res(n, vi(n));
    for (int i = 0; i < n; i++)
        res[i][i] = 1;
    return res;
}

vvi matrixAdd(const vvi &a, const vvi &b) {
    int n = a.size();
    int m = a[0].size();
    vvi res(n, vi(m));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            res[i][j] = (a[i][j] + b[i][j]) % mod;
    return res;
}

vvi matrixMul(const vvi &a, const vvi &b) {
    int n = a.size();
    int m = a[0].size();
    int k = b[0].size();
    vvi res(n, vi(k));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < k; j++)
            for (int p = 0; p < m; p++)
                res[i][j] = (res[i][j] + (long long) a[i][p] * b[p][j]) % mod;
    return res;
}

vvi matrixPow(const vvi &a, int p) {
    if (p == 0)
        return matrixUnit(a.size());
    if (p & 1)
        return matrixMul(a, matrixPow(a, p - 1));
    return matrixPow(matrixMul(a, a), p / 2);
}

vvi matrixPowSum(const vvi &a, int p) {
    int n = a.size();
    if (p == 0)
        return vvi(n, vi(n));
    if (p % 2 == 0)
        return matrixMul(matrixPowSum(a, p / 2), matrixAdd(matrixUnit(n), matrixPow(a, p / 2)));
    return matrixAdd(a, matrixMul(matrixPowSum(a, p - 1), a));
}

int main() {
    vvi a(2, vi(2));
    a[0][0] = 1;
    a[0][1] = 1;
    a[1][0] = 1;
    vvi b = matrixPow(a, 10);
}
