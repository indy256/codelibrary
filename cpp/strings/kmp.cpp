#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Knuth–Morris–Pratt_algorithm

vector<int> prefix_function(string s) {
    vector<int> p(s.length());
    int k = 0;
    for (int i = 1; i < s.length(); i++) {
        while (k > 0 && s[k] != s[i])
            k = p[k - 1];
        if (s[k] == s[i])
            ++k;
        p[i] = k;
    }
    return p;
}

int find_substring(string haystack, string needle) {
    int m = needle.length();
    if (m == 0)
        return 0;
    vector<int> p = prefix_function(needle);
    for (int i = 0, k = 0; i < haystack.length(); i++) {
        while (k > 0 && needle[k] != haystack[i])
            k = p[k - 1];
        if (needle[k] == haystack[i])
            ++k;
        if (k == m)
            return i + 1 - m;
    }
    return -1;
}

// usage example
int main() {
    int pos = find_substring("acabc", "ab");
    cout << pos << endl;
}
