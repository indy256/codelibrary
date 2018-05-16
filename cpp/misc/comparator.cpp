#include <bits/stdc++.h>

using namespace std;

struct item {
    int x, y;

    bool operator<(const item &o) const {
        return x < o.x || x == o.x && y < o.y;
    }
};

struct item_cmp {
    bool operator()(const item &a, const item &b) {
        return a.x < b.x || a.x == b.x && a.y < b.y;
    }
};

bool cmp(const item &a, const item &b) {
    return a.x < b.x || a.x == b.x && a.y < b.y;
}

auto cmpl = [](const item &a, const item &b) { return a.x < b.x || a.x == b.x && a.y < b.y; };

int main() {
    item a[] = {{2, 3},
                {1, 2}};

    set<item, bool (*)(const item &, const item &)> s(a, a + 2, cmp);
    for (const item &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    sort(a, a + 2, item_cmp());
    sort(a, a + 2, cmp);
    sort(a, a + 2, cmpl);
    cout << a[0].x << " " << a[0].y << endl;
}
