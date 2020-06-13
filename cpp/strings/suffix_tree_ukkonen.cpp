#include <bits/stdc++.h>

using namespace std;

// See http://codeforces.ru/blog/entry/16780 for description

const int inf = 1e9;
const int maxn = 1e5;
char s[maxn];
unordered_map<char, int> to[maxn];
int len[maxn], f_pos[maxn], link[maxn];
int node, pos;
int sz = 1, n = 0;

int make_node(int _pos, int _len) {
    f_pos[sz] = _pos;
    len[sz] = _len;
    return sz++;
}

void go_edge() {
    while (pos > len[to[node][s[n - pos]]]) {
        node = to[node][s[n - pos]];
        pos -= len[node];
    }
}

void add_letter(char c) {
    s[n++] = c;
    pos++;
    int last = 0;
    while (pos > 0) {
        go_edge();
        int edge = s[n - pos];
        int &v = to[node][edge];
        int t = s[f_pos[v] + pos - 1];
        if (v == 0) {
            v = make_node(n - pos, inf);
            link[last] = node;
            last = 0;
        } else if (t == c) {
            link[last] = node;
            return;
        } else {
            int u = make_node(f_pos[v], pos - 1);
            to[u][c] = make_node(n - 1, inf);
            to[u][t] = v;
            f_pos[v] += pos - 1;
            len[v] -= pos - 1;
            v = u;
            link[last] = u;
            last = u;
        }
        if (node == 0)
            pos--;
        else
            node = link[node];
    }
}

int main() {
    len[0] = inf;
    string s = "abracadabra";
    int ans = 0;
    for (int i = 0; i < s.size(); i++)
        add_letter(s[i]);
    for (int i = 1; i < sz; i++)
        ans += min((int)s.size() - f_pos[i], len[i]);
    cout << ans << "\n";
}
