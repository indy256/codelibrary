// Based on http://www.codeforces.com/contest/117/submission/860934
public class LinkCutTreeConnectivity {

	public static class Node {
		Node left;
		Node right;
		Node parent;
		boolean flip;
	}

	static void push(Node x) {
		if (!x.flip)
			return;
		x.flip = false;
		Node t = x.left;
		x.left = x.right;
		x.right = t;
		if (x.left != null)
			x.left.flip = !x.left.flip;
		if (x.right != null)
			x.right.flip = !x.right.flip;
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
				push(g);
			push(p);
			push(x);
			if (!isRoot(p))
				rotate((x == p.left) == (p == g.left) ? p : x);
			rotate(x);
		}
		push(x);
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
		while (x.right != null) {
			x = x.right;
			push(x);
		}
		return x;
	}

	// prerequisite: x and y are in different trees
	public static void link(Node x, Node y) {
		if (findRoot(x) == findRoot(y))
			throw new RuntimeException("error: x and y are connected");
		expose(x);
		x.flip = !x.flip; // evert
		x.parent = y;
	}

	public static boolean connected(Node x, Node y) {
		if (x == y)
			return true;
		expose(x);
		expose(y);
		return x.parent != null;
	}

	public static void cut(Node x, Node y) {
		expose(x);
		x.flip = !x.flip; // evert
		expose(y);
		if (y.right != x || x.left != null || x.right != null)
			throw new RuntimeException("error: no edge (x,y)");
		y.right.parent = null;
		y.right = null;
	}

	// Usage example
	public static void main(String[] args) {
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();

		link(n1, n2);
		link(n3, n2);
		System.out.println(false == connected(n1, n4));
		link(n4, n3);
		System.out.println(true == connected(n4, n1));
		cut(n2, n3);
		System.out.println(false == connected(n1, n4));
	}
}
