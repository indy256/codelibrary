#include <iostream>
#include <algorithm>
using namespace std;

const int maxn = 200000;
int tfill[4 * maxn];
int tsum[4 * maxn];

void push(int node, int left, int right) {
	if (tfill[node] == -1)
		return;
	tsum[node] = tfill[node] * (right - left + 1);
	if (left < right) {
		tfill[node * 2] = tfill[node];
		tfill[node * 2 + 1] = tfill[node];
	}
	tfill[node] = -1;
}

void pop(int node, int left, int right) {
	int mid = (left + right) >> 1;
	tsum[node] = (tfill[node * 2] != -1) ? tfill[node * 2] * (mid - left + 1) : tsum[node * 2];
	tsum[node] += (tfill[node * 2 + 1] != -1) ? tfill[node * 2 + 1] * (right - mid) : tsum[node * 2 + 1];
}

void add(int i, int value, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left == right) {
		tsum[node] += value;
		return;
	}
	int mid = (left + right) >> 1;
	if (i <= mid)
		add(i, value, node * 2, left, mid);
	else
		add(i, value, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
}

int sum(int a, int b, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left >= a && right <= b)
		return tsum[node];
	int mid = (left + right) >> 1;
	int res = 0;
	if (a <= mid) res += sum(a, b, node * 2, left, mid);
	if (b > mid) res += sum(a, b, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
	return res;
}

int get(int i) {
	return sum(i, i);
}

void set(int i, int value) {
	add(i, -get(i) + value);
}

void fill(int a, int b, int value, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left >= a && right <= b) {
		tfill[node] = value;
		return;
	}
	int mid = (left + right) >> 1;
	if (a <= mid) fill(a, b, value, node * 2, left, mid);
	if (b > mid) fill(a, b, value, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
}

int main() {
	fill(tfill, tfill + maxn, -1);
	set(0, 4);
	set(1, 5);
	add(1, 5);
	cout << (4 == get(0)) << endl;
	cout << (10 == get(1)) << endl;

	fill(0, 1, 1);
	cout << (1 == get(0)) << endl;
	cout << (1 == get(1)) << endl;
	cout << (0 == get(2)) << endl;
}
