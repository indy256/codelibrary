#include <bits/stdc++.h>

using namespace std;

// Manacher's algorithm: https://e-maxx-eng.appspot.com/string/manacher.html

// d1[i] - how many palindromes of odd length with center at i
vector<int> odd_palindromes(const string &s) {
    size_t n = s.size();
    vector<int> d1(n);
    int l = 0, r = -1;
    for (int i = 0; i < n; ++i) {
        int k = (i > r ? 0 : min(d1[l + r - i], r - i)) + 1;
        while (i + k < n && i - k >= 0 && s[i + k] == s[i - k]) ++k;
        d1[i] = k--;
        if (i + k > r)
            l = i - k, r = i + k;
    }
    return d1;
}

// d2[i] - how many palindromes of even length with center at i
vector<int> even_palindromes(const string &s) {
    size_t n = s.size();
    vector<int> d2(n);
    int l = 0, r = -1;
    for (int i = 0; i < n; ++i) {
        int k = (i > r ? 0 : min(d2[l + r - i + 1], r - i + 1)) + 1;
        while (i + k - 1 < n && i - k >= 0 && s[i + k - 1] == s[i - k]) ++k;
        d2[i] = --k;
        if (i + k - 1 > r)
            l = i - k, r = i + k - 1;
    }
    return d2;
}

int main() {
    string text = "aaaba";

    auto d1 = odd_palindromes(text);
    copy(d1.begin(), d1.end(), ostream_iterator<int>(cout, " "));
    cout << endl;

    auto d2 = even_palindromes(text);
    copy(d2.begin(), d2.end(), ostream_iterator<int>(cout, " "));
    cout << endl;
}
