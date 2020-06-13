#include <bits/stdc++.h>

using namespace std;

struct wavelet_tree {
    int lo, hi;
    wavelet_tree *l = nullptr;
    wavelet_tree *r = nullptr;
    vector<int> b;

    static wavelet_tree create(int *from, int *to) {
        int min = *min_element(from, to);
        int max = *max_element(from, to);
        return wavelet_tree(from, to, min, max);
    }

    // nos are in range [x,y]
    // array indices are [from, to)
    wavelet_tree(int *from, int *to, int x, int y) {
        lo = x;
        hi = y;
        if (lo == hi || from >= to)
            return;
        int mid = (lo + hi) / 2;
        auto f = [mid](int x) { return x <= mid; };
        b.reserve(to - from + 1);
        b.push_back(0);
        for (auto it = from; it != to; it++) {
            b.push_back(b.back() + f(*it));
        }
        // see how lambda function is used here
        auto pivot = stable_partition(from, to, f);
        l = new wavelet_tree(from, pivot, lo, mid);
        r = new wavelet_tree(pivot, to, mid + 1, hi);
    }

    // kth smallest element in [l, r]
    int kth(int l, int r, int k) {
        if (l > r)
            return 0;
        if (lo == hi)
            return lo;
        int inLeft = b[r] - b[l - 1];
        int lb = b[l - 1];  // amt of nos in first (l-1) nos that go in left
        int rb = b[r];      // amt of nos in first (r) nos that go in left
        return k <= inLeft ? this->l->kth(lb + 1, rb, k) : this->r->kth(l - lb, r - rb, k - inLeft);
    }

    // count of nos in [l, r] Less than or equal to k
    int LTE(int l, int r, int k) {
        if (l > r || k < lo)
            return 0;
        if (hi <= k)
            return r - l + 1;
        int lb = b[l - 1];
        int rb = b[r];
        return this->l->LTE(lb + 1, rb, k) + this->r->LTE(l - lb, r - rb, k);
    }

    // count of nos in [l, r] equal to k
    int count(int l, int r, int k) {
        if (l > r || k < lo || k > hi)
            return 0;
        if (lo == hi)
            return r - l + 1;
        int lb = b[l - 1];
        int rb = b[r];
        int mid = (lo + hi) / 2;
        return k <= mid ? this->l->count(lb + 1, rb, k) : this->r->count(l - lb, r - rb, k);
    }

    ~wavelet_tree() {
        delete l;
        delete r;
    }
};

// usage example
int main() {
    int a[] = {3, 1, 4, 2};
    wavelet_tree wtree = wavelet_tree::create(a, a + 4);

    cout << wtree.kth(1, 3, 2) << endl;
    cout << wtree.LTE(1, 3, 2) << endl;
    cout << wtree.count(1, 4, 2) << endl;
}
