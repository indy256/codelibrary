#include <bits/stdc++.h>

using namespace std;

string min_cyclic_shift(string s) {
    s += s;
    int n = s.length();
    int i = 0;
    int pos = 0;
    while (i < n / 2) {
        pos = i;
        int j = i + 1, k = i;
        while (j < n && s[k] <= s[j]) {
            if (s[k] < s[j])
                k = i;
            else
                ++k;
            ++j;
        }
        while (i <= k)
            i += j - k;
    }
    return s.substr(pos, n / 2);
}

// usage example
int main() {
    cout << min_cyclic_shift("bcabab") << endl;
}
