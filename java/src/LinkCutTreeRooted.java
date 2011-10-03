public class LinkCutTreeRooted {

	int[] left;
	int[] right;
	int[] parent;
	boolean[] root;

	public LinkCutTreeRooted(int n) {
		left = new int[n + 1];
		right = new int[n + 1];
		parent = new int[n + 1];
		root = new boolean[n + 1];
	}

	void connect(int child[], int p, int v) {
		child[p] = v;
		parent[v] = p;
	}

	void rotate(int v) {
		int p = parent[v];
		int[] l = v == left[p] ? left : right;
		int[] r = v == left[p] ? right : left;
		connect(l, p, r[v]);
		connect(l, parent[p], v);
		root[v] |= root[p];
		root[p] = false;
	}

	void splay(int v) {
		while (parent[v] != 0) {
			int p = parent[v];
			int pp = parent[p];
			if (pp != 0) {
				// zig-zig or zig-zag
				rotate((v == left[p]) == (p == left[pp]) ? p : v);
			}
			rotate(v);
		}
	}

	// precondition: x is the root, x and y are not connected
	public void link(int x, int y) {
		access(x);
		splay(x);

		root[left[x]] = true;
		parent[left[x]] = 0;
		left[x] = 0;
		parent[x] = y;
		access(x);
	}

	// precondition: v is not a root node
	public void cut(int x) {
		access(x);

		if (x.left != null) {
			x.left.parent = null;
			x.left.path_parent = null;
			x.left = null;
		}
	}

	public int findRoot(int x) {
		access(x);

		while (x.left != null)
			x = x.left;

		splay(x);
		return x;
	}

	void access(int x) {
		splay(x);

		if (x.right != null) {
			x.right.path_parent = x;
			x.right.parent = null;
			x.right = null;
		}

		for (Node t = x; t.path_parent != null;) {
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

		splay(x);
	}

	public int lca(int u, int v) {
		if (findRoot(u) != findRoot(v))
			return -1;

		access(u);
		splay(v);

		if (v.right != null) {
			v.right.path_parent = v;
			v.right.parent = null;
			v.right = null;
		}

		int lca = v;

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

	// Usage example
	public static void main(String[] args) {
		LinkCutTreeRooted t = new LinkCutTreeRooted(5);

		t.link(0, 1);
		t.link(2, 1);
		t.link(3, 2);
		System.out.println(1 == t.lca(0, 3));
		t.cut(3);
		System.out.println(-1 == t.lca(0, 3));
		t.link(4, 2);
		System.out.println(2 == t.lca(0, 4));
	}
}
