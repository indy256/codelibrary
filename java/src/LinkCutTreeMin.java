import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkCutTreeMin {

	public static class Node {
		Node left;
		Node right;
		Node parent;

		int id;
		int minId;
		long value;
		long delta;
		long min;

		Object o;
		long savedValue;

		void update() {
			min = value;
			minId = id;
			if (delta != 0) throw new RuntimeException();
			if (left != null && min > left.min) {
				min = left.min;
				minId = left.minId;
			}
			if (right != null && min >= right.min) {
				min = right.min;
				minId = right.minId;
			}
		}

		void applyDelta(long delta) {
			this.delta += delta;
			this.value += delta;
			this.min += delta;
		}

		void pushDelta() {
			if (left != null)
				left.applyDelta(delta);
			if (right != null)
				right.applyDelta(delta);
			delta = 0;
		}

		public Node(int id, int value) {
			this.id = id;
			this.value = value;
			update();
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
		p.pushDelta();
		x.pushDelta();
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
		x.pushDelta();
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
	public static void link(Node x, Node y, int newValue, Object o) {
		if (findRoot(x) == findRoot(y))
			throw new RuntimeException("error: x and y are connected");
		expose(x);
		if (x.right != null)
			throw new RuntimeException("error: x is not a root node");
		x.parent = y;
		x.value = newValue;
		x.update();
		x.o = o;
	}

	public static void cut(Node x) {
		expose(x);
		if (x.right == null)
			throw new RuntimeException("error: x is a root node");
		x.right.parent = null;
		x.right = null;
		x.savedValue = x.value;
		x.value = Integer.MAX_VALUE;
	}

	public static long min(Node x) {
		expose(x);
		return x.min;
	}

	public static int minId(Node x) {
		expose(x);
		return x.minId;
	}

	public static void add(Node x, long delta) {
		expose(x);
		x.applyDelta(delta);
	}

	// Usage example
	public static void main(String[] args) {
//		Node n0 = new Node(0);
//		Node n1 = new Node(1);
//		link(n0, n1);
//		add(n0, 1);
//		add(n1, 1);
//		System.out.println(min(n0));
//		System.out.println(min(n1));
//		System.exit(0);

		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(30) + 1;
			Node[] nodes1 = new Node[n];
			TreeForestValue.Node[] nodes2 = new TreeForestValue.Node[n];

			for (int i = 0; i < n; i++) {
				nodes1[i] = new Node(i, i);
				nodes2[i] = new TreeForestValue.Node(i, i);
			}

			final List<Integer>[] tree = getRandomTree(n, rnd);
			for (int u = 0; u < n; u++) {
				for (int v : tree[u]) {
					link(nodes1[v], nodes1[u], 0, null);
					TreeForestValue.link(nodes2[v], nodes2[u]);
				}
				for (int i = 0; i < n; i++) {
					int v = rnd.nextInt(10) + 1;
					add(nodes1[i], v);
					TreeForestValue.add(nodes2[i], v);
				}
				for (int i = 0; i < n; i++) {
					int min1 = minId(nodes1[i]);
					int min2 = TreeForestValue.minId(nodes2[i]);
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
					int min1 = minId(nodes1[i]);
					int min2 = TreeForestValue.minId(nodes2[i]);
					if (min1 != min2) {
						throw new RuntimeException();
					}
				}
			}
		}
		System.out.println("Tests passed");
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
