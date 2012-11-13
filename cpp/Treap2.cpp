#include <iostream>
#include <climits>
#include <algorithm>
#include <vector>
using namespace std;

struct node {
    int cnt;
    int prio;
    int value;
    int maxv;
    node *l;
    node *r;
};

const int MAXN = 200000;

node treap[MAXN];
int t_cnt;
node *root;
vector<int> rnd;

int cnt(node *p) {
    return p ? p->cnt : 0;
}

int maxv(node *p) {
    return p ? p->maxv : INT_MIN;
}

void update(node *p) {
    p->cnt = 1 + cnt(p->l) + cnt(p->r);
    p->maxv = max(p->value, max(maxv(p->l), maxv(p->r)));
}

void merge(node *&t, node *l, node *r) {
    if (!l)
        t = r;
    else if (!r)
        t = l;
    else if (l->prio < r->prio)
        merge(l->r, l->r, r), t = l;
    else
        merge(r->l, l, r->l), t = r;
    update(t);
}

void split(node *t, node *&l, node *&r, int key) {
    if (!t) {
        l = r = NULL;
        return;
    } else if (key <= cnt(t->l))
        split(t->l, l, t->l, key), r = t;
    else
        split(t->r, t->r, r, key - cnt(t->l) - 1), l = t;
    update(t);
}

int get(node *&t, int index) {
    if (index < cnt(t->l))
        return get(t->l, index);
    else if (index > cnt(t->l))
        return get(t->r, index - cnt(t->l) - 1);
    return t->value;
}

int get(int index) {
    return get(root, index);
}

void add(node *&t, node *it, int index) {
    if (!t)
        t = it;
    else if (it->prio < t->prio)
        split(t, it->l, it->r, index), t = it;
    else if (index <= cnt(t->l))
        add(t->l, it, index);
    else
        add(t->r, it, index - cnt(t->l) - 1);
    update(t);
}

void add(int index, int value) {
    node *it = &treap[t_cnt];
    it->value = value;
    it->prio = rnd[t_cnt];
    //it->prio = (rand()<<15) + rand();
    ++t_cnt;
    add(root, it, index);
}

void remove(node *&t, int index) {
    if (index == cnt(t->l))
        merge(t, t->l, t->r);
    else {
        if (index < cnt(t->l))
            remove(t->l, index);
        else
            remove(t->r, index - cnt(t->l) - 1);
    }
    update(t);
}

void remove(int index) {
    remove(root, index);
}

int maxv(int a, int b) {
    node *l1, *r1;
    split(root, l1, r1, b + 1);
    node *l2, *r2;
    split(l1, l2, r2, a);
    int res = maxv(r2);
    node *t;
    merge(t, l2, r2);
    merge(root, t, r1);
    return res;
}

void print(node *t) {
    if (t) {
        print(t->l);
        cout << t->value << " ";
        print(t->r);
    }
}

void print() {
    print(root);
    cout << endl;
}

int main() {
    for (int i = 0; i < MAXN; i++)
        rnd.push_back(i);
    random_shuffle(rnd.begin(), rnd.end());

    add(0, 1);
    add(0, 2);
    add(0, 3);
    cout << maxv(1, 2) << endl;
    print();

    remove(1);
    print();
    cout << get(0) << endl;
}
