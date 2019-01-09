#include <bits/stdc++.h>

using namespace std;

vector<double> gauss(vector<vector<double>> a, vector<double> b) {
    int n = a.size();
    for (int row = 0; row < n; row++) {
        int best = row;
        for (int i = row + 1; i < n; i++)
            if (abs(a[best][row]) < abs(a[i][row]))
                best = i;
        swap(a[row], a[best]);
        swap(b[row], b[best]);
        for (int i = row + 1; i < n; i++)
            a[row][i] /= a[row][row];
        b[row] /= a[row][row];
        // a[row][row] = 1;
        for (int i = 0; i < n; i++) {
            double z = a[i][row];
            if (i != row && z != 0) {
                // row + 1 instead of row is an optimization
                for (int j = row + 1; j < n; j++)
                    a[i][j] -= a[row][j] * z;
                b[i] -= b[row] * z;
            }
        }
    }
    return b;
}

// usage example
int main() {
    vector<vector<double>> a = {{4,  2, -1},
                                {2,  4, 3},
                                {-1, 3, 5}};
    vector<double> b = {1, 0, 0};
    vector<double> x = gauss(a, b);
    for (int i = 0; i < a.size(); i++) {
        double y = 0;
        for (int j = 0; j < a[i].size(); j++)
            y += a[i][j] * x[j];
        if (abs(b[i] - y) > 1e-9)
            throw std::runtime_error("");
    }
}
