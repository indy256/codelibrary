public class LinkCutTree {

	public static class Node {
		Node left;
		Node right;
		Node parent;
		Node path_parent;
	}

	// prerequisite: v is a root node, w is in another tree
	public static void link(Node v, Node w) {
		access(v);
		access(w);

		v.left = w;
		w.parent = v;
		w.path_parent = null;
	}

	// prerequisite: v is not a root node
	public static void cut2(Node v) {
		access(v);

		if (v.left != null) {
			v.left.parent = null;
			v.left.path_parent = null;
			v.left = null;
		}
	}

	public static void cut(Node v) {
		splay(v);

		if (v.left != null) {
			v.left.parent = null;
			v.left = null;
		}

		v.path_parent = null;
	}

	public static Node findRoot(Node v) {
		access(v);

		while (v.left != null)
			v = v.left;

		splay(v);
		return v;
	}

	static void access(Node v) {
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

	public static Node lca(Node u, Node v) {
		if (findRoot(u) != findRoot(v))
			return null;

		access(u);
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

	static void connect(Node x, Node y, boolean left) {
		if (left)
			y.left = x;
		else
			y.right = x;
		if (x != null)
			x.parent = y;
	}

	static void rotate(Node x) {
		Node y = x.parent;
		Node z = y.parent;
		if (x == y.left) {
			connect(x.right, y, true);
			connect(y, x, false);
		} else {
			connect(x.left, y, false);
			connect(y, x, true);
		}
		connect(x, z, false);

		// connect x to z as left child
		x.parent = z;
		if (z != null) {
			if (y == z.left)
				z.left = x;
			else
				z.right = x;
		}
		x.path_parent = y.path_parent;
		y.path_parent = null;
	}

	static void rotate2(Node x) {
		Node y = x.parent;
		Node z = y.parent;
		if (x == y.left) {
			y.left = x.right;
			if (x.right != null)
				x.right.parent = y;
			x.right = y;
			y.parent = x;
		} else {
			y.right = x.left;
			if (x.left != null)
				x.left.parent = y;
			x.left = y;
			y.parent = x;
		}
		x.parent = z;
		if (z != null) {
			if (y == z.left)
				z.left = x;
			else
				z.right = x;
		}
		x.path_parent = y.path_parent;
		y.path_parent = null;
	}

	static void splay(Node x) {
		while (x.parent != null) {
			Node y = x.parent;
			Node z = y.parent;
			if (z == null) {
				// zig
				rotate(x);
			} else if ((x == y.left) == (y == z.left)) {
				// zig-zig
				rotate(y);
				rotate(x);
			} else {
				// zig-zag
				rotate(x);
				rotate(x);
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
