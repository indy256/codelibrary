#include <algorithm>
#include <climits>
#include <vector>
#include <iostream>
using namespace std;

typedef pair<int, int> pii;
typedef vector<pii> vpii;

const int maxn = 100000;
int tx[maxn * 4];
int ty[maxn * 4];
bool divX[maxn * 4];

bool cmpX(const pii &a, const pii &b) {
    return a.first < b.first;
}

bool cmpY(const pii &a, const pii &b) {
    return a.second < b.second;
}

void buildTree(int node, int left, int right, pii points[]) {
    if (left > right)
        return;
    int mid = (left + right) >> 1;

    //sort(points + left, points + right + 1, divX ? cmpX : cmpY);
    int minx = INT_MAX;
    int maxx = INT_MIN;
    int miny = INT_MAX;
    int maxy = INT_MIN;
    for (int i = left; i <= right; i++) {
        minx = min(minx, points[i].first);
        maxx = max(maxx, points[i].first);
        miny = min(miny, points[i].second);
        maxy = max(maxy, points[i].second);
    }
    divX[node] = (maxx - minx) >= (maxy - miny);
    nth_element(points + left, points + mid, points + right + 1, divX[node] ? cmpX : cmpY);

    tx[node] = points[mid].first;
    ty[node] = points[mid].second;
    if (left == right)
        return;
    buildTree(node * 2, left, mid - 1, points);
    buildTree(node * 2 + 1, mid + 1, right, points);
}

long long closestDist;
int closestNode;

void findNearestNeighbour(int node, int left, int right, int x, int y) {
    if (left > right)
        return;
    int dx = x - tx[node];
    int dy = y - ty[node];
    long long d = dx * (long long) dx + dy * (long long) dy;
    if (closestDist > d) {
        closestDist = d;
        closestNode = node;
    }
    if (left == right)
        return;

    int delta = divX[node] ? dx : dy;
    long long delta2 = delta * (long long) delta;
    int mid = (left + right) >> 1;
    int n1 = node << 1;
    int l1 = left;
    int r1 = mid - 1;
    int n2 = node << 1 | 1;
    int l2 = mid + 1;
    int r2 = right;
    if (delta > 0)
        swap(l1, l2), swap(r1, r2), swap(n1, n2);

    findNearestNeighbour(n1, l1, r1, x, y);
    if (delta2 < closestDist)
        findNearestNeighbour(n2, l2, r2, x, y);
}

int findNearestNeighbour(int n, int x, int y) {
    closestDist = LLONG_MAX;
    findNearestNeighbour(1, 0, n - 1, x, y);
    return closestNode;
}

int main() {
    vpii p;
    p.push_back(make_pair(0, 2));
    p.push_back(make_pair(0, 3));
    p.push_back(make_pair(-1, 0));

    p.resize(unique(p.begin(), p.end()) - p.begin());

    int n = p.size();
    buildTree(1, 0, n - 1, &(vpii(p)[0]));
    int res = findNearestNeighbour(n, 0, 0);

    cout << p[res].first << " " << p[res].second << endl;

    return 0;
}
