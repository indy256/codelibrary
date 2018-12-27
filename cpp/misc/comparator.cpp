#include <bits/stdc++.h>

using namespace std;

struct point {
    int x, y;
};

int main() {
    point a[] = {{2, 3},
                {1, 2}};

    auto cmp = [](auto &a, auto &b) { return a.x < b.x || a.x == b.x && a.y < b.y; };

    sort(a, a + 2, cmp);
    set<point, decltype(cmp)> s(a, a + 2, cmp);
    map<point, int, decltype(cmp)> m({{point{2, 3}, 1}}, cmp);
    priority_queue<point, vector<point>, decltype(cmp)> q(a, a + 2, cmp);

    for (auto &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    for (auto &entry : m) {
        cout << entry.first.x << " " << entry.first.y << " " << entry.second << endl;
    }

    while (!q.empty()) {
        auto item = q.top();
        q.pop();
        cout << item.x << " " << item.y << endl;
    }
}
