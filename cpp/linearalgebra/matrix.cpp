#include <bits/stdc++.h>

using namespace std;

typedef vector<int> vi;
typedef vector<vi> vvi;

const int mod = 1234567891;

vvi matrix_unit(int n) {
    vvi res(n, vi(n));
    for (int i = 0; i < n; i++)
        res[i][i] = 1;
    return res;
}

vvi matrix_add(const vvi &a, const vvi &b) {
    int n = a.size();
    int m = a[0].size();
    vvi res(n, vi(m));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            res[i][j] = (a[i][j] + b[i][j]) % mod;
    return res;
}

vvi matrix_mul(const vvi &a, const vvi &b) {
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

vvi matrix_pow(const vvi &a, int p) {
    if (p == 0)
        return matrix_unit(a.size());
    if (p % 2 == 0)
        return matrix_pow(matrix_mul(a, a), p / 2);
    else
        return matrix_mul(a, matrix_pow(a, p - 1));
}

// a + a^2 + ... + a^p
vvi matrix_pow_sum(const vvi &a, int p) {
    int n = a.size();
    if (p == 0)
        return vvi(n, vi(n));
    if (p % 2 == 0)
        return matrix_mul(matrix_pow_sum(a, p / 2), matrix_add(matrix_unit(n), matrix_pow(a, p / 2)));
    else
        return matrix_add(a, matrix_mul(matrix_pow_sum(a, p - 1), a));
}

void matrix_print(const vvi &a) {
    for (auto row : a) {
        copy(row.begin(), row.end(), ostream_iterator<int>(cout, " "));
        cout << endl;
    }
}

// usage example
int main() {
    vvi a(2, vi(2));
    a[0][0] = 1;
    a[0][1] = 1;
    a[1][0] = 1;
    vvi b = matrix_pow(a, 2);
    matrix_print(b);
}
