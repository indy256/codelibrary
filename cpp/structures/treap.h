#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/treap.html

mt19937_64 rng(chrono::steady_clock::now().time_since_epoch().count());

struct Treap {
    long long node_value;
    long long mx;
    long long sum;
    long long add;

    long long key;  // keys should be unique
    int size;
    long long prio;
    Treap *l, *r;

    Treap(long long key, long long value)
        : node_value(value), mx(value), sum(value), add(0), key(key), size(1), prio(rng()), l(nullptr), r(nullptr) {}

    void apply(long long v) {
        node_value += v;
        mx += v;
        sum += v * size;
        add += v;
    }

    void push() {
        if (add != 0) {
            if (l != nullptr)
                l->apply(add);
            if (r != nullptr)
                r->apply(add);
            add = 0;
        }
    }

    void pull() {
        mx = max(node_value, max(get_mx(l), get_mx(r)));
        sum = node_value + get_sum(l) + get_sum(r);
        size = 1 + get_size(l) + get_size(r);
    }

    static long long get_mx(Treap *root) { return root == nullptr ? numeric_limits<long long>::min() : root->mx; }

    static long long get_sum(Treap *root) { return root == nullptr ? 0 : root->sum; }

    static int get_size(Treap *root) { return root == nullptr ? 0 : root->size; }
};

using pTreap = Treap *;

void split(pTreap t, long long min_right, pTreap &l, pTreap &r) {
    if (!t) {
        l = r = nullptr;
    } else {
        t->push();
        if (t->key >= min_right) {
            split(t->l, min_right, l, t->l);
            r = t;
        } else {
            split(t->r, min_right, t->r, r);
            l = t;
        }
        t->pull();
    }
}

void merge(pTreap &t, pTreap &l, pTreap &r) {
    if (!l || !r) {
        t = l ? l : r;
    } else {
        l->push();
        r->push();
        if (l->prio > r->prio) {
            merge(l->r, l->r, r);
            t = l;
        } else {
            merge(r->l, l, r->l);
            t = r;
        }
        t->pull();
    }
}

void insert(pTreap &t, long long key, long long value) {
    pTreap l, r;
    split(t, key, l, r);
    auto node = new Treap(key, value);
    merge(t, l, node);
    merge(t, t, r);
}

void remove(pTreap &t, long long key) {
    pTreap left1, right1, left2, right2;
    split(t, key, left1, right1);
    split(right1, key + 1, left2, right2);
    delete left2;
    merge(t, left1, right2);
}

void modify(pTreap &t, long long ll, long long rr, long long delta) {
    pTreap left1, right1, left2, right2;
    split(t, rr + 1, left1, right1);
    split(left1, ll, left2, right2);
    if (right2 != nullptr)
        right2->apply(delta);
    merge(t, left2, right2);
    merge(t, t, right1);
}

Treap query(pTreap &t, long long ll, long long rr) {
    pTreap left1, right1, left2, right2;
    split(t, rr + 1, left1, right1);
    split(left1, ll, left2, right2);
    Treap res(0, 0);
    if (right2)
        res = *right2;
    merge(t, left2, right2);
    merge(t, t, right1);
    return res;
}

void clear(pTreap &t) {
    if (!t)
        return;
    clear(t->l);
    clear(t->r);
    delete t;
    t = nullptr;
}

void print(pTreap t) {
    if (!t)
        return;
    print(t->l);
    cout << t->node_value << endl;
    print(t->r);
}
