#include <bits/stdc++.h>

using namespace std;

struct point {
    int x, y;

    bool operator==(const point &p) const { return x == p.x && y == p.y; }
};

struct hasher {
    size_t operator()(const point &p) const { return p.x * 37 + p.y; }
};

int main() {
    unordered_map<point, int, hasher> m;

    m[{1, 2}] = 1;
}
