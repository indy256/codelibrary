public class LinkCutTree {

	static class Node {
		int id;
		Node left;
		Node right;
		Node parent;
		Node path_parent;

		Node(int id) {
			this.id = id;
		}
	}

	Node makeTree(int id) {
		return new Node(id);
	}

	Node leftRotate(Node x) {
		Node y = x.right;
		x.right = y.left;

		if (y.left != null)
			y.left.parent = x;

		y.path_parent = x.path_parent;
		x.path_parent = null;
		y.parent = x.parent;

		if (x.parent != null && x == x.parent.left)
			x.parent.left = y;
		else if (x.parent != null && x == x.parent.right)
			x.parent.right = y;

		y.left = x;
		x.parent = y;
		return y;
	}

	Node rightRotate(Node x) {
		Node y = x.left;
		x.left = y.right;

		if (y.right != null)
			y.right.parent = x;

		y.path_parent = x.path_parent;
		x.path_parent = null;
		y.parent = x.parent;

		if (x.parent != null && x == x.parent.left)
			x.parent.left = y;
		else if (x.parent != null && x == x.parent.right)
			x.parent.right = y;

		y.right = x;
		x.parent = y;
		return y;
	}

	void splay(Node v) {
		if (v == null)
			return;

		while (v.parent != null) {
			Node p = v.parent;
			Node pp = p.parent;
			if (p.left == v) {
				if (pp != null) {
					if (pp.left == p) {
						// zig zig
						rightRotate(pp);
						v = rightRotate(p);
					} else if (pp.right == p) {
						// zig zag
						rightRotate(p);
						v = leftRotate(pp);
					}
				} else {
					// single rotation
					v = rightRotate(p);
				}
			} else if (p.right == v) {
				if (pp != null) {
					if (pp.left == p) {
						// zig zag
						leftRotate(p);
						v = rightRotate(pp);
					} else if (pp.right == p) {
						// zig zig
						leftRotate(pp);
						v = leftRotate(p);
					}
				} else {
					// single rotation
					v = leftRotate(p);
				}
			}
		}
	}

	void expose(Node v) {
		splay(v);

		if (v.right != null) {
			v.right.path_parent = v;
			v.right.parent = null;
			v.right = null;
		}

		Node t = v;
		while (t.path_parent != null) {
			Node w = t.path_parent;
			splay(w);

			// switch
			if (w.right != null) {
				w.right.path_parent = w;
				w.right.parent = null;
			}

			w.right = t;
			t.parent = w;
			t.path_parent = null;

			t = w;
		}

		splay(v);
	}

	Node findRoot(Node v) {
		expose(v);

		Node t = v;
		while (t.left != null)
			t = t.left;

		splay(t);
		return t;
	}

	void link(Node v, Node w) {
		expose(v);
		expose(w);

		v.left = w;
		w.parent = v;
		w.path_parent = null;
	}

	void cut(Node v) {
		expose(v);

		if (v.left != null) {
			v.left.parent = null;
			v.left.path_parent = null;
			v.left = null;
		}
	}

	Node nca(Node v, Node w) {
		Node r1 = findRoot(v);
		Node r2 = findRoot(w);

		if (r1 != r2)
			return null;

		Node nca = null;
		expose(v);

		splay(w);

		if (w.right != null) {
			w.right.path_parent = w;
			w.right.parent = null;
			w.right = null;
		}

		if (w.path_parent == null) {
			nca = w;
		}

		Node t = w;
		while (t.path_parent != null) {
			Node x = t.path_parent;
			splay(x);

			if (x.path_parent == null) {
				nca = x;
			}

			// switch
			if (x.right != null) {
				x.right.path_parent = x;
				x.right.parent = null;
			}

			x.right = t;
			t.parent = x;
			t.path_parent = null;

			t = x;
		}

		splay(w);
		return nca;
	}

	// Usage example
	public static void main(String[] args) {
		LinkCutTree tree = new LinkCutTree();

		Node n1 = tree.makeTree(1);
		Node n2 = tree.makeTree(2);
		Node n3 = tree.makeTree(3);
		Node n4 = tree.makeTree(4);

		tree.link(n1, n2);
		tree.link(n3, n2);
		tree.link(n4, n3);

		Node nca = tree.nca(n1, n4);
		System.out.println(nca.id);
	}
}
