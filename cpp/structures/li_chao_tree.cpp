#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/geometry/convex_hull_trick.html

using ftype = long long;

struct Line {
    ftype k, d;

    ftype eval(ftype x) {
        return k * x + d;
    }
};

struct Node {
    Line line;
    Node *left = nullptr;
    Node *right = nullptr;

    Node(Line line) : line(line) {}

    void add_line(Line nline, ftype l, ftype r) {
        ftype m = (l + r) / 2;
        bool left_smaller = nline.eval(l) < line.eval(l);
        bool mid_smaller = nline.eval(m) < line.eval(m);
        if (mid_smaller) {
            swap(line, nline);
        }
        if (r - l == 1) {
            return;
        }
        if (left_smaller != mid_smaller) {
            if (left == nullptr)
                left = new Node(nline);
            else
                left->add_line(nline, l, m);
        } else {
            if (right == nullptr)
                right = new Node(nline);
            else
                right->add_line(nline, m, r);
        }
    }

    ftype get_min(ftype x, ftype l, ftype r) {
        if (r - l > 1) {
            ftype m = (l + r) / 2;
            if (x < m && left != nullptr) {
                return min(line.eval(x), left->get_min(x, l, m));
            }
            if (x >= m && right != nullptr) {
                return min(line.eval(x), right->get_min(x, m, r));
            }
        }
        return line.eval(x);
    }
};

struct LiChaoTree {
    ftype minx;
    ftype maxx;
    Node *root;

    LiChaoTree(ftype minx, ftype maxx) : minx(minx), maxx(maxx) {
        root = new Node({0, numeric_limits<ftype>::max() / 2});
    }

    void add_line(Line line) {
        root->add_line(line, minx, maxx + 1);
    }

    ftype get_min(ftype x) {
        return root->get_min(x, minx, maxx + 1);
    }
};

// usage example
int main() {
    LiChaoTree t = LiChaoTree(1, 1e9);
    t.add_line({1, 3});
    t.add_line({2, 1});
    cout << t.get_min(1) << endl;
}
