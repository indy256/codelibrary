#include <bits/stdc++.h>

using namespace std;

// See https://codeforces.com/blog/entry/17956?#comment-228040 for description

const int MAXLEN = 1e6;
string s;
int pos[MAXLEN], len[MAXLEN], par[MAXLEN];
unordered_map<char, int> to[MAXLEN], link[MAXLEN];
int sz = 2;

void attach(int child, int parent, char c, int child_len) {
    to[parent][c] = child;
    len[child] = child_len;
    par[child] = parent;
}

void extend(char c) {
    int p;
    int i = s.size();
    int old = sz - 1;
    for (p = old; !link[p].count(c); p = par[p])
        i -= len[p];
    int v = link[p][c];
    if (to[v].count(s[i])) {
        int u = to[v][c];
        for (pos[sz] = pos[u] - len[u]; s[pos[sz]] == s[i]; pos[sz] += len[p], i += len[p])
            p = to[p][s[i]];
        attach(sz, v, s[pos[u] - len[u]], len[u] - (pos[u] - pos[sz]));
        attach(u, sz, s[pos[sz]], pos[u] - pos[sz]);
        v = link[p][c] = sz++;
    }
    link[old][c] = sz;
    attach(sz, v, s[i], s.size() - i);
    pos[sz++] = s.size();
}

int main() {
    len[1] = 1;
    pos[1] = 0;
    par[1] = 0;
    for (int c = 0; c <= 255; c++)
        to[0][c] = link[0][c] = 1;
    s = "aabababbbbadcasdf#";
    for (int i = s.size() - 1; i >= 0; i--)
        extend(s[i]);
}
