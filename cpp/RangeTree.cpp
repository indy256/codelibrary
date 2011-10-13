#include <algorithm>
#include <climits>
#include <vector>
#include <cstdio>
using namespace std;

#define mp make_pair
#define pii pair<int,int>
#define all(a) a.begin(),a.end()

struct rect {
	int x1, y1, x2, y2, z, num;

	bool operator<(const rect & o) const {
		return z < o.z;
	}
};

bool cmp(const pair<pii, int> &a, const pair<pii, int> &b) {
	if (a.first.second != b.first.second)
		return a.first.second < b.first.second;
	else
		return a.second < b.second;
}

const int MMX = 1 << 17;
vector<pii> tree[2 * MMX];
vector<int> tmin[2 * MMX];
int N;

int power2(int x) {
	int res = 1;
	while (res < x) res *= 2;
	return res;
}

void buildY(int y1, int y2, int nodeX, int nodeY = 1) {
	if (y1 == y2) {
		tmin[nodeX][nodeY] = y1 < tree[nodeX].size() ? tree[nodeX][y1].second : INT_MAX;
	} else {
		int mid = (y1 + y2) >> 1;
		buildY(y1, mid, nodeX, nodeY * 2);
		buildY(mid + 1, y2, nodeX, nodeY * 2 + 1);
		tmin[nodeX][nodeY] = min(tmin[nodeX][nodeY * 2], tmin[nodeX][nodeY * 2 + 1]);
	}
}

void buildX(int x1, int x2, vector<pair<pii, int> > &cur, int nodeX = 1) {
	for (int i = 0; i < cur.size(); i++)
		tree[nodeX].push_back(mp(cur[i].first.second, cur[i].second));

	tmin[nodeX].resize(power2(tree[nodeX].size()) * 2);
	buildY(0, tmin[nodeX].size() / 2 - 1, nodeX);
	if (x1 == x2) return;
	int mid = (x1 + x2) >> 1;
	vector<pair<pii, int> > left, right;

	for (int i = 0; i < cur.size(); i++) {
		if (cur[i].first.first <= mid)
			left.push_back(cur[i]);
		else
			right.push_back(cur[i]);
	}
	buildX(x1, mid, left, nodeX * 2);
	buildX(mid + 1, x2, right, nodeX * 2 + 1);
}

void updateY(int y, int z, int value, int num) {
	y = lower_bound(all(tree[num]), mp(y, z)) - tree[num].begin();
	y += tmin[num].size() / 2;
	for (tmin[num][y] = value; y >>= 1;)
		tmin[num][y] = min(tmin[num][y * 2], tmin[num][y * 2 + 1]);
}

void updateX(int x, int y, int z, int value) {
	x += N;
	for (updateY(y, z, value, x); x >>= 1;) updateY(y, z, value, x);
}

int findY(int y1, int y2, int nodeX) {
	y1 = lower_bound(all(tree[nodeX]), mp(y1, -1)) - tree[nodeX].begin();
	y2 = upper_bound(all(tree[nodeX]), mp(y2, INT_MAX)) - tree[nodeX].begin() - 1;
	int res = INT_MAX;
	int M = tmin[nodeX].size() / 2;
	for (y1 += M, y2 += M; y1 <= y2; y1 = (y1 + 1) >> 1, y2 = (y2 - 1) >> 1) {
		res = min(res, tmin[nodeX][y1]);
		res = min(res, tmin[nodeX][y2]);
	}
	return res;
}

int findX(int x1, int x2, int y1, int y2) {
	int res = INT_MAX;
	for (x1 += N, x2 += N; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
		res = min(res, findY(y1, y2, x1));
		res = min(res, findY(y1, y2, x2));
	}
	return res;
}

int main() {
#ifndef ONLINE_JUDGE    
	freopen("D:/projects/olymp/wikialgo/cpp/input.txt", "r", stdin);
#endif

	int n;
	scanf("%d", &n);
	vector<rect> rects(n);
	for (int i = 0; i < n; i++) {
		scanf("%d%d%d%d%d", &rects[i].x1, &rects[i].x2, &rects[i].y1, &rects[i].y2, &rects[i].z);
		rects[i].num = i;
	}

	sort(all(rects));

	int m;
	scanf("%d", &m);
	vector<pii> points(m);
	vector<int> cx(m);
	for (int i = 0; i < m; i++) {
		scanf("%d%d", &points[i].first, &points[i].second);
		cx[i] = points[i].first;
	}
	sort(all(cx));
	cx.resize(unique(all(cx)) - cx.begin());
	vector<pair<pii, int> > q(m);

	for (int i = 0; i < m; i++) {
		points[i].first = lower_bound(all(cx), points[i].first) - cx.begin();
		q[i] = mp(points[i], i);
	}
	sort(all(q), cmp);

	N = power2(q.size());
	buildX(0, N - 1, q);

	vector<int> res(m);

	for (int i = 0; i < n; i++) {
		int pos = findX(lower_bound(all(cx), rects[i].x1) - cx.begin(), upper_bound(all(cx), rects[i].x2) - cx.begin() - 1, rects[i].y1, rects[i].y2);
		if (pos != INT_MAX) {
			res[pos] = rects[i].num + 1;
			updateX(points[pos].first, points[pos].second, pos, INT_MAX);
		}
	}
	for (int i = 0; i < m; i++) printf("%d\n", res[i]);
}
