#include <bits/stdc++.h>

using namespace std;

int maximum_zero_submatrix(const vector<vector<int>> &a) {
    int R = a.size();
    int C = a[0].size();

    int res = 0;
    vector<int> d(C, -1);
    vector<int> d1(C);
    vector<int> d2(C);
    vector<int> st(C);
    for (int r = 0; r < R; ++r) {
        for (int c = 0; c < C; ++c)
            if (a[r][c] == 1)
                d[c] = r;
        int size = 0;
        for (int c = 0; c < C; ++c) {
            while (size > 0 && d[st[size - 1]] <= d[c])
                --size;
            d1[c] = size == 0 ? -1 : st[size - 1];
            st[size++] = c;
        }
        size = 0;
        for (int c = C - 1; c >= 0; --c) {
            while (size > 0 && d[st[size - 1]] <= d[c])
                --size;
            d2[c] = size == 0 ? C : st[size - 1];
            st[size++] = c;
        }
        for (int j = 0; j < C; ++j)
            res = max(res, (r - d[j]) * (d2[j] - d1[j] - 1));
    }
    return res;
}

// usage example
int main() {
    vector<vector<int>> m{{1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 0, 1}};
    int area = maximum_zero_submatrix(m);
    cout << area << endl;
}
