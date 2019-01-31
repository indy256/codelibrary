#include <bits/stdc++.h>

using namespace std;

// solves a^x = b (mod m)
// (a,m)=1
int discrete_log(int a, int b, int m) {
    int n = (int) sqrt(m) + 1;

    int an = 1;
    for (int i = 0; i < n; ++i)
        an = ((long long) an * a) % m;

    unordered_map<int, int> vals;
    for (int i = 1, cur = an; i <= n; ++i) {
        if (!vals.count(cur))
            vals[cur] = i;
        cur = ((long long) cur * an) % m;
    }

    for (int i = 0, cur = b; i <= n; ++i) {
        if (vals.count(cur)) {
            int res = (long long) vals[cur] * n - i;
            if (res < m)
                return res;
        }
        cur = ((long long) cur * a) % m;
    }
    return -1;
}

// usage example
int main() {
    cout << discrete_log(2, 3, 5) << endl;
}
