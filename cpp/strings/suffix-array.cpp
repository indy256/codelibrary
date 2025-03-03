#include <bits/stdc++.h>

using namespace std;

// build suffix array in O(n*log(n))
vector<int> suffix_array(const string& S) {
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

#define IS_VALID_INDEX(i, h, j, n) (i + h < n && j + h < n)

// https://en.wikipedia.org/wiki/LCP_array
vector<int> lcp_array(const string& s) {
    int n = s.size();
    vector<int> sa = suffix_array(s);
    vector<int> rank(n);
    for (int i = 0; i < n; i++)
        rank[sa[i]] = i;
    vector<int> lcp(n - 1);
    for (int i = 0, h = 0; i < n; i++) {
        if (rank[i] < n - 1) {
            for (int j = sa[rank[i] + 1]; IS_VALID_INDEX(i, h, j, n) && s[i + h] == s[j + h]; ++h)
                ;
            lcp[rank[i]] = h;
            if (h > 0)
                --h;
        }
    }
    return lcp;
}

void test_lcp_array() {
    // Test 1: Simple case with no common prefixes
    string s1 = "abcd";
    vector<int> lcp1 = lcp_array(s1);
    cout << "Test 1 - Expected: [], Got: ";
    for (int v : lcp1)
        cout << v << " ";
    cout << endl;  // Expected output: []

    // Test 2: Simple case with repeated substring
    string s2 = "abab";
    vector<int> lcp2 = lcp_array(s2);
    cout << "Test 2 - Expected: [2, 0, 1], Got: ";
    for (int v : lcp2)
        cout << v << " ";
    cout << endl;  // Expected output: [2, 0, 1]

    // Test 3: String with full repetition // is test correct?
    string s3 = "aaaa";
    vector<int> lcp3 = lcp_array(s3);
    cout << "Test 3 - Expected: [3, 2, 1], Got: ";
    for (int v : lcp3)
        cout << v << " ";
    cout << endl;  // Expected output: [3, 2, 1]

    // Test 4: String with unique characters
    string s4 = "abcdef";
    vector<int> lcp4 = lcp_array(s4);
    cout << "Test 4 - Expected: [], Got: ";
    for (int v : lcp4)
        cout << v << " ";
    cout << endl;  // Expected output: []

    // Test 5: Edge case: Empty string
    // string s5 = "";
    // vector<int> lcp5 = lcp_array(s5);
    // cout << "Test 5 - Expected: [], Got: ";
    // for (int v : lcp5)
    //     cout << v << " ";
    // cout << endl;  // Expected output: []

    // Test 6: Single character string
    string s6 = "a";
    vector<int> lcp6 = lcp_array(s6);
    cout << "Test 6 - Expected: [], Got: ";
    for (int v : lcp6)
        cout << v << " ";
    cout << endl;  // Expected output: []
}

// usage example
int main() {
    test_lcp_array();

    cout << "\nExample test for 'abcab'" << endl;
    string s = "abcab";

    vector<int> sa = suffix_array(s);
    for (int v : sa)
        cout << v << " ";
    cout << endl;

    vector<int> lcp = lcp_array(s);
    for (int v : lcp)
        cout << v << " ";
    cout << endl;

    cout << "\nExample test for 'banana'" << endl;
    string s2 = "banana";

    vector<int> sa2 = suffix_array(s2);
    for (int v : sa2)
        cout << v << " ";
    cout << endl;

    vector<int> lcp2 = lcp_array(s2);
    for (int v : lcp2)
        cout << v << " ";
    cout << endl << endl;

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