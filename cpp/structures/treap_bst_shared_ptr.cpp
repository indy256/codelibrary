#include <bits/stdc++.h>

using namespace std;

mt19937_64 rng(chrono::steady_clock::now().time_since_epoch().count());

struct Treap;

using pTreap = shared_ptr<Treap>;

struct Treap {
    int key;
    long long prio;
    int size;
    pTreap l, r;

    Treap(int key) : key(key), prio(rng()), size(1), l(nullptr), r(nullptr) {}

    void update() { size = 1 + get_size(l) + get_size(r); }

    static int get_size(pTreap node) { return node ? node->size : 0; }

    //    ~item() {
    //        cout << "item " << key << " disposed" << endl;
    //    }
};

void split(pTreap t, int key, pTreap &l, pTreap &r) {
    if (!t)
        l = r = nullptr;
    else if (key < t->key)
        split(t->l, key, l, t->l), r = t, t->update();
    else
        split(t->r, key, t->r, r), l = t, t->update();
}

void merge(pTreap &t, pTreap &l, pTreap &r) {
    if (!l || !r)
        t = l ? l : r;
    else if (l->prio > r->prio)
        merge(l->r, l->r, r), t = l, t->update();
    else
        merge(r->l, l, r->l), t = r, t->update();
}

void insert(pTreap &t, pTreap it) {
    if (!t)
        t = it;
    else if (it->prio > t->prio)
        split(t, it->key, it->l, it->r), t = it, t->update();
    else
        insert(it->key < t->key ? t->l : t->r, it), t->update();
}

void erase(pTreap &t, int key) {
    if (t->key == key)
        merge(t, t->l, t->r);
    else
        erase(key < t->key ? t->l : t->r, key), t->update();
}

int kth(pTreap &t, int k) {
    if (k < Treap::get_size(t->l))
        return kth(t->l, k);
    else if (k > Treap::get_size(t->l))
        return kth(t->r, k - Treap::get_size(t->l) - 1);
    return t->key;
}

void print(pTreap &t) {
    if (!t)
        return;
    print(t->l);
    cout << t->key << endl;
    print(t->r);
}

// usage example
int main() {
    pTreap t1 = nullptr;
    int a1[] = {1, 2};
    for (int x : a1)
        insert(t1, make_shared<Treap>(x));

    pTreap t2 = nullptr;
    int a2[] = {7, 4, 5};
    for (int x : a2)
        insert(t2, make_shared<Treap>(x));

    pTreap t = nullptr;
    merge(t, t1, t2);

    for (int i = 0; i < t->size; ++i) {
        cout << kth(t, i) << endl;
    }
}
