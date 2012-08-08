public class LinkCutTree {

	public static class Node {
		Node left;
		Node right;
		Node parent;
	}

	static boolean isRoot(Node x) {
		return x.parent == null || (x.parent.left != x && x.parent.right != x);
	}

	static void connect(Node ch, Node p, boolean leftChild) {
		if (leftChild)
			p.left = ch;
		else
			p.right = ch;
		if (ch != null)
			ch.parent = p;
	}

	static void rotate(Node x) {
		Node p = x.parent;
		Node g = p.parent;

		boolean leftChild = (x == p.left);
		connect(leftChild ? x.right : x.left, p, leftChild);
		connect(p, x, !leftChild);

		x.parent = g;
		if (g != null && (g.left == p || g.right == p))
			connect(x, g, p == g.left);
	}

	static void splay(Node x) {
		while (!isRoot(x)) {
			Node p = x.parent;
			Node g = p.parent;
			if (isRoot(p)) {
				// zig
				rotate(x);
			} else if ((x == p.left) == (p == g.left)) {
				// zig-zig
				rotate(p);
				rotate(x);
			} else {
				// zig-zag
				rotate(x);
				rotate(x);
			}
		}
	}

	static Node expose(Node x) {
		Node last = null;
		for (Node y = x; y != null; y = y.parent) {
			splay(y);
			y.left = last;
			last = y;
		}
		splay(x);
		return last;
	}

	public static Node findRoot(Node x) {
		expose(x);
		while (x.right != null)
			x = x.right;
		return x;
	}

	// prerequisite: x is a root node, y is in another tree
	public static void link(Node x, Node y) {
		expose(x);
		if (x.right != null)
			throw new RuntimeException("error: x is not a root node");
		x.parent = y;
	}

	public static void cut(Node x) {
		expose(x);
		x.right.parent = null;
		x.right = null;
	}

	public static Node lca(Node x, Node y) {
		if (findRoot(x) != findRoot(y))
			return null;
		expose(x);
		return expose(y);
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
