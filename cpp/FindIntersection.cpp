#include <algorithm>
#include <vector>
#include <set>
using namespace std;

typedef pair<int, int> pii;

int cross(int ax, int ay, int bx, int by, int cx, int cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
}

int cross(pii a, pii b, pii c) {
    return cross(a.first, a.second, b.first, b.second, c.first, c.second);
}

class segment {
    public:
    pii a, b;
    int id;
    segment(pii a, pii b, int id) :
        a(a), b(b), id(id) {
    }
    bool operator<(const segment &o) const {
        if (a.first < o.a.first) {
            int s = cross(a, b, o.a);
            return (s > 0 || s == 0 && a.second < o.a.second);
        } else {
            int s = cross(o.a, o.b, a);
            return (s < 0 || s == 0 && a.second < o.a.second);
        }
        return a.second < o.a.second;
    }
};

bool intersect(segment s1, segment s2) {
    int x1 = s1.a.first, y1 = s1.a.second, x2 = s1.b.first, y2 = s1.b.second;
    int x3 = s2.a.first, y3 = s2.a.second, x4 = s2.b.first, y4 = s2.b.second;
    if (max(x1, x2) < min(x3, x4) || max(x3, x4) < min(x1, x2) || max(y1, y2) < min(y3, y4) || max(y3, y4) < min(y1, y2)) {
        return false;
    }
    int z1 = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
    int z2 = (x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1);
    if (z1 < 0 && z2 < 0 || z1 > 0 && z2 > 0) {
        return false;
    }
    int z3 = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
    int z4 = (x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3);
    if (z3 < 0 && z4 < 0 || z3 > 0 && z4 > 0) {
        return false;
    }
    return true;
}

class event {
    public:
    pii p;
    int id;
    int type;
    event(pii p, int id, int type) :
        p(p), id(id), type(type) {
    }
    bool operator<(const event &o) const {
        return p.first < o.p.first || p.first == o.p.first && (type > o.type || type == o.type && p.second < o.p.second);
    }
};

pii findIntersection(vector<segment> a) {
    int n = a.size();
    vector<event> e;
    for (int i = 0; i < n; ++i) {
        if (a[i].a > a[i].b)
            swap(a[i].a, a[i].b);
        e.push_back(event(a[i].a, i, 1));
        e.push_back(event(a[i].b, i, -1));
    }
    sort(e.begin(), e.end());

    set<segment> q;

    for (int i = 0; i < n * 2; ++i) {
        int id = e[i].id;
        if (e[i].type == 1) {
            set<segment>::iterator it = q.lower_bound(a[id]);
            if (it != q.end() && intersect(*it, a[id]))
                return make_pair(it->id, a[id].id);
            if (it != q.begin() && intersect(*--it, a[id]))
                return make_pair(it->id, a[id].id);
            q.insert(a[id]);
        } else {
            set<segment>::iterator it = q.lower_bound(a[id]), next = it, prev = it;
            if (it != q.begin() && it != --q.end()) {
                ++next, --prev;
                if (intersect(*next, *prev))
                    return make_pair(next->id, prev->id);
            }
            q.erase(it);
        }
    }
    return make_pair(-1, -1);
}

int main() {
}
