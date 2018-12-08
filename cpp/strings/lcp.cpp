#include <bits/stdc++.h>

using namespace std;

const int maxn = 200000;
int sa[maxn];
int lcp[maxn];
int Rank[maxn];
unsigned char *s;
int n;

void calc_lcp() {
    for (int i = 0; i < n; i++)
        Rank[sa[i]] = i;
    for (int i = 0, h = 0; i < n; i++) {
        if (Rank[i] < n - 1) {
            for (int j = sa[Rank[i] + 1]; s[i + h] == s[j + h]; ++h)
                ;
            lcp[Rank[i]] = h;
            if (h > 0)
                --h;
        }
    }
}

int main() {

}