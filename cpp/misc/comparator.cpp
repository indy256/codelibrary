#include <bits/stdc++.h>

using namespace std;

struct item {
    int x, y;
};

int main() {
    item a[] = {{2, 3},
                {1, 2}};

    auto cmp = [](auto &a, auto &b) { return a.x < b.x || a.x == b.x && a.y < b.y; };

    set<item, decltype(cmp)> s(a, a + 2, cmp);
    for (const item &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    sort(a, a + 2, cmp);

    cout << a[0].x << " " << a[0].y << endl;
}
