import java.util.Arrays;

public class LinkCutTreeRooted {

	int[] left;
	int[] right;
	int[] parent;
	boolean[] root;
	int[] v;
	int[] sum;

	public LinkCutTreeRooted(int n) {
		left = new int[n + 1];
		right = new int[n + 1];
		parent = new int[n + 1];
		root = new boolean[n + 1];
		v = new int[n + 1];
		sum = new int[n + 1];
	}

	void update(int x) {
		sum[x] = v[x] + sum[left[x]] + sum[right[x]];
	}

	void set(int x, int value) {
		splay(x);
		v[x] = value;
		update(x);
	}

	void connect(int p, int v, int[] child) {
		if (!root[v])
			child[p] = v;
		if (v != 0)
			parent[v] = p;
	}

	void rotate(int x) {
		int y = parent[x];
		int z = parent[y];
		root[x] |= root[y];
		root[y] = false;
		int[] l = x == left[y] ? left : right;
		int[] r = x == left[y] ? right : left;
		connect(y, r[x], l);
		connect(x, y, r);
		connect(z, x, l);
		update(x);
		update(y);
	}

	void splay(int x) {
		while (parent[x] != 0) {
			int y = parent[x];
			int z = parent[y];
			if (z != 0) {
				// zig or zag
				rotate((x == left[y]) == (y == left[z]) ? y : x);
			}
			// zig
			rotate(x);
		}
	}

	// precondition: x is the root, x and y are not connected
	public void link(int x, int y) {
		access(x);

		root[y] = false;
		left[x] = y;
		parent[y] = x;
	}

	// precondition: v is not a root node
	public void cut(int x) {
		access(x);
		root[left[x]] = true;
		parent[left[x]] = 0;
		left[x] = 0;
	}

	public int findRoot(int x) {
		access(x);

		while (left[x] != 0)
			x = left[x];

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
