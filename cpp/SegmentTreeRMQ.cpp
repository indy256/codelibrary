#include <iostream>
#include <climits>
using namespace std;

const int maxn = 200000;
int tmin[4 * maxn];
int t[maxn];

void buildTree(int node = 1, int left = 0, int right = maxn - 1) {
    if (left == right) {
        tmin[node] = t[left];
    } else {
        int mid = (left + right) >> 1;
        buildTree(node * 2, left, mid);
        buildTree(node * 2 + 1, mid + 1, right);
        tmin[node] = min(tmin[node * 2], tmin[node * 2 + 1]);
    }
}

void add(int i, int value, int node = 1, int left = 0, int right = maxn - 1) {
    if (left == right) {
        tmin[node] += value;
        return;
    }
    int mid = (left + right) >> 1;
	if (i <= mid)
		add(i, value, node * 2, left, mid);
	else
		add(i, value, node * 2 + 1, mid + 1, right);
    tmin[node] = min(tmin[node * 2], tmin[node * 2 + 1]);
}

int minv(int a, int b, int node = 1, int left = 0, int right = maxn - 1) {
    if (left >= a && right <= b)
        return tmin[node];
    int mid = (left + right) >> 1;
	int res = INT_MAX;
	if (a <= mid)
		res = min(res, minv(a, b, node * 2, left, mid));
	if (b > mid)
		res = min(res, minv(a, b, node * 2 + 1, mid + 1, right));
    return res;
}

int get(int i) {
    return minv(i, i);
}

void set(int i, int value) {
    add(i, -get(i) + value);
}

int main() {
    t[0] = t[1] = t[2] = 5;
    buildTree();
    set(0, 4);
    set(1, 5);
    add(2, 5);

    cout << (4 == get(0)) << endl;
    cout << (4 == minv(0, 2)) << endl;
    cout << (10 == get(2)) << endl;
}
