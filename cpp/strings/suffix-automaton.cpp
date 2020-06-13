#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/string/suffix-automaton.html

struct state {
    int length;
    int suffLink;
    vector<int> inv_sufflinks;
    int firstPos = -1;
    vector<int> next = vector<int>(128, -1);
};

constexpr int MAXLEN = 100'000;
state st[MAXLEN * 2];
int sz;

void build_suffix_automaton(const string &s) {
    st[0].suffLink = -1;
    int last = 0;
    sz = 1;
    for (int i = 0; i < s.length(); i++) {
        char c = s[i];
        int cur = sz++;
        st[cur].length = i + 1;
        st[cur].firstPos = i;
        int p = last;
        for (; p != -1 && st[p].next[c] == -1; p = st[p].suffLink) {
            st[p].next[c] = cur;
        }
        if (p == -1) {
            st[cur].suffLink = 0;
        } else {
            int q = st[p].next[c];
            if (st[p].length + 1 == st[q].length) {
                st[cur].suffLink = q;
            } else {
                int clone = sz++;
                st[clone].length = st[p].length + 1;
                st[clone].next = st[q].next;
                st[clone].suffLink = st[q].suffLink;
                for (; p != -1 && st[p].next[c] == q; p = st[p].suffLink) {
                    st[p].next[c] = clone;
                }
                st[q].suffLink = clone;
                st[cur].suffLink = clone;
            }
        }
        last = cur;
    }
    for (int i = 1; i < sz; i++) {
        st[st[i].suffLink].inv_sufflinks.push_back(i);
    }
}

// usage example
int main() {
    build_suffix_automaton("ababcc");
}
