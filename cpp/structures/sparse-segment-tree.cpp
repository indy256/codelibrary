#include <bits/stdc++.h>

using namespace std;

struct Node {
    Node *l = nullptr;
    Node *r = nullptr;
    int left;
    int right;
    int nsum;

    Node(int lo, int hi, int val) : left(lo), right(hi), nsum(val) {}

    void add(int pos, int val) {
        if (pos < left || pos > right) {
            return;
        }
        nsum += val;
        if (right - left == 1) {
            return;
        }
        int mid = (left + right) / 2;
        if (pos < mid) {
            if (l == nullptr) {
                l = new Node(left, mid, 0);
            }
            l->add(pos, val);
        } else {
            if (r == nullptr) {
                r = new Node(mid + 1, right, 0);
            }
            r->add(pos, val);
        }
    }

    int sum(int from, int to) {
        if (to < left || right < from) {
            return 0;
        }
        if (from <= left && right <= to) {
            return nsum;
        }
        return (l ? l->sum(from, to) : 0) + (r ? r->sum(from, to) : 0);
    }
};

// usage example
int main() {
    Node t(0, 1000, 0);
    t.add(1, 1);
    t.add(100, 2);
    cout << t.sum(0, 10) << endl;
    cout << t.sum(0, 200) << endl;
}
