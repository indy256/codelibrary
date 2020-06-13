#include <bits/stdc++.h>

using namespace std;

vector<double> gauss(vector<vector<double>> A, vector<double> b) {
    int n = A.size();
    for (int i = 0; i < n; i++) {
        int pivot = i;
        for (int j = i + 1; j < n; j++)
            if (abs(A[pivot][i]) < abs(A[j][i]))
                pivot = j;
        swap(A[i], A[pivot]);
        swap(b[i], b[pivot]);
        for (int j = i + 1; j < n; j++)
            A[i][j] /= A[i][i];
        b[i] /= A[i][i];
        for (int j = 0; j < n; j++)
            if (j != i && A[j][i] != 0) {
                for (int k = i + 1; k < n; k++)
                    A[j][k] -= A[i][k] * A[j][i];
                b[j] -= b[i] * A[j][i];
            }
    }
    return b;
}

template <class T>
vector<T> gauss(vector<vector<T>> A, vector<T> b) {
    int n = A.size();
    for (int i = 0; i < n; i++) {
        int pivot = i;
        for (int j = i; j < n; j++)
            if (A[j][i] != 0) {
                pivot = j;
                break;
            }
        assert(A[pivot][i] != 0);
        swap(A[i], A[pivot]);
        swap(b[i], b[pivot]);
        for (int j = i + 1; j < n; j++)
            A[i][j] /= A[i][i];
        b[i] /= A[i][i];
        for (int j = 0; j < n; j++)
            if (j != i && A[j][i] != 0) {
                for (int k = i + 1; k < n; k++)
                    A[j][k] -= A[i][k] * A[j][i];
                b[j] -= b[i] * A[j][i];
            }
    }
    return b;
}

template <class T>
vector<vector<T>> inverse(vector<vector<T>> A) {
    int n = A.size();
    vector<vector<T>> B(n, vector<T>(n));
    for (int i = 0; i < n; ++i)
        B[i][i] = 1;
    for (int i = 0; i < n; i++) {
        int pivot = i;
        for (int j = i; j < n; j++)
            if (A[j][i] != 0) {
                pivot = j;
                break;
            }
        assert(A[pivot][i] != 0);
        swap(A[i], A[pivot]);
        swap(B[i], B[pivot]);
        T Aii = A[i][i];
        for (int j = 0; j < n; j++) {
            A[i][j] /= Aii;
            B[i][j] /= Aii;
        }
        for (int j = 0; j < n; j++) {
            T Aji = A[j][i];
            if (j != i && Aji != 0)
                for (int k = 0; k < n; k++) {
                    A[j][k] -= A[i][k] * Aji;
                    B[j][k] -= B[i][k] * Aji;
                }
        }
    }
    return B;
}

#include "../numbertheory/modint.h"
#include "matrix.h"

constexpr int mod = (int)1e9 + 7;
using mint = modint<mod>;

// usage example
int main() {
    {
        vector<vector<double>> A{{4, 2, -1}, {2, 4, 3}, {-1, 3, 5}};
        vector<double> b = {1, 0, 0};
        vector<double> x = gauss(A, b);
        vector<vector<double>> y = A * transpose(vector<vector<double>>{x});
        for (int i = 0; i < b.size(); i++)
            assert(abs(b[i] - y[i][0]) < 1e-9);
    }
    {
        vector<vector<mint>> A{{4, 2, -1}, {2, 4, 3}, {-1, 3, 5}};
        vector<mint> b = {1, 2, 3};
        vector<mint> x = gauss(A, b);
        vector<vector<mint>> y = A * transpose(vector<vector<mint>>{x});
        assert(transpose(y)[0] == b);

        vector<vector<mint>> B = inverse(A);
        vector<vector<mint>> E = A * B;
        for (int i = 0; i < A.size(); ++i)
            for (int j = 0; j < A.size(); ++j)
                assert(E[i][j] == (i == j ? 1 : 0));
    }
}
