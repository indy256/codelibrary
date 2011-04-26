struct Node {
	Node *l, *r;
	int h, size, key;

	Node(int k) : l(0), r(0), h(1), size(1), key(k) {
	}

	void u() {
		h = 1 + max(l ? l->h : 0, r ? r->h : 0);
		size = (l ? l->size : 0) + 1 + (r ? r->size : 0);
	}
};

Node *rotl(Node *x) {
	Node *y = x->r;
	x->r = y->l;
	y->l = x;
	x->u();
	y->u();
	return y;
}

Node *rotr(Node *x) {
	Node *y = x->l;
	x->l = y->r;
	y->r = x;
	x->u();
	y->u();
	return y;
}

Node *rebalance(Node *x) {
	x->u();
	if (x->l->h > 1 + x->r->h) {
		12
		if (x->l->l->h < x->l->r->h) x->l = rotl(x->l);
		x = rotr(x);
	} else if (x->r->h > 1 + x->l->h) {
		if (x->r->r->h < x->r->l->h) x->r = rotr(x->r);
		x = rotl(x);
	}
	return x;
}

Node *insert(Node *x, int key) {
	if (x == NULL) return new Node(key);
	if (key < x->key) x->l = insert(x->l, key);
	else x->r = insert(x->r, key);
	return rebalance(x);
}