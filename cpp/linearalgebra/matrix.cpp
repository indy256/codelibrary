#include "matrix.h"

#include <bits/stdc++.h>

using namespace std;

// usage example
#include "../numbertheory/modint.h"

constexpr int mod = (int)1e9 + 7;
using mint = modint<mod>;

int main() {
    // Fibonacci numbers
    vector<mint> f{1, 1};
    vector<mint> a{1, 1};
    for (int i = 0; i < 60; ++i) {
        cout << (int)nth_element_of_recurrence(a, f, i) << endl;
    }

    vector<vector<mint>> A(2, vector<mint>(2));
    A[0][0] = 1;
    A[0][1] = 1;
    A[1][0] = 1;
    matrix_print(matrix_pow_sum(A, 4));
    matrix_print(A + (A ^ 2) + (A ^ 3) + (A ^ 4));
}
