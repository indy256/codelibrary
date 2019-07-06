#include <bits/stdc++.h>

using namespace std;

using vi = vector<int>;
using vvi = vector<vi>;

const int mod = 1000'000'007;

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
    vvi res = matrix_unit(a.size());
    vvi x = a;
    while (true) {
        if (p & 1)
            res = matrix_mul(res, x);
        p >>= 1;
        if (!p) break;
        x = matrix_mul(x, x);
    }
    return res;
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

// returns f[n] = f[n-1]*a[k-1] + ... + f[n-k]*a[0], where f[0], ..., f[k-1] are given
int nth_recurrence_relation(const vi &f, const vi &a, int n) {
    int k = f.size();
    if (n < k)
        return f[n];
    vvi A(k, vi(k));
    vvi F(k, vi(1));
    for (int i = 0; i < k; ++i) {
        A[0][i] = a[k - 1 - i];
        F[i][0] = f[i];
    }
    for (int i = 0; i < k - 1; ++i) {
        A[i + 1][i] = 1;
    }
    vvi An = matrix_pow(A, n - k + 1);
    return matrix_mul(An, F)[0][0];
}

void matrix_print(const vvi &a) {
    for (auto &row : a) {
        for (int x:row) cout << x << " ";
        cout << endl;
    }
}

// usage example
int main() {
    vvi A(2, vi(2));
    A[0][0] = 1;
    A[0][1] = 1;
    A[1][0] = 1;
    vvi B = matrix_pow(A, 2);
    matrix_print(B);

    // Fibonacci numbers
    vector f{1, 1};
    vector a{1, 1};
    for (int i = 0; i < 10; ++i) {
        cout << nth_recurrence_relation(f, a, i) << endl;
    }
}
