#include <iostream>
using namespace std;

const int maxn = 200000;
int tsum[4 * maxn];
int tadd[4 * maxn];

void push(int node, int left, int right) {
	tsum[node] += tadd[node] * (right - left + 1);
	if (left < right) {
		tadd[node * 2] += tadd[node];
		tadd[node * 2 + 1] += tadd[node];
	}
	tadd[node] = 0;
}

void pop(int node, int left, int right) {
	int mid = (left + right) >> 1;
	tsum[node] = tsum[node * 2] + tadd[node * 2] * (mid - left + 1);
	tsum[node] += tsum[node * 2 + 1] + tadd[node * 2 + 1] * (right - mid);
}

void add(int a, int b, int value, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left >= a && right <= b) {
		tadd[node] += value;
		return;
	}
	int mid = (left + right) >> 1;
	if (a <= mid) add(a, b, value, node * 2, left, mid);
	if (b > mid) add(a, b, value, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
}

int sum(int a, int b, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left >= a && right <= b) return tsum[node];
	int mid = (left + right) >> 1;
	int res = 0;
	if (a <= mid) res += sum(a, b, node * 2, left, mid);
	if (b > mid) res += sum(a, b, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
	return res;
}

int get(int i, int node = 1, int left = 0, int right = maxn - 1) {
	push(node, left, right);
	if (left == right) return tsum[node];
	int mid = (left + right) >> 1;
	int res = i <= mid ? get(i, node * 2, left, mid) : get(i, node * 2 + 1, mid + 1, right);
	pop(node, left, right);
	return res;
}

void set(int i, int value) {
	add(i, i, -get(i) + value);
}

int main() {
	add(0, 0, 1);
	set(0, 4);
	set(1, 5);
	set(2, 5);
	add(2, 2, 5);
	add(0, 2, 1);

	cout << (5 == get(0)) << endl;
	cout << (22 == sum(0, 2)) << endl;
	cout << (17 == sum(1, 2)) << endl;
}
