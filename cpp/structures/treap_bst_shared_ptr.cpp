#include <bits/stdc++.h>

using namespace std;

random_device rd;
default_random_engine rnd(rd());

struct item;

typedef shared_ptr<item> pitem;

struct item {
    int key, prio;
    pitem l, r;

    item(int key) : key(key), prio(rnd()), l(nullptr), r(nullptr) {}

//    ~item() {
//        cout << "item " << key << " disposed" << endl;
//    }
};

void split(const pitem & t, int key, pitem &l, pitem &r) {
    if (!t)
        l = r = nullptr;
    else if (key < t->key)
        split(t->l, key, l, t->l), r = t;
    else
        split(t->r, key, t->r, r), l = t;
}

void merge(pitem &t, pitem l, pitem r) {
    if (!l || !r)
        t = l ? l : r;
    else if (l->prio > r->prio)
        merge(l->r, l->r, r), t = l;
    else
        merge(r->l, l, r->l), t = r;
}

void insert(pitem &t, const pitem &it) {
    if (!t)
        t = it;
    else if (it->prio > t->prio)
        split(t, it->key, it->l, it->r), t = it;
    else
        insert(it->key < t->key ? t->l : t->r, it);
}

void erase(pitem &t, int key) {
    if (t->key == key)
        merge(t, t->l, t->r);
    else
        erase(key < t->key ? t->l : t->r, key);
}

void print(const pitem &t) {
    if (!t)
        return;
    print(t->l);
    cout << t->key << endl;
    print(t->r);
}

// usage example
int main() {
    pitem t1 = nullptr;
    int a1[] = {1, 2};
    for (int &x: a1)
        insert(t1, pitem(new item(x)));

    pitem t2 = nullptr;
    int a2[] = {7, 4, 5};
    for (int &x: a2)
        insert(t2, pitem(new item(x)));

    pitem t = nullptr;
    merge(t, t1, t2);
    print(t);
}
