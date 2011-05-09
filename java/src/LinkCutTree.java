public class LinkCutTree {

	public static class Node {
		Node left;
		Node right;
		Node parent;
		Node path_parent;
	}

	// prerequisite: v is a root node, w is in another tree
	public static void link(Node v, Node w) {
		expose(v);
		expose(w);

		v.left = w;
		w.parent = v;
		w.path_parent = null;
	}

	// prerequisite: v is not a root node
	public static void cut(Node v) {
		expose(v);

		if (v.left != null) {
			v.left.parent = null;
			v.left.path_parent = null;
			v.left = null;
		}
	}

	public static Node findRoot(Node v) {
		expose(v);

		while (v.left != null)
			v = v.left;

		splay(v);
		return v;
	}

	public static Node lca(Node u, Node v) {
		if (findRoot(u) != findRoot(v))
			return null;

		expose(u);
		splay(v);

		if (v.right != null) {
			v.right.path_parent = v;
			v.right.parent = null;
			v.right = null;
		}

		Node lca = v;

		for (Node t = v; t.path_parent != null;) {
			Node w = t.path_parent;
			splay(w);

			if (w.path_parent == null)
				lca = w;

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
		return lca;
	}

	static void expose(Node v) {
		splay(v);

		if (v.right != null) {
			v.right.path_parent = v;
			v.right.parent = null;
			v.right = null;
		}

		for (Node t = v; t.path_parent != null;) {
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

	static void rotate(Node v) {
		Node p = v.parent;
		Node pp = p.parent;
		if (v == p.left) {
			p.left = v.right;
			if (v.right != null)
				v.right.parent = p;
			v.right = p;
			p.parent = v;
		} else {
			p.right = v.left;
			if (v.left != null)
				v.left.parent = p;
			v.left = p;
			p.parent = v;
		}
		v.parent = pp;
		if (pp != null) {
			if (p == pp.left)
				pp.left = v;
			else
				pp.right = v;
		}
		v.path_parent = p.path_parent;
		p.path_parent = null;
	}

	static void splay(Node v) {
		while (v.parent != null) {
			Node p = v.parent;
			Node pp = p.parent;
			if (pp == null) {
				// zig
				rotate(v);
			} else if ((v == p.left) == (p == pp.left)) {
				// zig-zig
				rotate(p);
				rotate(v);
			} else {
				// zig-zag
				rotate(v);
				rotate(v);
			}
		}
	}

	// Usage example
	public static void main(String[] args) {
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();

		link(n1, n2);
		link(n3, n2);
		link(n4, n3);
		System.out.println(n2 == lca(n1, n4));
		cut(n4);
		System.out.println(null == lca(n1, n4));
		link(n5, n3);
		System.out.println(n2 == lca(n1, n5));
	}
}
