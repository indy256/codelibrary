#include <bits/stdc++.h>

using namespace std;
#ifdef _MSC_VER
int __builtin_ctzll(unsigned long long x) {
    int bit = 0;
    while (bit < 64 && (x & (1LL << bit)) == 0)
        ++bit;
    return bit;
}
int __builtin_popcountll(unsigned long long x) {
    int bits = 0;
    for (; x; x &= x - 1, ++bits)
        ;
    return bits;
}
#endif

using ll = long long;

int ctz(ll x) {
    return x == 0 ? 64 : __builtin_ctzll(x);
}

// maximum independent set in O(3^(n/3))
int mis(const vector<ll> &g, ll unused) {
    if (unused == 0)
        return 0;
    int v = -1;
    for (int u = __builtin_ctzll(unused); u < g.size(); u += ctz(unused >> (u + 1)) + 1)
        if (v == -1 || __builtin_popcountll(g[v] & unused) > __builtin_popcountll(g[u] & unused))
            v = u;
    int res = 0;
    ll nv = g[v] & unused;
    for (int y = __builtin_ctzll(nv); y < g.size(); y += ctz(nv >> (y + 1)) + 1)
        res = max(res, 1 + mis(g, unused & ~g[y]));
    return res;
}

// usage example
int main() {
    vector<ll> g{0b110, 0b101, 0b011};
    for (int i = 0; i < g.size(); ++i) {
        g[i] |= 1LL << i;
    }
    cout << mis(g, (1LL << g.size()) - 1) << endl;
}
