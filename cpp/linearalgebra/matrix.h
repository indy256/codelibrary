#include <bits/stdc++.h>

using namespace std;

template <class T>
vector<vector<T>> matrix_unit(int n) {
    vector<vector<T>> res(n, vector<T>(n));
    for (int i = 0; i < n; i++)
        res[i][i] = 1;
    return res;
}

template <class T>
vector<vector<T>> &operator+=(vector<vector<T>> &a, const vector<vector<T>> &b) {
    for (size_t i = 0; i < a.size(); i++)
        for (size_t j = 0; j < a[0].size(); j++)
            a[i][j] += b[i][j];
    return a;
}

template <class T>
vector<vector<T>> operator+(vector<vector<T>> a, const vector<vector<T>> &b) {
    a += b;
    return a;
}

template <class T>
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

template <class T>
vector<vector<T>> &operator*=(vector<vector<T>> &a, const vector<vector<T>> &b) {
    a = a * b;
    return a;
}

template <class T>
vector<vector<T>> operator^(const vector<vector<T>> &a, long long p) {
    vector<vector<T>> res = matrix_unit<T>(a.size());
    int highest_one_bit = -1;
    while (1LL << (highest_one_bit + 1) <= p)
        ++highest_one_bit;
    for (int i = highest_one_bit; i >= 0; i--) {
        res *= res;
        if (p >> i & 1) {
            res *= a;
        }
    }
    return res;
}

template <class T>
vector<vector<T>> transpose(const vector<vector<T>> &a) {
    int n = a.size();
    int m = a[0].size();
    vector<vector<T>> b(m, vector<T>(n));
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            b[j][i] = a[i][j];
        }
    }
    return b;
}

// a + a^2 + ... + a^p
template <class T>
vector<vector<T>> matrix_pow_sum(const vector<vector<T>> &a, long long p) {
    int n = a.size();
    vector<vector<T>> res = vector<vector<T>>(n, vector<T>(n));
    vector<vector<T>> b = matrix_unit<T>(n);
    int highest_one_bit = -1;
    while (1LL << (highest_one_bit + 1) <= p)
        ++highest_one_bit;
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
template <class T>
T nth_element_of_recurrence(const vector<T> &a, const vector<T> &f, long long n) {
    int k = f.size();
    if (n < k)
        return f[n];
    vector<vector<T>> A(k, vector<T>(k));
    A[k - 1] = a;
    for (int i = 0; i < k - 1; ++i) {
        A[i][i + 1] = 1;
    }
    vector<vector<T>> F = transpose(vector<vector<T>>{f});
    return ((A ^ n) * F)[0][0];
}

template <class T>
void matrix_print(const vector<vector<T>> &a) {
    for (auto &row : a) {
        for (T x : row)
            cout << (int)x << " ";
        cout << endl;
    }
}
