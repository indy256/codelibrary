#include <bits/stdc++.h>
using namespace std;

bool next_combination(vector<int> &comb, int n) {
    int k = comb.size();
    for (int i = k - 1; i >= 0; i--) {
        if (comb[i] < n - k + i) {
            ++comb[i];
            while (++i < k) {
                comb[i] = comb[i - 1] + 1;
            }
            return true;
        }
    }
    return false;
}

int main() {
    vector<int> comb{0, 1, 2};
    do {
        for (int v : comb)
            cout << v + 1 << " ";
        cout << endl;
    } while (next_combination(comb, 5));
}
