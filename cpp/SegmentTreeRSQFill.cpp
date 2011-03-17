#include <iostream>
using namespace std;

const int maxn = 200000;
bool isFilled[4 * maxn];
int fillv[4 * maxn];
int tsum[4 * maxn];

void push(int node, int left, int right) {
    if (!isFilled[node])
        return;
    isFilled[node] = false;
    tsum[node] = fillv[node] * (right - left + 1);
    if (left < right) {
        fillv[node * 2] = fillv[node];
        fillv[node * 2 + 1] = fillv[node];
        isFilled[node * 2] = true;
        isFilled[node * 2 + 1] = true;
    }
}

void add(int i, int value, int node = 1, int left = 0, int right = maxn - 1) {
    if (left > i || right < i)
        return;
    push(node, left, right);
    if (left == right) {
        tsum[node] += value;
        return;
    }
    int mid = (left + right) >> 1;
    add(i, value, node * 2, left, mid);
    add(i, value, node * 2 + 1, mid + 1, right);
    tsum[node] = tsum[node * 2] + tsum[node * 2 + 1];
}

int sum(int a, int b, int sumAdd = 0, int node = 1, int left = 0, int right = maxn - 1) {
    if (left > b || right < a)
        return 0;
    push(node, left, right);
    if (left >= a && right <= b)
        return tsum[node];
    int mid = (left + right) >> 1;
    int l = sum(a, b, sumAdd, node * 2, left, mid);
    int r = sum(a, b, sumAdd, node * 2 + 1, mid + 1, right);
    return l + r;
}

int get(int i) {
    return sum(i, i);
}

void set(int i, int value) {
    add(i, -get(i) + value);
}

void fill(int a, int b, int value, int node = 1, int left = 0, int right = maxn - 1) {
    if (left > b || right < a)
        return;
    if (left >= a && right <= b) {
        isFilled[node] = true;
        fillv[node] = value;
        push(node, left, right);
        return;
    }
    int mid = (left + right) >> 1;
    fill(a, b, value, node * 2, left, mid);
    fill(a, b, value, node * 2 + 1, mid + 1, right);
    tsum[node] = tsum[node * 2] + tsum[node * 2 + 1];
}

int main() {
    set(0, 4);
    set(1, 5);
    add(1, 5);
    cout << (4 == get(0)) << endl;
    cout << (10 == get(1)) << endl;

    fill(0, 1, 1);
    cout << (get(0)) << endl;
    cout << (get(1)) << endl;
    cout << (get(2)) << endl;
}
