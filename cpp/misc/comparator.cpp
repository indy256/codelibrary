#include <bits/stdc++.h>

using namespace std;

struct point {
    int x, y;
};

struct point2 {
    int x, y;

    bool operator<(const point2 &b) const { return x == b.x ? y < b.y : x < b.x; }

    bool operator<(int qx) const { return x < qx; }
};

int main() {
    point a[] = {{2, 3}, {1, 2}};

    auto cmp = [](auto &a, auto &b) { return a.x < b.x || (a.x == b.x && a.y < b.y); };
    sort(a, a + 2, cmp);

    set<point, decltype(cmp)> s(a, a + 2, cmp);
    for (auto &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    point2 b[] = {{2, 3}, {1, 2}};
    set<point2, less<>> s2(b, b + 2);
    for (auto &it : s2) {
        cout << it.x << " " << it.y << endl;
    }
    cout << s2.lower_bound(1)->y << endl;

    map<point, int, decltype(cmp)> m({{point{2, 3}, 1}}, cmp);
    for (auto &entry : m) {
        cout << entry.first.x << " " << entry.first.y << " " << entry.second << endl;
    }

    priority_queue<point, vector<point>, decltype(cmp)> q(a, a + 2, cmp);
    while (!q.empty()) {
        auto item = q.top();
        q.pop();
        cout << item.x << " " << item.y << endl;
    }
}
