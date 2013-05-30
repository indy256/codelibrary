// Based on http://www.codeforces.com/contest/117/submission/860934
public class LinkCutTreeLca {

	public static class Node {
		Node left;
		Node right;
		Node parent;
	}

	// Whether x is a root of a splay tree
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

	// rotate edge (x, x.parent)
	static void rotate(Node x) {
		Node p = x.parent;
		Node g = p.parent;
		boolean isRootP = isRoot(p);
		boolean leftChildX = (x == p.left);

		connect(leftChildX ? x.right : x.left, p, leftChildX);
		connect(p, x, !leftChildX);

		if (!isRootP)
			connect(x, g, p == g.left);
		else
			x.parent = g;
	}

	static void splay(Node x) {
		while (!isRoot(x)) {
			Node p = x.parent;
			Node g = p.parent;
			if (!isRoot(p))
				rotate((x == p.left) == (p == g.left) ? p : x);
			rotate(x);
		}
	}

	// Makes node x the root of the virtual tree, and also x is the leftmost node in its splay tree
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
		if (findRoot(x) == findRoot(y))
			throw new RuntimeException("error: x and y are connected");
		expose(x);
		if (x.right != null)
			throw new RuntimeException("error: x is not a root node");
		x.parent = y;
	}

	public static void cut(Node x) {
		expose(x);
		if (x.right == null)
			throw new RuntimeException("error: x is a root node");
		x.right.parent = null;
		x.right = null;
	}

	public static Node lca(Node x, Node y) {
		if (findRoot(x) != findRoot(y))
			throw new RuntimeException("error: x and y are not connected");
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
		link(n5, n3);
		System.out.println(n2 == lca(n1, n5));
	}
}
