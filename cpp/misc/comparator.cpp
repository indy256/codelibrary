#include <bits/stdc++.h>

using namespace std;

struct item {
    int x, y;
};

int main() {
    item a[] = {{2, 3},
                {1, 2}};

    auto comparator = [](auto &a, auto &b) { return a.x < b.x || a.x == b.x && a.y < b.y; };

    sort(a, a + 2, comparator);

    set<item, decltype(comparator)> s(a, a + 2, comparator);
    for (const item &it : s) {
        cout << it.x << " " << it.y << endl;
    }

    priority_queue<item, vector<item>, decltype(comparator)> q(a, a + 2, comparator);
    while(!q.empty()) {
        auto item = q.top();
        q.pop();
        cout << item.x << " " << item.y << endl;
    }
}
