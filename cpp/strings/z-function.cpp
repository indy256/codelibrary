#include <bits/stdc++.h>

using namespace std;

// z[i] = lcp(s[0..], s[i..])
vector<int> z_function(const string &s) {
    vector<int> z(s.length());
    for (int i = 1, l = 0, r = 0; i < z.size(); ++i) {
        if (i <= r)
            z[i] = min(r - i + 1, z[i - l]);
        while (i + z[i] < z.size() && s[z[i]] == s[i + z[i]])
            ++z[i];
        if (r < i + z[i] - 1) {
            l = i;
            r = i + z[i] - 1;
        }
    }
    return z;
}

// usage example
int main() {
    vector<int> z = z_function("abcababc");
    for (int x : z)
        cout << x << " ";
    cout << endl;
}
