#include <bits/stdc++.h>

using namespace std;

struct item {
    int x, y;
};

int main() {
    item a[] = {{2, 3},
                {1, 2}};

    auto cmp = [](auto &a, auto &b) { return a.x < b.x || a.x == b.x && a.y < b.y; };

    sort(a, a + 2, cmp);

    set<item, decltype(cmp)> s(a, a + 2, cmp);
    for (const item &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    priority_queue<item, vector<item>, decltype(cmp)> q(a, a + 2, cmp);
    while(!q.empty()) {
        auto item = q.top();
        q.pop();
        cout << item.x << " " << item.y << endl;
    }
}
