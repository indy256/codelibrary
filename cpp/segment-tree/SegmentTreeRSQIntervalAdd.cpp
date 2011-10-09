#include <iostream>
using namespace std;

const int maxn = 200000;
int tsum[4 * maxn], tadd[4 * maxn];

void add(int a, int b, int value, int node = 1, int left = 0, int right = maxn - 1) {
    if (left > b || right < a)
        return;
    if (left >= a && right <= b) {
        tadd[node] += value;
        return;
    }
    int mid = (left + right) >> 1;
    add(a, b, value, node * 2, left, mid);
    add(a, b, value, node * 2 + 1, mid + 1, right);
    int l = tsum[node * 2] + tadd[node * 2] * (mid - left + 1);
    int r = tsum[node * 2 + 1] + tadd[node * 2 + 1] * (right - mid);
    tsum[node] = l + r;
}

int sum(int a, int b, int sumAdd = 0, int node = 1, int left = 0, int right = maxn - 1) {
    if (left > b || right < a)
        return 0;
    sumAdd += tadd[node];
    if (left >= a && right <= b)
        return tsum[node] + sumAdd * (right - left + 1);
    int mid = (left + right) >> 1;
    int l = sum(a, b, sumAdd, node * 2, left, mid);
    int r = sum(a, b, sumAdd, node * 2 + 1, mid + 1, right);
    return l + r;
}

int get(int i, int sumAdd = 0, int node = 1, int left = 0, int right = maxn - 1) {
    sumAdd += tadd[node];
    if (left == right)
        return tsum[node] + sumAdd;
    int mid = (left + right) >> 1;
    if (i <= mid)
        return get(i, sumAdd, node * 2, left, mid);
    else
        return get(i, sumAdd, node * 2 + 1, mid + 1, right);
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
