#include <bits/stdc++.h>

using namespace std;

using pii = pair<int, int>;

const int maxn = 100'000;
int tx[maxn];
int ty[maxn];
bool divX[maxn];

void build_tree(int left, int right, pii *points) {
    if (left >= right)
        return;
    int mid = (left + right) >> 1;

    // sort(points + left, points + right + 1, divX ? cmpX : cmpY);
    int minx = numeric_limits<int>::max();
    int maxx = numeric_limits<int>::min();
    int miny = numeric_limits<int>::max();
    int maxy = numeric_limits<int>::min();
    for (int i = left; i < right; i++) {
        minx = min(minx, points[i].first);
        maxx = max(maxx, points[i].first);
        miny = min(miny, points[i].second);
        maxy = max(maxy, points[i].second);
    }
    divX[mid] = (maxx - minx) >= (maxy - miny);
    bool (*cmpX)(pii, pii) = [](pii a, pii b) { return a.first < b.first; };
    bool (*cmpY)(pii, pii) = [](pii a, pii b) { return a.second < b.second; };
    nth_element(points + left, points + mid, points + right, divX[mid] ? cmpX : cmpY);

    tx[mid] = points[mid].first;
    ty[mid] = points[mid].second;

    if (left + 1 == right)
        return;
    build_tree(left, mid, points);
    build_tree(mid + 1, right, points);
}

long long closest_dist;
int closest_node;

void find_nearest_neighbour(int left, int right, int x, int y) {
    if (left >= right)
        return;
    int mid = (left + right) >> 1;
    int dx = x - tx[mid];
    int dy = y - ty[mid];
    long long d = dx * (long long)dx + dy * (long long)dy;
    if (closest_dist > d && d) {
        closest_dist = d;
        closest_node = mid;
    }
    if (left + 1 == right)
        return;

    int delta = divX[mid] ? dx : dy;
    long long delta2 = delta * (long long)delta;
    int l1 = left;
    int r1 = mid;
    int l2 = mid + 1;
    int r2 = right;
    if (delta > 0)
        swap(l1, l2), swap(r1, r2);

    find_nearest_neighbour(l1, r1, x, y);
    if (delta2 < closest_dist)
        find_nearest_neighbour(l2, r2, x, y);
}

int find_nearest_neighbour(int n, int x, int y) {
    closest_dist = LLONG_MAX;
    find_nearest_neighbour(0, n, x, y);
    return closest_node;
}

// usage example
int main() {
    vector<pii> p;
    p.emplace_back(0, 2);
    p.emplace_back(0, 3);
    p.emplace_back(-1, 0);

    p.resize(unique(p.begin(), p.end()) - p.begin());

    int n = p.size();
    build_tree(0, n - 1, &p[0]);
    int res = find_nearest_neighbour(n, 0, 0);

    cout << p[res].first << " " << p[res].second << endl;

    return 0;
}
