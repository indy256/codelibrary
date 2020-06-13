#include <bits/stdc++.h>

using namespace std;

// build suffix array in O(n*log(n))
vector<int> suffix_array(const string &S) {
    int n = S.length();

    // Stable sort of characters.
    // Same characters are sorted by their position in descending order.
    // E.g. last character which represents suffix of length 1 should be ordered first among same characters.
    vector<int> sa;
    for (int i = n - 1; i >= 0; --i) {
        sa.push_back(i);
    }
    stable_sort(sa.begin(), sa.end(), [&](int a, int b) { return S[a] < S[b]; });

    vector<int> classes(n);
    for (int i = 0; i < n; ++i) {
        classes[i] = S[i];
    }
    // sa[i] - suffix on i'th position after sorting by first len characters
    // classes[i] - equivalence class of the i'th suffix after sorting by first len characters

    for (int len = 1; len < n; len *= 2) {
        // Calculate classes for suffixes of length len * 2
        vector<int> c = classes;
        for (int i = 0; i < n; i++) {
            // Condition sa[i - 1] + len < n emulates 0-symbol at the end of the string.
            // A separate class is created for each suffix followed by emulated 0-symbol.
            classes[sa[i]] =
                i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2]
                    ? classes[sa[i - 1]]
                    : i;
        }
        // Suffixes are already sorted by first len characters
        // Now sort suffixes by first len * 2 characters
        vector<int> cnt(n);
        iota(cnt.begin(), cnt.end(), 0);
        vector<int> s = sa;
        for (int i = 0; i < n; i++) {
            // s[i] - order of suffixes sorted by first len characters
            // (s[i] - len) - order of suffixes sorted only by second len characters
            int s1 = s[i] - len;
            // sort only suffixes of length > len, others are already sorted
            if (s1 >= 0)
                sa[cnt[classes[s1]]++] = s1;
        }
    }
    return sa;
}

// https://en.wikipedia.org/wiki/LCP_array
vector<int> lcp_array(const string &s) {
    int n = s.size();
    vector<int> sa = suffix_array(s);
    vector<int> rank(n);
    for (int i = 0; i < n; i++)
        rank[sa[i]] = i;
    vector<int> lcp(n - 1);
    for (int i = 0, h = 0; i < n; i++) {
        if (rank[i] < n - 1) {
            for (int j = sa[rank[i] + 1]; s[i + h] == s[j + h]; ++h)
                ;
            lcp[rank[i]] = h;
            if (h > 0)
                --h;
        }
    }
    return lcp;
}

// usage example
int main() {
    string s = "abcab";

    vector<int> sa = suffix_array(s);
    for (int v : sa)
        cout << v << " ";
    cout << endl;

    vector<int> lcp = lcp_array(s);
    for (int v : lcp)
        cout << v << " ";
    cout << endl;

    mt19937 rng(1);
    s.clear();
    for (int i = 0; i < 1000'000; ++i) {
        char c = uniform_int_distribution<int>('a', 'd')(rng);
        s.push_back(c);
    }
    auto t1 = chrono::high_resolution_clock::now();
    sa = suffix_array(s);
    auto t2 = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> duration = t2 - t1;
    cout << duration.count() << " ms" << endl;
}
