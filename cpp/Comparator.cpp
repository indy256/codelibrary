#include <algorithm>
#include <set>
#include <iostream>
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

int main() {
    item a[] = { { 2, 3 }, { 1, 2 } };
    //typedef set<item, bool(*)(const item&, const item&)> myset;
    //myset s(a, a + 2, cmp);
    //typedef set<item, item_cmp> myset;
    typedef set<item> myset;
    myset s(a, a + 2);
    for (myset::iterator it = s.begin(); it != s.end(); it++) {
        cout << it->x << " " << it->y << endl;
    }

    sort(a, a + 2, cmp);
    sort(a, a + 2, item_cmp());
    cout << a[0].x << " " << a[0].y << endl;
}
