#include <iostream>
#include <climits>
#include <algorithm>
#include <vector>
using namespace std;

// specific code
const int MAXN = 200000;

const int NEUTRAL_VALUE = INT_MIN;
const int NEUTRAL_DELTA = 0;

int joinValues(int leftValue, int rightValue) {
	return max(leftValue, rightValue);
}

int joinDeltas(int oldDelta, int newDelta) {
	return oldDelta + newDelta;
}

int joinValueWithDelta(int value, int delta, int length) {
	return value + delta;
}

// generic code
struct treap {
	int nodeValue;
	int subTreeValue;
	int delta;
	int count;
	int prio;
	treap *l;
	treap *r;
};

treap nodes[MAXN];
int nodes_cnt;
vector<int> rnd;

static int getCount(treap* root) {
	return root ? root->count : 0;
}

static int getSubTreeValue(treap* root) {
	return root ? root->subTreeValue : NEUTRAL_VALUE;
}

void update(treap *root) {
	if (!root) return;
	root->subTreeValue = joinValues(joinValues(getSubTreeValue(root->l), root->nodeValue), getSubTreeValue(root->r));
	root->count = 1 + getCount(root->l) + getCount(root->r);
}

void applyDelta(treap *root, int delta) {
	if (!root) return;
	root->delta = joinDeltas(root->delta, delta);
	root->nodeValue = joinValueWithDelta(root->nodeValue, delta, 1);
	root->subTreeValue = joinValueWithDelta(root->subTreeValue, delta, root->count);
}

void pushDelta(treap* root) {
	if (!root) return;
	applyDelta(root->l, root->delta);
	applyDelta(root->r, root->delta);
	root->delta = NEUTRAL_DELTA;
}

void merge(treap* &t, treap* l, treap* r) {
	pushDelta(l);
	pushDelta(r);
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

void split(treap* t, treap* &l, treap* &r, int key) {
	pushDelta(t);
	if (!t)
		l = r = NULL;
	else if (key <= getCount(t->l))
		split(t->l, l, t->l, key), r = t;
	else
		split(t->r, t->r, r, key - getCount(t->l) - 1), l = t;
	update(t);
}

int get(treap* t, int index) {
	pushDelta(t);
	if (index < getCount(t->l))
		return get(t->l, index);
	else if (index > getCount(t->l))
		return get(t->r, index - getCount(t->l) - 1);
	return t->nodeValue;
}

void insert(treap* &t, treap* item, int index) {
	pushDelta(t);
	if (!t)
		t = item;
	else if (item->prio < t->prio)
		split(t, item->l, item->r, index), t = item;
	else if (index <= getCount(t->l))
		insert(t->l, item, index);
	else
		insert(t->r, item, index - getCount(t->l) - 1);
	update(t);
}

void insert(treap* &root, int index, int value) {
	treap *item = &nodes[nodes_cnt];
	item->nodeValue = value;
	item->subTreeValue = value;
	item->delta = NEUTRAL_DELTA;
	item->count = 1;
	item->prio = rnd[nodes_cnt];
	// it->prio = (rand() << 15) + rand();
	++nodes_cnt;
	insert(root, item, index);
}

void remove(treap* &t, int index) {
	pushDelta(t);
	if (index == getCount(t->l))
		merge(t, t->l, t->r);
	else
		if (index < getCount(t->l))
			remove(t->l, index);
		else
			remove(t->r, index - getCount(t->l) - 1);
	update(t);
}

int query(treap* &root, int a, int b) {
	treap *l1, *r1;
	split(root, l1, r1, b + 1);
	treap *l2, *r2;
	split(l1, l2, r2, a);
	int res = getSubTreeValue(r2);
	treap *t;
	merge(t, l2, r2);
	merge(root, t, r1);
	return res;
}

void modify(treap* &root, int a, int b, int delta) {
	treap *l1, *r1;
	split(root, l1, r1, b + 1);
	treap *l2, *r2;
	split(l1, l2, r2, a);
	int res = getSubTreeValue(r2);
	treap *t;
	merge(t, l2, r2);
	merge(root, t, r1);
}

void print(treap* t) {
	if (!t) return;
	pushDelta(t);
	print(t->l);
	cout << t->nodeValue << " ";
	print(t->r);
}

int main() {
	for (int i = 0; i < MAXN; i++)
		rnd.push_back(i);
	random_shuffle(rnd.begin(), rnd.end());

	treap* t = NULL;

	insert(t, 0, 1);
	insert(t, 0, 2);
	insert(t, 0, 3);
	cout << query(t, 1, 2) << endl;
	print(t);
	cout << endl;

	remove(t, 1);
	print(t);
	cout << endl;
	cout << get(t, 0) << endl;
}
