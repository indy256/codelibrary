#include <iostream>
using namespace std;

const int maxn = 200000;
int t[maxn];

void add(int i, int value) {
	for (; i < maxn; i += (i + 1) & -(i + 1))
		t[i] += value;
}

int sum(int i) {
	int res = 0;
	for (; i >= 0; i -= (i + 1) & -(i + 1))
		res += t[i];
	return res;
}

// sum[a,b]
int sum(int a, int b) {
	return sum(b) - sum(a - 1);
}

int get(int i) {
	return sum(i, i);
}

void set(int i, int value) {
	add(i, -get(i) + value);
}

// Returns min(p|sum[0,p]>=sum)
int lower_bound(int sum) {
	--sum;
	int pos = -1;
	for (int blockSize = 1 << 30; blockSize != 0; blockSize >>= 1) {
		if (blockSize > maxn) continue;
		int nextPos = pos + blockSize;
		if (nextPos < maxn && sum >= t[nextPos]) {
			sum -= t[nextPos];
			pos = nextPos;
		}
	}
	return pos + 1;
}


// Usage example
int main() {
	set(0, 4);
	set(1, 5);
	set(2, 5);
	add(2, 5);

	cout << (4 == get(0)) << endl;
	cout << (19 == sum(0, 2)) << endl;
	cout << (15 == sum(1, 2)) << endl;
	cout << (2 == lower_bound(19)) << endl;
	cout << (maxn == lower_bound(20)) << endl;
}
