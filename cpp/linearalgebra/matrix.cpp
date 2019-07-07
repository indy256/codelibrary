#include <bits/stdc++.h>

using namespace std;

template<class T>
vector<vector<T>> matrix_unit(int n) {
    vector<vector<T>> res(n, vector<T>(n));
    for (int i = 0; i < n; i++)
        res[i][i] = 1;
    return res;
}

template<class T>
vector<vector<T>> &operator+=(vector<vector<T>> &a, const vector<vector<T>> &b) {
    for (int i = 0; i < a.size(); i++)
        for (int j = 0; j < a[0].size(); j++)
            a[i][j] += b[i][j];
    return a;
}

template<class T>
vector<vector<T>> operator+(vector<vector<T>> a, const vector<vector<T>> &b) {
    a += b;
    return a;
}

template<class T>
vector<vector<T>> operator*(const vector<vector<T>> &a, const vector<vector<T>> &b) {
    int n = a.size();
    int m = a[0].size();
    int k = b[0].size();
    vector<vector<T>> res(n, vector<T>(k));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < k; j++)
            for (int p = 0; p < m; p++)
                res[i][j] += a[i][p] * b[p][j];
    return res;
}

template<class T>
vector<vector<T>> &operator*=(vector<vector<T>> &a, const vector<vector<T>> &b) {
    a = a * b;
    return a;
}

template<class T>
vector<vector<T>> operator^(const vector<vector<T>> &a, long long p) {
    vector<vector<T>> res = matrix_unit<T>(a.size());
    int highest_one_bit = p ? __builtin_clzll(1) - __builtin_clzll(p) : -1;
    for (int i = highest_one_bit; i >= 0; i--) {
        res *= res;
        if (p >> i & 1) {
            res *= a;
        }
    }
    return res;
}

// a + a^2 + ... + a^p
template<class T>
vector<vector<T>> matrix_pow_sum(const vector<vector<T>> &a, long long p) {
    int n = a.size();
    vector<vector<T>> res = vector<vector<T>>(n, vector<T>(n));
    vector<vector<T>> b = matrix_unit<T>(n);
    int highest_one_bit = p ? __builtin_clzll(1) - __builtin_clzll(p) : -1;
    for (int i = highest_one_bit; i >= 0; i--) {
        res = res * (matrix_unit<T>(n) + b);
        b *= b;
        if (p >> i & 1) {
            b *= a;
            res = res * a + a;
        }
    }
    return res;
}

// returns f[n] = f[n-1]*a[k-1] + ... + f[n-k]*a[0], where f[0], ..., f[k-1] are provided
// O(k^3*log(n)) complexity
template<class T>
T nth_element_of_recurrence(const vector<T> &a, const vector<T> &f, long long n) {
    int k = f.size();
    if (n < k)
        return f[n];
    vector<vector<T>> A(k, vector<T>(k));
    vector<vector<T>> F(k, vector<T>(1));
    for (int i = 0; i < k; ++i) {
        A[0][i] = a[k - 1 - i];
        F[i][0] = f[i];
    }
    for (int i = 0; i < k - 1; ++i) {
        A[i + 1][i] = 1;
    }
    vector<vector<T>> An = A ^(n - k + 1);
    return (An * F)[0][0];
}

template<class T>
void matrix_print(const vector<vector<T>> &a) {
    for (auto &row : a) {
        for (T x:row) cout << (int) x << " ";
        cout << endl;
    }
}

// usage example
#include "../numbertheory/modint.cpp"

int main() {
    // Fibonacci numbers
    vector<mint> f{1, 1};
    vector<mint> a{1, 1};
    for (int i = 0; i < 60; ++i) {
        cout << (int) nth_element_of_recurrence(a, f, i) << endl;
    }

    vector<vector<mint>> A(2, vector<mint>(2));
    A[0][0] = 1;
    A[0][1] = 1;
    A[1][0] = 1;
    matrix_print(matrix_pow_sum(A, 4));
    matrix_print(A + (A ^ 2) + (A ^ 3) + (A ^ 4));
}
