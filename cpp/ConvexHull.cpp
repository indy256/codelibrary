#include <algorithm>
#include <iostream>

using namespace std;

typedef pair<long long, long long> point;

long long cross(const point &a, const point &b, const point &c) {
	return (b.first - a.first) * (c.second - a.second) - (b.second - a.second) * (c.first - a.first);
}

vector<point> convexHull(vector<point> p) {
	int n = p.size();
	if (n <= 1)
		return p;
	sort(p.begin(), p.end());
	vector<point> q;
	for (int i = 0; i < n; q.push_back(p[i++]))
		while (q.size() >= 2 && cross(q.end()[-2], q.back(), p[i]) >= 0)
			q.pop_back();
	for (int i = n - 2, t = q.size(); i >= 0; q.push_back(p[i--]))
		while (q.size() > t && cross(q.end()[-2], q.back(), p[i]) >= 0)
			q.pop_back();
	q.resize(q.size() - 1 - (q[0] == q[1]));
	return q;
}

int main() {
	vector<point> points = {point(0, 0), point(3, 0), point(0, 3), point(1, 1)};
	vector<point> hull = convexHull(points);
	cout << (3 == hull.size()) << endl;
}
