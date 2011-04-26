#include <iostream>
#include <map>
using namespace std;

map<int, int> t;
int n;

void add(int i, int value) {
	for (; i < n; i |= i + 1)
		t[i] += value;
}

int sum(int i) {
	int res = 0;
	for (; i >= 0; i -= -~i & ~i)
		if (t.count(i)) res += t[i];
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

// Usage example
int main() {
	n = 4;
	set(0, 4);
	set(1, 5);
	set(2, 5);
	add(2, 5);

	cout << (4 == get(0)) << endl;
	cout << (19 == sum(0, 2)) << endl;
	cout << (15 == sum(1, 2)) << endl;
}
