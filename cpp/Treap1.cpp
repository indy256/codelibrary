struct Node {
	int key, aux, size;
	Node *l, *r; // BST w.r.t. key; min-heap w.r.t. aux

	Node(int k) : key(k), aux(rand()), size(1), l(0), r(0) {
	}
};

Node *upd(Node *p) {
	if (p) p->size = 1 + (p->l ? p->l->size : 0)+(p->r ? p->r->size : 0);
	return p;
}

void split(Node *p, Node *by, Node **L, Node **R) {
	if (p == NULL) {
		*L = *R = NULL;
	} else if (p->key < by->key) {
		split(p->r, by, &p->r, R);
		*L = upd(p);
	} else {
		split(p->l, by, L, &p->l);
		*R = upd(p);
	}
}

Node *merge(Node *L, Node *R) {
	Node *p;
	if (L == NULL || R == NULL) p = (L != NULL ? L : R);
	else if (L->aux < R->aux) {
		L->r = merge(L->r, R);
		p = L;
	} else {
		R->l = merge(L, R->l);
		p = R;
	}
	return upd(p);
}

Node *insert(Node *p, Node *n) {
	if (p == NULL) return upd(n);
	if (n->aux <= p->aux) {
		split(p, n, &n->l, &n->r);
		return upd(n);
	}
	if (n->key < p->key) p->l = insert(p->l, n);
	else p->r = insert(p->r, n);
	return upd(p);
}

Node *erase(Node *p, int key) {
	if (p == NULL) return NULL;
	if (key == p->key) {
		Node *q = merge(p->l, p->r);
		delete p;
		return upd(q);
	}
	if (key < p->key) p->l = erase(p->l, key);
	else p->r = erase(p->r, key);
	return upd(p);
}