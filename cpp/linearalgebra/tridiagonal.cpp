#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
template <class T>
vector<T> tridiagonal_solve(vector<T> diag, const vector<T> &super, const vector<T> &sub, vector<T> b) {
    for (int i = 0; i < b.size() - 1; ++i) {
        diag[i + 1] -= super[i] * sub[i] / diag[i];
        b[i + 1] -= b[i] * sub[i] / diag[i];
    }
    for (int i = b.size() - 1; i > 0; --i) {
        b[i] /= diag[i];
        b[i - 1] -= b[i] * super[i - 1];
    }
    b[0] /= diag[0];
    return b;
}

// usage example
int main() {
    // -1  1  0   x[0]   5
    //  3 -1  2 * x[1] = 6
    //  0  4 -1   x[2]   7
    vector<double> x = tridiagonal_solve<double>({-1, -1, -1}, {1, 2}, {3, 4}, {5, 6, 7});

    for (double v : x)
        cout << fixed << setprecision(5) << v << " ";
    cout << endl;
}
