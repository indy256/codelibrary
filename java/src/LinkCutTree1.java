import java.util.*;

public class LinkCutTree1 {

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

	void leftRotate(Node x) {
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
	}

	void rightRotate(Node x) {
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
	}

	void splay(Node v) {
		while (v.parent != null) {
			Node p = v.parent;
			Node pp = p.parent;
			if (v == p.left) {
				if (pp != null) {
					if (p == pp.left) {
						// zig-zig
						rightRotate(pp);
						rightRotate(p);
					} else {
						// zig-zag
						rightRotate(p);
						leftRotate(pp);
					}
				} else {
					// single rotation
					rightRotate(p);
				}
			} else {
				if (pp != null) {
					if (p == pp.right) {
						// zig-zig
						leftRotate(pp);
						leftRotate(p);
					} else {
						// zig-zag
						leftRotate(p);
						rightRotate(pp);
					}
				} else {
					// single rotation
					leftRotate(p);
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

	// prerequisite: v is a root node
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

	Node lca(Node v, Node w) {
		Node r1 = findRoot(v);
		Node r2 = findRoot(w);

		if (r1 != r2)
			return null;

		Node lca = null;
		expose(v);

		splay(w);

		if (w.right != null) {
			w.right.path_parent = w;
			w.right.parent = null;
			w.right = null;
		}

		if (w.path_parent == null) {
			lca = w;
		}

		Node t = w;
		while (t.path_parent != null) {
			Node x = t.path_parent;
			splay(x);

			if (x.path_parent == null) {
				lca = x;
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
		return lca;
	}

	// Usage example
	public static void main(String[] args) {
		LinkCutTree1 tree = new LinkCutTree1();

		// Node n1 = tree.makeTree(1);
		// Node n2 = tree.makeTree(2);
		// Node n3 = tree.makeTree(3);
		// Node n4 = tree.makeTree(4);
		//
		// tree.link(n1, n2);
		// tree.link(n3, n2);
		// tree.link(n4, n3);
		//
		// Node lca = tree.lca(n1, n4);
		// System.out.println(2 == lca.id);

		tree = new LinkCutTree1();
		int n = 100000;
		Node[] nodes1 = new Node[n];
		TreeForest.Node[] nodes2 = new TreeForest.Node[n];
		Map<Node, Integer> set1 = new IdentityHashMap<Node, Integer>();
		Map<TreeForest.Node, Integer> set2 = new IdentityHashMap<TreeForest.Node, Integer>();
		for (int i = 0; i < n; i++) {
			nodes1[i] = tree.makeTree(i);
			nodes2[i] = new TreeForest.Node();
			set1.put(nodes1[i], i);
			set2.put(nodes2[i], i);
		}
		Random rnd = new Random(1);
		int linkCount = 0;
		int cutCount = 0;
		int lcaCount = 0;
		for (int step = 0; step < 1000000; step++) {
			if (rnd.nextInt(10) < 9) {
				if (linkCount < 100000) {
					int u = rnd.nextInt(n);
					int v = rnd.nextInt(n);
					if (TreeForest.findRoot(nodes2[u]) != TreeForest.findRoot(nodes2[v]) && nodes2[u].parent == null) {
						TreeForest.link(nodes2[u], nodes2[v]);
						tree.link(nodes1[u], nodes1[v]);
						// System.out.println("link");
						++linkCount;
					}
				}
			} else {
				int u = rnd.nextInt(n);
				if (nodes2[u].parent != null) {
					if (cutCount < 100000) {
						TreeForest.cut(nodes2[u]);
						tree.cut(nodes1[u]);
						// System.out.println("cut");
						++cutCount;
					}
				}
			}

			int u = rnd.nextInt(n);
			int v = rnd.nextInt(n);
			if (TreeForest.findRoot(nodes2[u]) == TreeForest.findRoot(nodes2[v])) {
				TreeForest.Node lca2 = TreeForest.lca(nodes2[u], nodes2[v]);
				Node lca1 = tree.lca(nodes1[u], nodes1[v]);
				int r1 = set1.get(lca1);
				int r2 = set2.get(lca2);
				if (r1 != r2) {
					System.err.println(u + " " + v + " " + r1 + " " + r2);
				}
				// System.out.println("lca " + u + " " + v + " " + r1 + " " +
				// r2);
				++lcaCount;
			}
		}
		System.out.println("linkCount = " + linkCount);
		System.out.println("cutCount = " + cutCount);
		System.out.println("lcaCount = " + lcaCount);
	}
}
