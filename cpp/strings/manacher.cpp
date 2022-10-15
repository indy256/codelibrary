#include <bits/stdc++.h>

using namespace std;

// Manacher's algorithm: https://cp-algorithms.com/string/manacher.html

// d1[i] - how many palindromes of odd length with center at i
vector<int> odd_palindromes(const string &s) {
    size_t n = s.size();
    vector<int> d1(n);
    int l = 0, r = -1;
    for (int i = 0; i < n; ++i) {
        int len = i > r ? 1 : min(d1[l + r - i], r - i + 1);
        while (i - len >= 0 && i + len < n && s[i - len] == s[i + len])
            ++len;
        d1[i] = len;
        if (r < i + len - 1) {
            r = i + len - 1;
            l = i - (len - 1);
        }
    }
    return d1;
}

// d2[i] - how many palindromes of even length with center at i
vector<int> even_palindromes(const string &s) {
    size_t n = s.size();
    vector<int> d2(n);
    int l = 0, r = -1;
    for (int i = 0; i < n; ++i) {
        int len = i > r ? 0 : min(d2[l + r - i + 1], r - i + 1);
        while (i - len - 1 >= 0 && i + len < n && s[i - len - 1] == s[i + len])
            ++len;
        d2[i] = len;
        if (r < i + len - 1) {
            r = i + len - 1;
            l = i - len;
        }
    }
    return d2;
}

// usage example
int main() {
    string text = "abbba";

    auto d1 = odd_palindromes(text);
    for (int d : d1)
        cout << d << " ";
    cout << endl;

    auto d2 = even_palindromes(text);
    for (int d : d2)
        cout << d << " ";
    cout << endl;
}
