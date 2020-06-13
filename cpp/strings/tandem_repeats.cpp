#include <bits/stdc++.h>

using namespace std;

vector<int> z_function(const string &s) {
    int n = (int)s.length();
    vector<int> z(n);
    for (int i = 1, l = 0, r = 0; i < n; ++i) {
        if (i <= r)
            z[i] = min(r - i + 1, z[i - l]);
        while (i + z[i] < n && s[z[i]] == s[i + z[i]])
            ++z[i];
        if (i + z[i] - 1 > r)
            l = i, r = i + z[i] - 1;
    }
    return z;
}

void output_tandem(const string &s, int shift, bool left, int cntr, int l, int l1, int l2) {
    int pos;
    if (left)
        pos = cntr - l1;
    else
        pos = cntr - l1 - l2 - l1 + 1;
    cout << "[" << shift + pos << ".." << shift + pos + 2 * l - 1 << "] = " << s.substr(pos, 2 * l) << endl;
}

void output_tandems(const string &s, int shift, bool left, int cntr, int l, int k1, int k2) {
    for (int l1 = 1; l1 <= l; ++l1) {
        if (left && l1 == l)
            break;
        if (l1 <= k1 && l - l1 <= k2)
            output_tandem(s, shift, left, cntr, l, l1, l - l1);
    }
}

inline int get_z(const vector<int> &z, int i) {
    return 0 <= i && i < (int)z.size() ? z[i] : 0;
}

void find_tandems(string s, int shift = 0) {
    int n = (int)s.length();
    if (n == 1)
        return;

    int nu = n / 2;
    int nv = n - nu;
    string u = s.substr(0, nu);
    string v = s.substr(nu);
    string ru = string(u.rbegin(), u.rend());
    string rv = string(v.rbegin(), v.rend());

    find_tandems(u, shift);
    find_tandems(v, shift + nu);

    vector<int> z1 = z_function(ru);
    vector<int> z2 = z_function(v + '#' + u);
    vector<int> z3 = z_function(ru + '#' + rv);
    vector<int> z4 = z_function(v);
    for (int cntr = 0; cntr < n; ++cntr) {
        int l, k1, k2;
        if (cntr < nu) {
            l = nu - cntr;
            k1 = get_z(z1, nu - cntr);
            k2 = get_z(z2, nv + 1 + cntr);
        } else {
            l = cntr - nu + 1;
            k1 = get_z(z3, nu + 1 + nv - 1 - (cntr - nu));
            k2 = get_z(z4, (cntr - nu) + 1);
        }
        if (k1 + k2 >= l)
            output_tandems(s, shift, cntr < nu, cntr, l, k1, k2);
    }
}

// usage example
int main() {
    find_tandems("abcabczz");
}
