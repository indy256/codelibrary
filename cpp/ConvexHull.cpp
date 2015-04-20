#include <algorithm>
#include <iostream>

using namespace std;

typedef pair<long long, long long> point;

long long cross(const point &a, const point &b, const point &c) {
	return (b.first - a.first) * (c.second - a.second) - (b.second - a.second) * (c.first - a.first);
}

vector<point> convexHull(vector<point> points) {
	if (points.size() <= 1)
		return points;
	sort(points.begin(), points.end());
	vector<point> h;
	for (auto p: points) {
		while (h.size() >= 2 && cross(h.end()[-2], h.back(), p) >= 0)
			h.pop_back();
		h.push_back(p);
	}
	reverse(points.begin(), points.end());
	int upper = h.size();
	for (auto p: points) {
		while (h.size() > upper && cross(h.end()[-2], h.back(), p) >= 0)
			h.pop_back();
		h.push_back(p);
	}
	h.resize(h.size() - 1 - (h[0] == h[1]));
	return h;
}

// Usage example
int main() {
	vector<point> hull1 = convexHull((vector<point>) {point(0, 0), point(3, 0), point(0, 3), point(1, 1)});
	cout << (3 == hull1.size()) << endl;
	vector<point> hull2 = convexHull((vector<point>) {point(0, 0), point(0, 0)});
	cout << (1 == hull2.size()) << endl;
}
