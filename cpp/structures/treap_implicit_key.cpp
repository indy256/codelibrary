#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/treap.html

mt19937_64 rng(chrono::steady_clock::now().time_since_epoch().count());

struct Node {
    long long node_value;
    long long mx;
    long long sum;
    long long add;

    int size;
    long long prio;
    Node *l, *r;


    Node(long long value) : node_value(value), mx(value), sum(value), add(0), size(1), prio(rng()),
                            l(nullptr), r(nullptr) {}

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

    static long long get_mx(Node *root) {
        return root == nullptr ? numeric_limits<long long>::min() : root->mx;
    }

    static long long get_sum(Node *root) {
        return root == nullptr ? 0 : root->sum;
    }

    static int get_size(Node *root) {
        return root == nullptr ? 0 : root->size;
    }
};

using pNode = Node *;

void split(pNode t, int min_right, pNode &l, pNode &r) {
    if (!t) {
        l = r = nullptr;
    } else {
        t->push();
        if (Node::get_size(t->l) >= min_right) {
            split(t->l, min_right, l, t->l);
            r = t;
        } else {
            split(t->r, min_right - Node::get_size(t->l) - 1, t->r, r);
            l = t;
        }
        t->pull();
    }
}

void merge(pNode &t, pNode &l, pNode &r) {
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

void insert(pNode &t, int index, long long value) {
    pNode l, r;
    split(t, index, l, r);
    auto node = new Node(value);
    merge(t, l, node);
    merge(t, t, r);
}

void remove(pNode &t, int index) {
    pNode left1, right1, left2, right2;
    split(t, index, left1, right1);
    split(right1, 1, left2, right2);
    delete left2;
    merge(t, left1, right2);
}

void modify(pNode &t, int ll, int rr, long long delta) {
    pNode left1, right1, left2, right2;
    split(t, rr + 1, left1, right1);
    split(left1, ll, left2, right2);
    right2->apply(delta);
    merge(t, left2, right2);
    merge(t, t, right1);
}

Node query(pNode &t, int ll, int rr) {
    pNode left1, right1, left2, right2;
    split(t, rr + 1, left1, right1);
    split(left1, ll, left2, right2);
    Node res = *right2;
    merge(t, left2, right2);
    merge(t, t, right1);
    return res;
}

int find_first(pNode t, int ll, int rr, const function<bool(const Node &)> &f, int l, int r) {
    if (ll <= l && r <= rr && !f(*t)) {
        return -1;
    }
    if (l == r) {
        return l;
    }
    t->push();
    int m = Node::get_size(t->l);
    int res = -1;
    if (ll < m) {
        res = find_first(t->l, ll, rr, f, l, l + m - 1);
    }
    if (res == -1) {
        auto single = new Node(0);
        single->size = 1;
        single->apply(t->node_value);
        res = find_first(single, ll, rr, f, l + m, l + m);
    }
    if (rr > m && res == -1) {
        res = find_first(t->r, ll, rr, f, l + m + 1, r);
    }
    t->pull();
    return res;
}

// calls all FALSE elements to the left of the sought position exactly once
int find_first(pNode t, int ll, int rr, const function<bool(const Node &)> &f) {
    assert(0 <= ll && ll <= rr && rr <= Node::get_size(t) - 1);
    return find_first(t, ll, rr, f, 0, Node::get_size(t) - 1);
}

// Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
int sum_lower_bound(pNode t, int ll, int rr, long long sum) {
    long long sumSoFar = 0;
    return find_first(t, ll, rr,
                      [&](const Node &node) {
                          if (sumSoFar + node.sum >= sum) return true;
                          sumSoFar += node.sum;
                          return false;
                      });
}

void print(pNode t) {
    if (!t)
        return;
    print(t->l);
    cout << t->node_value << endl;
    print(t->r);
}

// usage example
int main() {
    pNode t = nullptr;
    int pos = 0;
    for (int a: {1, 2, 7, 4, 5})
        insert(t, pos++, a);
    int n = t->size;
    for (int i = 0; i < n; ++i)
        cout << query(t, i, i).node_value << endl;
    modify(t, 1, 3, 10);
    for (int i = 0; i < n; ++i)
        cout << query(t, i, i).node_value << endl;
    for (int i = 0; i < n; ++i)
        remove(t, 0);
    cout << Node::get_size(t) << endl;

    for (int v : {2, 1, 10, 20}) {
        insert(t, Node::get_size(t), v);
    }
    cout << (2 == sum_lower_bound(t, 0, Node::get_size(t) - 1, 12));
}
