#include <iostream>
#include <climits>
#include <vector>
using namespace std;

// specific code
const int maxn = 200000;

const int INIT_VALUE = 0;
const int NEUTRAL_VALUE = INT_MIN;
const int NEUTRAL_DELTA = 0;

int joinValues(int leftValue, int rightValue) {
	return max(leftValue, rightValue);
}

int joinDeltas(int oldDelta, int newDelta) {
	return oldDelta + newDelta;
}

int joinValueWithDelta(int value, int delta, int length) {
	return value + delta;
}

// generic code
int n;
int value[4 * maxn];
int delta[4 * maxn]; // affects only child roots

void init(int root, int left, int right) {
	if (left == right) {
		value[root] = INIT_VALUE;
		delta[root] = NEUTRAL_DELTA;
	} else {
		init(2 * root + 1, left, (left + right) / 2);
		init(2 * root + 2, (left + right) / 2 + 1, right);
		value[root] = joinValues(value[2 * root + 1], value[2 * root + 2]);
		delta[root] = NEUTRAL_DELTA;
	}
}

void init(int n) {
	::n = n;
	init(0, 0, n - 1);
}

void pushDelta(int root, int left, int right) {
	delta[2 * root + 1] = joinDeltas(delta[2 * root + 1], delta[root]);
	delta[2 * root + 2] = joinDeltas(delta[2 * root + 2], delta[root]);
	int middle = (left + right) / 2;
	value[2 * root + 1] = joinValueWithDelta(value[2 * root + 1], delta[root], middle - left + 1);
	value[2 * root + 2] = joinValueWithDelta(value[2 * root + 2], delta[root], right - middle);
	delta[root] = NEUTRAL_DELTA;
}

int query(int a, int b, int root, int left, int right) {
	if (a > right || b < left)
		return NEUTRAL_VALUE;
	if (a <= left && right <= b)
		return value[root];
	pushDelta(root, left, right);
	return joinValues(query(a, b, root * 2 + 1, left, (left + right) / 2),
		query(a, b, root * 2 + 2, (left + right) / 2 + 1, right));
}

int query(int a, int b) {
	return query(a, b, 0, 0, n - 1);
}

void modify(int a, int b, int delta, int root, int left, int right) {
	if (a > right || b < left)
		return;
	if (a <= left && right <= b) {
		::delta[root] = joinDeltas(::delta[root], delta);
		value[root] = joinValueWithDelta(value[root], delta, right - left + 1);
		return;
	}
	pushDelta(root, left, right);
	modify(a, b, delta, 2 * root + 1, left, (left + right) / 2);
	modify(a, b, delta, 2 * root + 2, (left + right) / 2 + 1, right);
	value[root] = joinValues(value[2 * root + 1], value[2 * root + 2]);
}

void modify(int a, int b, int delta) {
	modify(a, b, delta, 0, 0, n - 1);
}

// Random test
int main() {
	for (int step = 0; step < 1000; step++) {
		int n = rand() % 50 + 1;
		vector<int> x(n, INIT_VALUE);
		init(n);
		for (int i = 0; i < 1000; i++) {
			int b = rand() % n;
			int a = rand() % (b + 1);
			int cmd = rand() % 3;
			if (cmd == 0) {
				int delta = rand() % 100 - 50;
				modify(a, b, delta);
				for (int j = a; j <= b; j++)
					x[j] = joinValueWithDelta(x[j], delta, 1);
			} else if (cmd == 1) {
				int res1 = query(a, b);
				int res2 = x[a];
				for (int j = a + 1; j <= b; j++)
					res2 = joinValues(res2, x[j]);
				if (res1 != res2) {
					cout << "error" << endl;
					return 0;
				}
			} else {
				for (int j = 0; j < n; j++) {
					if (query(j, j) != x[j]) {
						cout << "error" << endl;
						return 0;
					}
				}
			}
		}
	}
	cout << "Test passed" << endl;
}
