#include <bits/stdc++.h>

using namespace std;

// See https://codeforces.com/blog/entry/17956?#comment-228040 for description

const int MAXLEN = 1e6;
string s;
int pos[MAXLEN], len[MAXLEN], par[MAXLEN];
unordered_map<char, int> to[MAXLEN], Link[MAXLEN];
int sz = 2;

void attach(int child, int parent, char c, int child_len) {
    to[parent][c] = child;
    len[child] = child_len;
    par[child] = parent;
}

void extend(char c) {
    int v;
    int i = s.size();
    int old = sz - 1;
    for (v = old; !Link[v].count(c); v = par[v])
        i -= len[v];
    int w = Link[v][c];
    if (to[w].count(s[i])) {
        int u = to[w][c];
        for (pos[sz] = pos[u] - len[u]; s[pos[sz]] == s[i]; pos[sz] += len[v], i += len[v])
            v = to[v][s[i]];
        attach(sz, w, s[pos[u] - len[u]], len[u] - (pos[u] - pos[sz]));
        attach(u, sz, s[pos[sz]], pos[u] - pos[sz]);
        w = Link[v][c] = sz++;
    }
    Link[old][c] = sz;
    attach(sz, w, s[i], s.size() - i);
    pos[sz++] = s.size();
}

void init_tree() {
    len[1] = 1;
    pos[1] = 0;
    par[1] = 0;
    for (int c = 0; c <= 255; c++)
        to[0][c] = Link[0][c] = 1;
}

int main() {
    init_tree();

    s = "aabababbbbadcasdf#";
    for (int i = s.size() - 1; i >= 0; i--)
        extend(s[i]);
}
