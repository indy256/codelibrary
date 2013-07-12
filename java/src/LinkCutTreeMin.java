import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkCutTreeMin {

	public static class Node {
		Node left;
		Node right;
		Node parent;

		int value;
		int min;

		void update() {
			min = value;
			if (left != null)
				min = Math.min(min, left.min);
			if (right != null)
				min = Math.min(min, right.min);
		}

		public Node(int value) {
			this.value = value;
			this.min = value;
		}
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
		p.update();
	}

	static void splay(Node x) {
		while (!isRoot(x)) {
			Node p = x.parent;
			Node g = p.parent;
			if (!isRoot(p))
				rotate((x == p.left) == (p == g.left) ? p : x);
			rotate(x);
		}
		x.update();
	}

	// Makes node x the root of the virtual tree, and also x is the leftmost node in its splay tree
	static Node expose(Node x) {
		Node last = null;
		for (Node y = x; y != null; y = y.parent) {
			splay(y);
			y.left = last;
			y.update();
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

	public static int min(Node x) {
		expose(x);
		return x.min;
	}

	// Usage example
	public static void main(String[] args) {
//		Node n1 = new Node(1);
//		Node n2 = new Node(2);
//		Node n3 = new Node(3);
//		Node n4 = new Node(4);
//		Node n5 = new Node(5);
//
//		link(n2, n1);
//		link(n3, n1);
//		link(n4, n3);
//
//		System.out.println(min(n4));

		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(2) + 1;
			Node[] nodes1 = new Node[n];
			TreeForestValue.Node[] nodes2 = new TreeForestValue.Node[n];

			for (int i = 0; i < n; i++) {
				nodes1[i] = new Node(i);
				nodes2[i] = new TreeForestValue.Node(i);
			}

			final List<Integer>[] tree = getRandomTree(n, rnd);
			for (int u = 0; u < n; u++) {
				for (int v : tree[u]) {
					link(nodes1[v], nodes1[u]);
					TreeForestValue.link(nodes2[v], nodes2[u]);
				}
				for (int i = 0; i < n; i++) {
					int min1 = min(nodes1[i]);
					int min2 = TreeForestValue.min(nodes2[i]);
					if (min1 != min2) {
						throw new RuntimeException();
					}
				}
			}
			for (int u = 0; u < n; u++) {
				if (TreeForestValue.isRoot(nodes2[u])) continue;
				cut(nodes1[u]);
				TreeForestValue.cut(nodes2[u]);
				for (int i = 0; i < n; i++) {
					int min1 = min(nodes1[i]);
					int min2 = TreeForestValue.min(nodes2[i]);
					if (min1 != min2) {
						throw new RuntimeException();
					}
				}
			}
		}
	}

	public static List<Integer>[] getRandomTree(int n, Random rnd) {
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++)
			t[i] = new ArrayList<>();
		int[] p = getRandomPermutation(n, rnd);
		for (int i = 1; i < n; i++) {
			int parent = p[rnd.nextInt(i)];
			t[parent].add(p[i]);
//			t[p[i]].add(parent);
		}
		return t;
	}

	static int[] getRandomPermutation(int n, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = i;
		}
		for (int i = res.length - 1; i > 0; i--) {
			int j = rnd.nextInt(i + 1);
			int t = res[i];
			res[i] = res[j];
			res[j] = t;
		}
		return res;
	}
}
