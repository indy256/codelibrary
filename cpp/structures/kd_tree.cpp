#include <bits/stdc++.h>

using namespace std;

typedef pair<int, int> pii;

const int maxn = 100000;
int tx[maxn];
int ty[maxn];
bool divX[maxn];

void buildTree(int left, int right, pii points[]) {
    if (left >= right)
        return;
    int mid = (left + right) >> 1;

    //sort(points + left, points + right + 1, divX ? cmpX : cmpY);
    int minx = INT_MAX;
    int maxx = INT_MIN;
    int miny = INT_MAX;
    int maxy = INT_MIN;
    for (int i = left; i < right; i++) {
        minx = min(minx, points[i].first);
        maxx = max(maxx, points[i].first);
        miny = min(miny, points[i].second);
        maxy = max(maxy, points[i].second);
    }
    divX[mid] = (maxx - minx) >= (maxy - miny);
    auto cmpX = [](const pii &a, const pii &b) { return a.first < b.first; };
    auto cmpY = [](const pii &a, const pii &b) { return a.second < b.second; };
    nth_element(points + left, points + mid, points + right, divX[mid] ? cmpX : cmpY);

    tx[mid] = points[mid].first;
    ty[mid] = points[mid].second;

    if (left + 1 == right)
        return;
    buildTree(left, mid, points);
    buildTree(mid + 1, right, points);
}

long long closestDist;
int closestNode;

void findNearestNeighbour(int left, int right, int x, int y) {
    if (left >= right)
        return;
    int mid = (left + right) >> 1;
    int dx = x - tx[mid];
    int dy = y - ty[mid];
    long long d = dx * (long long) dx + dy * (long long) dy;
    if (closestDist > d && d) {
        closestDist = d;
        closestNode = mid;
    }
    if (left + 1 == right)
        return;

    int delta = divX[mid] ? dx : dy;
    long long delta2 = delta * (long long) delta;
    int l1 = left;
    int r1 = mid;
    int l2 = mid + 1;
    int r2 = right;
    if (delta > 0)
        swap(l1, l2), swap(r1, r2);

    findNearestNeighbour(l1, r1, x, y);
    if (delta2 < closestDist)
        findNearestNeighbour(l2, r2, x, y);
}

int findNearestNeighbour(int n, int x, int y) {
    closestDist = LLONG_MAX;
    findNearestNeighbour(0, n, x, y);
    return closestNode;
}

// usage example
int main() {
    vector<pii> p;
    p.emplace_back(0, 2);
    p.emplace_back(0, 3);
    p.emplace_back(-1, 0);

    p.resize(unique(p.begin(), p.end()) - p.begin());

    int n = p.size();
    buildTree(0, n - 1, &p[0]);
    int res = findNearestNeighbour(n, 0, 0);

    cout << p[res].first << " " << p[res].second << endl;

    return 0;
}
